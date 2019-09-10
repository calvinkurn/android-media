package com.tokopedia.promocheckout.detail.model.couponprevalidate

import com.google.gson.annotations.SerializedName

data class PromoCouponPrevalidateResponse(

	@SerializedName("hachikoValidateRedeem")
	val hachikoValidateRedeem: HachikoValidateRedeem? = null
)