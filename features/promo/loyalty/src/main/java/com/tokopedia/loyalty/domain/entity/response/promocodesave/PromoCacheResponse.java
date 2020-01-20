package com.tokopedia.loyalty.domain.entity.response.promocodesave;

import com.google.gson.annotations.SerializedName;

public class PromoCacheResponse{

	@SerializedName("SavePromoHistory")
	private SavePromoHistory savePromoHistory;

	public void setSavePromoHistory(SavePromoHistory savePromoHistory){
		this.savePromoHistory = savePromoHistory;
	}

	public SavePromoHistory getSavePromoHistory(){
		return savePromoHistory;
	}

	@Override
 	public String toString(){
		return 
			"PromoCacheResponse{" + 
			"savePromoHistory = '" + savePromoHistory + '\'' + 
			"}";
		}
}