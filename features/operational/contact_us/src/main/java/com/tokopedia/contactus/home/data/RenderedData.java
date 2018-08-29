package com.tokopedia.contactus.home.data;

import com.google.gson.annotations.SerializedName;

public class RenderedData {

	@SerializedName("rendered")
	private String rendered;

	public void setRendered(String rendered){
		this.rendered = rendered;
	}

	public String getRendered(){
		return rendered;
	}

	@Override
 	public String toString(){
		return 
			"RenderedData{" +
			"rendered = '" + rendered + '\'' + 
			"}";
		}
}