package com.tokopedia.analytics.mapper.model;

public class AppsflyerMappingRequest {
	private String customerUserId;
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
