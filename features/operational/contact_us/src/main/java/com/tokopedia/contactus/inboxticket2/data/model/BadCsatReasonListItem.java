package com.tokopedia.contactus.inboxticket2.data.model;

import com.google.gson.annotations.SerializedName;

public class BadCsatReasonListItem{
	@SerializedName("messageEn")
	private String messageEn;
	@SerializedName("id")
	private int id;
	@SerializedName("message")
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
