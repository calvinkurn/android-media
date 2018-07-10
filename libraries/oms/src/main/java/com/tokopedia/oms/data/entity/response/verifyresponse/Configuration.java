package com.tokopedia.oms.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class Configuration{

	@SerializedName("conv_fee")
	private Integer convFee;

	@SerializedName("price")
	private Integer price;

	@SerializedName("sub_config")
	private SubConfig subConfig;

	public void setConvFee(Integer convFee){
		this.convFee = convFee;
	}

	public Integer getConvFee(){
		return convFee;
	}

	public void setPrice(Integer price){
		this.price = price;
	}

	public Integer getPrice(){
		return price;
	}

	public void setSubConfig(SubConfig subConfig){
		this.subConfig = subConfig;
	}

	public SubConfig getSubConfig(){
		return subConfig;
	}

	@Override
 	public String toString(){
		return 
			"Configuration{" + 
			"conv_fee = '" + convFee + '\'' + 
			",price = '" + price + '\'' + 
			",sub_config = '" + subConfig + '\'' + 
			"}";
		}
}