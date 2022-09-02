package com.tokopedia.promogamification.common.floating.data.entity;

import com.google.gson.annotations.SerializedName;

public class GamiFloatingCloseClickResponse{

	@SerializedName("gamiFloatingClick")
	private GamiFloatingClickData gamiFloatingClick;

	public void setGamiFloatingClick(GamiFloatingClickData gamiFloatingClick){
		this.gamiFloatingClick = gamiFloatingClick;
	}

	public GamiFloatingClickData getGamiFloatingClick(){
		return gamiFloatingClick;
	}
}