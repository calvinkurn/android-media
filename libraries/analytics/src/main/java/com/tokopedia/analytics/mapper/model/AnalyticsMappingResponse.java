package com.tokopedia.analytics.mapper.model;

import com.google.gson.annotations.SerializedName;

public class AnalyticsMappingResponse{

	@SerializedName("appsflyer_mapping")
	private AppsflyerMapping appsflyerMapping;

	public void setAppsflyerMapping(AppsflyerMapping appsflyerMapping){
		this.appsflyerMapping = appsflyerMapping;
	}

	public AppsflyerMapping getAppsflyerMapping(){
		return appsflyerMapping;
	}

	@Override
 	public String toString(){
		return 
			"AnalyticsMappingResponse{" + 
			"appsflyer_mapping = '" + appsflyerMapping + '\'' + 
			"}";
		}
}