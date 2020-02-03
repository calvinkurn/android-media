package com.tokopedia.events.data.entity.response.searchresponse;

import com.google.gson.annotations.SerializedName;

public class Page{

	@SerializedName("uri_prev")
	private String uriPrev;

	@SerializedName("uri_next")
	private String uriNext;

	public void setUriPrev(String uriPrev){
		this.uriPrev = uriPrev;
	}

	public String getUriPrev(){
		return uriPrev;
	}

	public void setUriNext(String uriNext){
		this.uriNext = uriNext;
	}

	public String getUriNext(){
		return uriNext;
	}

	@Override
 	public String toString(){
		return 
			"Page{" + 
			"uri_prev = '" + uriPrev + '\'' + 
			",uri_next = '" + uriNext + '\'' + 
			"}";
		}
}