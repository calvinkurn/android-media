package com.tokopedia.contactus.inboxticket2.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChipGetInboxDetail{
	@SerializedName("data")
	private Data data;
	@SerializedName("messageError")
	private List<String> messageError;

	public void setMessageError(List<String> messageError){
		this.messageError = messageError;
	}

	public List<String> getMessageError(){
		return messageError;
	}
	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ChipGetInboxDetail{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}
