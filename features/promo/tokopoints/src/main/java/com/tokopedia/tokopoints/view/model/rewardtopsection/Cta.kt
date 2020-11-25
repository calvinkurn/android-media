package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class Cta(

	@SerializedName("appLink")
	val appLink: String? = null,

	@SerializedName("icon")
	val icon: String? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("url")
	val url: String? = null
)