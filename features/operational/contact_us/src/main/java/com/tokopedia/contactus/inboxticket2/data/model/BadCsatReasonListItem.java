package com.tokopedia.contactus.inboxticket2.data.model;

public class BadCsatReasonListItem{
	private String messageEn;
	private int id;
	private String message;

	public void setMessageEn(String messageEn){
		this.messageEn = messageEn;
	}

	public String getMessageEn(){
		return messageEn;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"BadCsatReasonListItem{" + 
			"message_en = '" + messageEn + '\'' + 
			",id = '" + id + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}
