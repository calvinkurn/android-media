package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class Configuration{

	@SerializedName("conv_fee")
	private int convFee;

	@SerializedName("price")
	private int price;

	@SerializedName("sub_config")
	private SubConfig subConfig;

	public void setConvFee(int convFee){
		this.convFee = convFee;
	}

	public int getConvFee(){
		return convFee;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
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