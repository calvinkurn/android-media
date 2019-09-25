package com.tokopedia.promocheckout.detail.model.couponprevalidate

import com.google.gson.annotations.SerializedName

data class ValidateRedeem(

	@SerializedName("message_success")
	val messageSuccess: String? = null,

	@SerializedName("is_valid")
	val isValid: Int? = null,

	@SerializedName("message_title")
	val messageTitle: String? = null
)