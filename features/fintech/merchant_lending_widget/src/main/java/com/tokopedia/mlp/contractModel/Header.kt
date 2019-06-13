package com.tokopedia.mlp.contractModel

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Header(

	@SerializedName("side_toggle")
	val sideToggle: SideToggle? = null,

	@SerializedName("side_text")
	val sideText: SideText? = null,

	@SerializedName("logo")
	val logo: String? = null,

	@SerializedName("tag")
	val tag: Tag? = null,

	@SerializedName("title")
	val title: String? = null
)