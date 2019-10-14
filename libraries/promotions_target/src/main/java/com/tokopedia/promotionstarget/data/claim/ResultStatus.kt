package com.tokopedia.promotionstarget.data.claim

import com.google.gson.annotations.SerializedName

data class ResultStatus(

	@SerializedName("reason")
	val reason: String? = null,

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("message")
	val message: List<String?>? = null
)