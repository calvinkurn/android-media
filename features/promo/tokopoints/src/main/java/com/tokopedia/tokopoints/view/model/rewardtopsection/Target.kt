package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class Target(

	@SerializedName("backgroundColor")
	val backgroundColor: String? = null,

	@SerializedName("text")
	val text: String? = null,

	@SerializedName("textColor")
	val textColor: String? = null
)