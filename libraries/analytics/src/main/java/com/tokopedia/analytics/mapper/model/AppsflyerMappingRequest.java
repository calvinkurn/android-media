package com.tokopedia.analytics.mapper.model;

import com.google.gson.annotations.SerializedName;

public class AppsflyerMappingRequest {

	@SerializedName("customerUserId")
	private String customerUserId;

	@SerializedName("appsflyerId")
	private String appsflyerId;

	public void setCustomerUserId(String customerUserId){
		this.customerUserId = customerUserId;
	}

	public String getCustomerUserId(){
		return customerUserId;
	}

	public void setAppsflyerId(String appsflyerId){
		this.appsflyerId = appsflyerId;
	}

	public String getAppsflyerId(){
		return appsflyerId;
	}

	@Override
 	public String toString(){
		return 
			"AppsflyerMappingRequest{" +
			"customerUserId = '" + customerUserId + '\'' + 
			",appsflyerId = '" + appsflyerId + '\'' + 
			"}";
		}
}
