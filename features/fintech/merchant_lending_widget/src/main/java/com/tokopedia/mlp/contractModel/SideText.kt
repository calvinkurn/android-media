package com.tokopedia.mlp.contractModel

import com.google.gson.annotations.SerializedName
data class SideText(

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("url")
	val url: String? = null
)
