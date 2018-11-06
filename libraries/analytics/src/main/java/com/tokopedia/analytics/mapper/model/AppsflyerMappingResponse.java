package com.tokopedia.analytics.mapper.model;

import com.google.gson.annotations.SerializedName;

public class AppsflyerMappingResponse {

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
			"AppsflyerMappingResponse{" +
			"appsflyer_mapping = '" + appsflyerMapping + '\'' + 
			"}";
		}
}