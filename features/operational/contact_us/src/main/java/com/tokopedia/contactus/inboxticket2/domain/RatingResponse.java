package com.tokopedia.contactus.inboxticket2.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class RatingResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("is_success")
	private int isSuccess;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setIsSuccess(int isSuccess){
		this.isSuccess = isSuccess;
	}

	public int getIsSuccess(){
		return isSuccess;
	}
}