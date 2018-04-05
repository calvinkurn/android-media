package com.tokopedia.tkpd.tkpdcontactus.home.data;

import com.google.gson.annotations.SerializedName;

public class ContactUsArticleResponse{

	@SerializedName("guid")
	private RenderedData guid;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private RenderedData title;

	public void setGuid(RenderedData guid){
		this.guid = guid;
	}

	public RenderedData getGuid(){
		return guid;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(RenderedData title){
		this.title = title;
	}

	public RenderedData getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"ContactUsArticleResponse{" + 
			"guid = '" + guid + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}