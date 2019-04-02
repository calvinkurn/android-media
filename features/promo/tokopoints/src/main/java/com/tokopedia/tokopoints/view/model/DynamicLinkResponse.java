package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class DynamicLinkResponse{

	@SerializedName("tokopointsDynamicLink")
	private TokopointsDynamicLinkEntity tokopointsDynamicLinkEntity;

	public void setTokopointsDynamicLinkEntity(TokopointsDynamicLinkEntity tokopointsDynamicLinkEntity){
		this.tokopointsDynamicLinkEntity = tokopointsDynamicLinkEntity;
	}

	public TokopointsDynamicLinkEntity getTokopointsDynamicLinkEntity(){
		return tokopointsDynamicLinkEntity;
	}

	@Override
 	public String toString(){
		return 
			"DynamicLinkResponse{" + 
			"tokopointsDynamicLinkEntity = '" + tokopointsDynamicLinkEntity + '\'' +
			"}";
		}
}