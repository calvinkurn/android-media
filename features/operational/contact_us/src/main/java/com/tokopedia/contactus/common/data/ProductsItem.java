package com.tokopedia.contactus.common.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductsItem implements Serializable {

	@SerializedName("image")
	private String image;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	@Override
 	public String toString(){
		return 
			"ProductsItem{" + 
			"image = '" + image + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			"}";
		}
}