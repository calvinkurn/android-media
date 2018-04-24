package com.tokopedia.tkpd.tkpdcontactus.home.data;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class TopBotStatus{

	@SerializedName("is_active")
	private boolean isActive;

	@SerializedName("is_success")
	private int isSuccess;

	@SerializedName("msg_id")
	private int msgId;

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

	@Override
 	public String toString(){
		return 
			"TopBotStatus{" + 
			"is_active = '" + isActive + '\'' + 
			",is_success = '" + isSuccess + '\'' + 
			",msg_id = '" + msgId + '\'' + 
			"}";
		}
}