package com.tokopedia.oms.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class OtherChargesItem{

	@SerializedName("conv_fee")
	private Integer convFee;

	public void setConvFee(Integer convFee){
		this.convFee = convFee;
	}

	public Integer getConvFee(){
		return convFee;
	}

	@Override
 	public String toString(){
		return 
			"OtherChargesItem{" + 
			"conv_fee = '" + convFee + '\'' + 
			"}";
		}
}