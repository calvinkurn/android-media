package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class PhoneVerificationResponse(

	@SerializedName("mf_get_user_info")
	val mfGetUserInfo: MfGetUserInfo? = null
)