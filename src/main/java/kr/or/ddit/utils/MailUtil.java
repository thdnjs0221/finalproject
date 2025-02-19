package kr.or.ddit.utils;

//https://mvnrepository.com/artifact/com.sun.mail/javax.mail/1.6.2


//sendMail메소드를 static 맹글어 쓰면 아주 편하지용
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class MailUtil {
	public static void sendMail(Map<String, String> mailDTO) {
	    String subject = mailDTO.get("title");
	    String fromMail = mailDTO.get("fromMail");
	    String password = mailDTO.get("password");
	    String fromName = mailDTO.get("fromName");
	    String toMail = mailDTO.get("toMail");
	    String contents = mailDTO.get("contents");

	    // mail properties
	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.naver.com"); // use naver mail
	    props.put("mail.smtp.port", "465"); // set port
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true"); // use TLS
	    props.put("mail.smtps.ssl.checkserveridentity", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    

	    Session mailSession = Session.getInstance(props,
	            new javax.mail.Authenticator() { // set authenticator
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(fromMail, password);
	                }
	            });

	    try {
	        MimeMessage message = new MimeMessage(mailSession);

	        // B(Base64) or Q(Quoted-Printabl) Encoding
	        message.setFrom(new InternetAddress(fromMail, MimeUtility.encodeText(fromName, "UTF-8", "B"))); // 한글의 경우 encoding 필요
	        message.setRecipients(
	            Message.RecipientType.TO, 
	            InternetAddress.parse(toMail)
	        );
	        message.setSubject(subject);
	        message.setContent(contents.toString(), "text/html;charset=UTF-8"); // 내용 설정 (HTML 형식)
	        message.setSentDate(new java.util.Date());

	        Transport t = mailSession.getTransport("smtp");
	        t.connect(fromMail, password);
	        t.sendMessage(message, message.getAllRecipients());
	        t.close();

	        System.out.println("잘 보내졌어요 ~!");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
