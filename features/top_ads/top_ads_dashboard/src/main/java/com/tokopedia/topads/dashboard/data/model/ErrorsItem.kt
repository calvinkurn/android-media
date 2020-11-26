package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

data class ErrorsItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)