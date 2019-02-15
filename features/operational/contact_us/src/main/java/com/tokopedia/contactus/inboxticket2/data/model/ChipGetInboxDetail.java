package com.tokopedia.contactus.inboxticket2.data.model;

public class ChipGetInboxDetail{
	private Data data;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ChipGetInboxDetail{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}
