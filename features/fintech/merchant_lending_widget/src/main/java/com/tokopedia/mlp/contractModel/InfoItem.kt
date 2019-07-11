package com.tokopedia.mlp.contractModel

import com.google.gson.annotations.SerializedName
data class InfoItem(

	@SerializedName("label")
	val label: String? = null,

	@SerializedName("value")
	val value: String? = null
)

