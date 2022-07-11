package com.tokopedia.promocheckout.detail.model.couponprevalidate

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ValidateRedeem(

	@SerializedName("message_success")
	val messageSuccess: String? = null,

	@SuppressLint("Invalid Data Type")
	@SerializedName("is_valid")
	val isValid: Int? = null,

	@SerializedName("message_title")
	val messageTitle: String? = null
)