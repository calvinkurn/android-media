package com.tokopedia.home.account.data.model.tokopointshortcut

import com.google.gson.annotations.SerializedName

data class Cta(

	@SerializedName("appLink")
	val appLink: String? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("url")
	val url: String? = null
)