package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class MfGetUserInfo(

	@SerializedName("verified_msisdn")
	val verifiedMsisdn: Boolean? = null
)