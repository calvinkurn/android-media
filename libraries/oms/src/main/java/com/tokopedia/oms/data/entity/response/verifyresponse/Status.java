package com.tokopedia.oms.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class Status{

	@SerializedName("result")
	private String result;

	@SerializedName("code")
	private Integer code;

	@SerializedName("message")
	private Message message;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setCode(Integer code){
		this.code = code;
	}

	public Integer getCode(){
		return code;
	}

	public void setMessage(Message message){
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"Status{" + 
			"result = '" + result + '\'' + 
			",code = '" + code + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}