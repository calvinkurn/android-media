package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TokopointsDynamicLinkEntity {

	@SerializedName("layoutType")
	private String layoutType;

	@SerializedName("links")
	private List<LinksItemEntity> links;

	public void setLayoutType(String layoutType){
		this.layoutType = layoutType;
	}

	public String getLayoutType(){
		return layoutType;
	}

	public void setLinks(List<LinksItemEntity> links){
		this.links = links;
	}

	public List<LinksItemEntity> getLinks(){
		return links;
	}

	@Override
 	public String toString(){
		return 
			"TokopointsDynamicLinkEntity{" +
			"layoutType = '" + layoutType + '\'' + 
			",links = '" + links + '\'' + 
			"}";
		}
}