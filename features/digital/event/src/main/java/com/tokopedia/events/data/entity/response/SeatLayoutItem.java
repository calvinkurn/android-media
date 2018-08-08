package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeatLayoutItem {


	@SerializedName("layout")
	@Expose
	private String layout;

	public void setLayout(String layout){
		this.layout = layout;
	}

	public String getLayout(){
		return layout;
	}

}
