package com.tokopedia.tkpd.tkpdcontactus.orderquery.data;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Payment{

	@SerializedName("method")
	private int method;

	@SerializedName("gateway_id")
	private int gatewayId;

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
			"method = '" + method + '\'' + 
			",gateway_id = '" + gatewayId + '\'' + 
			"}";
		}
}