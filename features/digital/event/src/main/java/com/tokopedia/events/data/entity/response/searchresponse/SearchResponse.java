package com.tokopedia.events.data.entity.response.searchresponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchResponse{

	@SerializedName("seo_url")
	private String seoUrl;

	@SerializedName("parent_id")
	private int parentId;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("grid_layout")
	private List<GridLayoutItem> gridLayout;

	@SerializedName("id")
	private int id;

	@SerializedName("filters")
	private List<FiltersItem> filters;

	@SerializedName("page")
	private Page page;

	@SerializedName("title")
	private String title;

	@SerializedName("url")
	private String url;

	@SerializedName("status")
	private int status;

	public void setSeoUrl(String seoUrl){
		this.seoUrl = seoUrl;
	}

	public String getSeoUrl(){
		return seoUrl;
	}

	public void setParentId(int parentId){
		this.parentId = parentId;
	}

	public int getParentId(){
		return parentId;
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

	public void setGridLayout(List<GridLayoutItem> gridLayout){
		this.gridLayout = gridLayout;
	}

	public List<GridLayoutItem> getGridLayout(){
		return gridLayout;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setFilters(List<FiltersItem> filters){
		this.filters = filters;
	}

	public List<FiltersItem> getFilters(){
		return filters;
	}

	public void setPage(Page page){
		this.page = page;
	}

	public Page getPage(){
		return page;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"SearchResponse{" + 
			"seo_url = '" + seoUrl + '\'' + 
			",parent_id = '" + parentId + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",grid_layout = '" + gridLayout + '\'' + 
			",id = '" + id + '\'' + 
			",filters = '" + filters + '\'' + 
			",page = '" + page + '\'' + 
			",title = '" + title + '\'' + 
			",url = '" + url + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}