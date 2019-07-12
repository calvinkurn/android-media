package com.tokopedia.mlp.contractModel

import com.google.gson.annotations.SerializedName
data class SideToggle(

	@SerializedName("toggle_status")
	val toggleStatus: Boolean? = null,

	@SerializedName("show_toggle")
	val showToggle: Boolean? = null,

	@SerializedName("url")
	val url: String? = null
)