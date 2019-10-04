package com.tokopedia.discovery.categoryrevamp.data.productModel

import com.google.gson.annotations.SerializedName

data class WholesalePriceItem(

	@field:SerializedName("quantityMax")
	val quantityMax: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("quantityMin")
	val quantityMin: Int? = null
)