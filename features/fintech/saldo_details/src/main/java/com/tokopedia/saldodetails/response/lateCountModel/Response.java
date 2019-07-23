package com.tokopedia.saldodetails.response.lateCountModel;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("mcl_get_latedetails")
	private MclGetLatedetails mclGetLatedetails;

	public void setMclGetLatedetails(MclGetLatedetails mclGetLatedetails){
		this.mclGetLatedetails = mclGetLatedetails;
	}

	public MclGetLatedetails getMclGetLatedetails(){
		return mclGetLatedetails;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"mcl_get_latedetails = '" + mclGetLatedetails + '\'' + 
			"}";
		}
}