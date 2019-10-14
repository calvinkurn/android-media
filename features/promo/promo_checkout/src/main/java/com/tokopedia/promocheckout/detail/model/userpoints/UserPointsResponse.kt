package com.tokopedia.promocheckout.detail.model.userpoints

import com.google.gson.annotations.SerializedName

data class UserPointsResponse(

	@SerializedName("tokopoints")
	val tokopoints: Tokopoints? = null
)
