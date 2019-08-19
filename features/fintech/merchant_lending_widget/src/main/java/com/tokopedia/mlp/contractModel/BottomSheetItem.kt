package com.tokopedia.mlp.contractModel

import com.google.gson.annotations.SerializedName
data class BottomSheetItem(

	@SerializedName("button_cta")
	val buttonCta: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("url")
	val url: String? = null
)

