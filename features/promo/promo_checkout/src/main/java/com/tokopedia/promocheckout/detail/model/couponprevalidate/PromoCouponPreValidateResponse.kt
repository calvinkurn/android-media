package com.tokopedia.promocheckout.detail.model.couponprevalidate

import com.google.gson.annotations.SerializedName

data class PromoCouponPreValidateResponse(

	@SerializedName("validateRedeem")
	val validateRedeem: ValidateRedeem? = null
)