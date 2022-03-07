package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class CartEmptyInfo(
	@field:SerializedName("image_url")
	val imageUrl: String = "",
	@field:SerializedName("detail")
	val detail: String = "",
	@field:SerializedName("message")
	val message: String = ""
)