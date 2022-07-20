package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName

data class Message(

	@field:SerializedName("color")
	val color: String = "",

	@field:SerializedName("state")
	val state: String = "",

	@field:SerializedName("text")
	val text: String = ""
)