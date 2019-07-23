package com.tokopedia.saldodetails.response.lateCountModel;

import com.google.gson.annotations.SerializedName;

public class MclGetLatedetails{

	@SerializedName("lateCount")
	private int lateCount;

	@SerializedName("softblockThreshold")
	private int softblockThreshold;

	@SerializedName("hardblockThreshold")
	private int hardblockThreshold;

	public void setLateCount(int lateCount){
		this.lateCount = lateCount;
	}

	public int getLateCount(){
		return lateCount;
	}

	public void setSoftblockThreshold(int softblockThreshold){
		this.softblockThreshold = softblockThreshold;
	}

	public int getSoftblockThreshold(){
		return softblockThreshold;
	}

	public void setHardblockThreshold(int hardblockThreshold){
		this.hardblockThreshold = hardblockThreshold;
	}

	public int getHardblockThreshold(){
		return hardblockThreshold;
	}

	@Override
 	public String toString(){
		return 
			"MclGetLatedetails{" + 
			"lateCount = '" + lateCount + '\'' + 
			",softblockThreshold = '" + softblockThreshold + '\'' + 
			",hardblockThreshold = '" + hardblockThreshold + '\'' + 
			"}";
		}
}