package com.tokopedia.promogamification.common.floating.data.entity;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResultStatus{

	@SerializedName("reason")
	private String reason;

	@SerializedName("code")
	private String code;

	@SerializedName("message")
	private List<String> message;

	public void setReason(String reason){
		this.reason = reason;
	}

	public String getReason(){
		return reason;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setMessage(List<String> message){
		this.message = message;
	}

	public List<String> getMessage(){
		return message;
	}
}