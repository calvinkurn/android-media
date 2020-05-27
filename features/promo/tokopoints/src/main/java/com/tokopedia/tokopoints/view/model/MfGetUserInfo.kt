package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class MfGetUserInfo(

	@SerializedName("phone_verified")
	val verifiedMsisdn: Boolean? = null
)
