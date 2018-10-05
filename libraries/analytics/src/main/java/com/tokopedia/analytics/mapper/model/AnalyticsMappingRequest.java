package com.tokopedia.analytics.mapper.model;

public class AnalyticsMappingRequest{
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
			"AnalyticsMappingRequest{" + 
			"customerUserId = '" + customerUserId + '\'' + 
			",appsflyerId = '" + appsflyerId + '\'' + 
			"}";
		}
}
