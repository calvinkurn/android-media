package com.tokopedia.contactus.inboxticket2.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class CreatedBy{

	@SerializedName("role")
	private String role;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("picture")
	private String picture;

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}
}