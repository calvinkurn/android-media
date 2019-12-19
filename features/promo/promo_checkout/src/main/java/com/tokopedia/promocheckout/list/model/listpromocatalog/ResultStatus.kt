package com.tokopedia.promocheckout.list.model.listpromocatalog

import com.google.gson.annotations.SerializedName

data class ResultStatus(

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("message")
	val message: List<String?>? = null,

	@SerializedName("status")
	val status: String? = null
)