package com.tokopedia.qrscanner.branchio.entity;

import com.google.gson.annotations.SerializedName;

public class CampaignResponseEntity {

	@SerializedName("status")
	private int status;

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}