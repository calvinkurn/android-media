package com.tokopedia.promocheckout.detail.model.userpoints

import com.google.gson.annotations.SerializedName

data class Tokopoints(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("status")
	val status: Status? = null
)
