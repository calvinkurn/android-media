package com.tokopedia.contactus.inboxticket2.data.model;

public class ChipInboxDetails{
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
