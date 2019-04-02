package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class LinksItemEntity {

	@SerializedName("backgroundURL")
	private String backgroundURL;

	@SerializedName("backgroundColor")
	private String backgroundColor;

	@SerializedName("appLink")
	private String appLink;

	@SerializedName("id")
	private int id;

	@SerializedName("text")
	private String text;

	@SerializedName("fontColor")
	private String fontColor;

	@SerializedName("url")
	private String url;

	public void setBackgroundURL(String backgroundURL){
		this.backgroundURL = backgroundURL;
	}

	public String getBackgroundURL(){
		return backgroundURL;
	}

	public void setBackgroundColor(String backgroundColor){
		this.backgroundColor = backgroundColor;
	}

	public String getBackgroundColor(){
		return backgroundColor;
	}

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

	public void setFontColor(String fontColor){
		this.fontColor = fontColor;
	}

	public String getFontColor(){
		return fontColor;
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
			"backgroundURL = '" + backgroundURL + '\'' + 
			",backgroundColor = '" + backgroundColor + '\'' + 
			",appLink = '" + appLink + '\'' + 
			",id = '" + id + '\'' + 
			",text = '" + text + '\'' + 
			",fontColor = '" + fontColor + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}