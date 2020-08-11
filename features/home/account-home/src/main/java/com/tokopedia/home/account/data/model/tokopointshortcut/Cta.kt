package com.tokopedia.home.account.data.model.tokopointshortcut

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cta(

	@SerializedName("appLink")
	@Expose
	val appLink: String = "",

	@SerializedName("text")
	@Expose
	val text: String = "",

	@SerializedName("url")
	@Expose
	val url: String = ""
)