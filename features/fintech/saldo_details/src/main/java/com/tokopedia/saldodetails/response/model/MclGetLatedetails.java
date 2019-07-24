package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class MclGetLatedetails{

	@SerializedName("lateCount")
	private int lateCount;

	public void setLateCount(int lateCount){
		this.lateCount = lateCount;
	}

	public int getLateCount(){
		return lateCount;
	}

	@Override
 	public String toString(){
		return 
			"MclGetLatedetails{" + 
			"lateCount = '" + lateCount + '\'' +
			"}";
		}
}
