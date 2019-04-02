package com.tokopedia.contactus.common.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Paging implements Serializable {

	@SerializedName("uri_previous")
	private String uriPrevious;

	@SerializedName("uri_next")
	private String uriNext;

	public void setUriPrevious(String uriPrevious){
		this.uriPrevious = uriPrevious;
	}

	public String getUriPrevious(){
		return uriPrevious;
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
			"Paging{" + 
			"uri_previous = '" + uriPrevious + '\'' + 
			",uri_next = '" + uriNext + '\'' + 
			"}";
		}
}