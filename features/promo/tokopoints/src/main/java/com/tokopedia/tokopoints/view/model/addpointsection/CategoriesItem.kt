package com.tokopedia.tokopoints.view.model.addpointsection

import com.google.gson.annotations.SerializedName

data class CategoriesItem(

	@SerializedName("appLink")
	val appLink: String? = null,

	@SerializedName("icon")
	val icon: String? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("type")
	val type: String? = null,

	@SerializedName("url")
	val url: String? = null
)
