package com.tokopedia.promogamification.common.floating.data.entity;

import com.google.gson.annotations.SerializedName;

public class GamiFloatingClickData {

	@SerializedName("resultStatus")
	private ResultStatus resultStatus;

	public void setResultStatus(ResultStatus resultStatus){
		this.resultStatus = resultStatus;
	}

	public ResultStatus getResultStatus(){
		return resultStatus;
	}
}