package com.tokopedia.challenges.view.model.upload;

import com.google.gson.annotations.SerializedName;

public class Collection{

	@SerializedName("Type")
	private String type;

	@SerializedName("ThumbnailImage")
	private String thumbnailImage;

	@SerializedName("HashTag")
	private String hashTag;

	@SerializedName("Title")
	private String title;

	@SerializedName("Id")
	private String id;

	@SerializedName("Date")
	private String date;

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setThumbnailImage(String thumbnailImage){
		this.thumbnailImage = thumbnailImage;
	}

	public String getThumbnailImage(){
		return thumbnailImage;
	}

	public void setHashTag(String hashTag){
		this.hashTag = hashTag;
	}

	public String getHashTag(){
		return hashTag;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	@Override
 	public String toString(){
		return 
			"Collection{" + 
			"type = '" + type + '\'' + 
			",thumbnailImage = '" + thumbnailImage + '\'' + 
			",hashTag = '" + hashTag + '\'' + 
			",title = '" + title + '\'' + 
			",id = '" + id + '\'' + 
			",date = '" + date + '\'' + 
			"}";
		}
}