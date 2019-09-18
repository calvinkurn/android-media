package com.tokopedia.discovery.categoryrevamp.data.bannedCategory;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("banned_message")
	private String bannedMessage;

	@SerializedName("app_redirection")
	private String appRedirection;

	@SerializedName("is_banned")
	private int isBanned;

	@SerializedName("is_adult")
	private int isAdult;

	public void setBannedMessage(String bannedMessage){
		this.bannedMessage = bannedMessage;
	}

	public String getBannedMessage(){
		return bannedMessage;
	}

	public void setAppRedirection(String appRedirection){
		this.appRedirection = appRedirection;
	}

	public String getAppRedirection(){
		return appRedirection;
	}

	public void setIsBanned(int isBanned){
		this.isBanned = isBanned;
	}

	public int getIsBanned(){
		return isBanned;
	}

	public void setIsAdult(int isAdult){
		this.isAdult = isAdult;
	}

	public int getIsAdult(){
		return isAdult;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"banned_message = '" + bannedMessage + '\'' + 
			",app_redirection = '" + appRedirection + '\'' + 
			",is_banned = '" + isBanned + '\'' + 
			",is_adult = '" + isAdult + '\'' + 
			"}";
		}
}