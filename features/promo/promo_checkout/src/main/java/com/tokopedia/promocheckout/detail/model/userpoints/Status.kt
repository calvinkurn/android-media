package com.tokopedia.promocheckout.detail.model.userpoints

import com.google.gson.annotations.SerializedName

data class Status(

	@SerializedName("tier")
	val tier: Tier? = null,

	@SerializedName("points")
	val points: Points? = null
)
