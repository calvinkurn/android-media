package com.tokopedia.promocheckout.detail.model.detailmodel

import com.google.gson.annotations.SerializedName

data class CouponDetailsResponse(

	@SerializedName("hachikoCatalogDetail")
	val hachikoCatalogDetail: HachikoCatalogDetail? = null
)