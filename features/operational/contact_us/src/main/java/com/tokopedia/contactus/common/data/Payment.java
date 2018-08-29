package com.tokopedia.contactus.common.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payment implements Serializable {

	@SerializedName("status_id")
	private int statusId;

	@SerializedName("method")
	private int method;

	@SerializedName("gateway_id")
	private int gatewayId;

	public void setStatusId(int statusId){
		this.statusId = statusId;
	}

	public int getStatusId(){
		return statusId;
	}

	public void setMethod(int method){
		this.method = method;
	}

	public int getMethod(){
		return method;
	}

	public void setGatewayId(int gatewayId){
		this.gatewayId = gatewayId;
	}

	public int getGatewayId(){
		return gatewayId;
	}

	@Override
 	public String toString(){
		return 
			"Payment{" + 
			"status_id = '" + statusId + '\'' + 
			",method = '" + method + '\'' + 
			",gateway_id = '" + gatewayId + '\'' + 
			"}";
		}
}