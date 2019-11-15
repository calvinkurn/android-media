package com.tokopedia.loyalty.domain.entity.response.promocodesave;

import com.google.gson.annotations.SerializedName;

public class SavePromoHistory{

	@SerializedName("Success")
	private boolean success;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"SavePromoHistory{" + 
			"success = '" + success + '\'' + 
			"}";
		}
}