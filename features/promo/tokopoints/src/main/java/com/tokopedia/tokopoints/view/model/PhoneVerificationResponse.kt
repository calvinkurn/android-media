package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class PhoneVerificationResponse(

	@SerializedName("profile")
	val mfGetUserInfo: MfGetUserInfo? = null
)
