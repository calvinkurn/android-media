package com.tokopedia.mlp.contractModel

import com.google.gson.annotations.SerializedName
data class Tag(

	@SerializedName("color")
	val color: String? = null,

	@SerializedName("name")
	val name: String? = null
)
