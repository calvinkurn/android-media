package com.tokopedia.contactus.inboxticket2.data.model;

import com.google.gson.annotations.SerializedName;

public class ChipInboxDetails{
	@SerializedName(value = "chipGetInboxDetail", alternate = {"chipSubmitRatingCSAT"})
	private ChipGetInboxDetail chipGetInboxDetail;

	public void setChipGetInboxDetail(ChipGetInboxDetail chipGetInboxDetail){
		this.chipGetInboxDetail = chipGetInboxDetail;
	}

	public ChipGetInboxDetail getChipGetInboxDetail(){
		return chipGetInboxDetail;
	}

	@Override
 	public String toString(){
		return 
			"ChipInboxDetails{" + 
			"chipGetInboxDetail = '" + chipGetInboxDetail + '\'' + 
			"}";
		}
}
