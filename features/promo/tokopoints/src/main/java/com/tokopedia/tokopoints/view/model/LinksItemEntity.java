package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class LinksItemEntity {

	@SerializedName("appLink")
	private String appLink;

	@SerializedName("id")
	private int id;

	@SerializedName("text")
	private String text;

	@SerializedName("url")
	private String url;

	public void setAppLink(String appLink){
		this.appLink = appLink;
	}

	public String getAppLink(){
		return appLink;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"LinksItemEntity{" +
			",appLink = '" + appLink + '\'' +
			",id = '" + id + '\'' + 
			",text = '" + text + '\'' + 
			",url = '" + url + '\'' +
			"}";
		}
}