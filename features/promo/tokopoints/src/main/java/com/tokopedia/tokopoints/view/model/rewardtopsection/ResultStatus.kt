package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class ResultStatus(

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("status")
	val status: String? = null
)