package com.tokopedia.discovery.categoryrevamp.data.bannedCategory;

import com.google.gson.annotations.SerializedName;

public class Header{

	@SerializedName("code")
	private int code;

	@SerializedName("server_prosess_time")
	private String serverProsessTime;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setServerProsessTime(String serverProsessTime){
		this.serverProsessTime = serverProsessTime;
	}

	public String getServerProsessTime(){
		return serverProsessTime;
	}

	@Override
 	public String toString(){
		return 
			"Header{" + 
			"code = '" + code + '\'' + 
			",server_prosess_time = '" + serverProsessTime + '\'' + 
			"}";
		}
}