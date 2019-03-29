package com.tokopedia.contactus.inboxticket2.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class AttachmentItem{

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("url")
	private String url;

	public void setThumbnail(String thumbnail){
		this.thumbnail = thumbnail;
	}

	public String getThumbnail(){
		return thumbnail;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}