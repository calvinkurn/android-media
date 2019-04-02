package com.tokopedia.contactus.orderquery.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QueryTicket implements Serializable{

	@SerializedName("redirect")
	private String redirect;

	@SerializedName("is_skip_article")
	private boolean isSkipArticle;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("hide_form")
	private boolean hideForm;

	@SerializedName("id")
	private int id;

	public void setRedirect(String redirect){
		this.redirect = redirect;
	}

	public String getRedirect(){
		return redirect;
	}

	public void setIsSkipArticle(boolean isSkipArticle){
		this.isSkipArticle = isSkipArticle;
	}

	public boolean isIsSkipArticle(){
		return isSkipArticle;
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

	public void setHideForm(boolean hideForm){
		this.hideForm = hideForm;
	}

	public boolean isHideForm(){
		return hideForm;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"QueryTicket{" + 
			"redirect = '" + redirect + '\'' + 
			",is_skip_article = '" + isSkipArticle + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",hide_form = '" + hideForm + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}