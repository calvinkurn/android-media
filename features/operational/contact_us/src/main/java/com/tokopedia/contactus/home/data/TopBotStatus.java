package com.tokopedia.contactus.home.data;

import com.google.gson.annotations.SerializedName;

public class TopBotStatus{

	@SerializedName("is_active")
	private boolean isActive;

	@SerializedName("is_success")
	private int isSuccess;

	@SerializedName("msg_id")
	private int msgId;

	@SerializedName("welcome_msg")
	private String welcomeMessge;

	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}

	public boolean isIsActive(){
		return isActive;
	}

	public void setIsSuccess(int isSuccess){
		this.isSuccess = isSuccess;
	}

	public int getIsSuccess(){
		return isSuccess;
	}

	public void setMsgId(int msgId){
		this.msgId = msgId;
	}

	public int getMsgId(){
		return msgId;
	}

	public String getWelcomeMessge() {
		return welcomeMessge;
	}

	public void setWelcomeMessge(String welcomeMessge) {
		this.welcomeMessge = welcomeMessge;
	}

	@Override
 	public String toString(){
		return 
			"TopBotStatus{" + 
			"is_active = '" + isActive + '\'' + 
			",is_success = '" + isSuccess + '\'' + 
			",msg_id = '" + msgId + '\'' +
			", welcomeMessage = '"+ welcomeMessge + '\'' +
					"}";
		}
}