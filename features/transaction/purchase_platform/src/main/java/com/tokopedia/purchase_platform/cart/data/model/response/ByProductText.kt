package com.tokopedia.purchase_platform.cart.data.model.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ByProductText(

	@field:SerializedName("in_cart")
	val inCart: String = "",

	@field:SerializedName("last_stock_less_than")
	val lastStockLessThan: String? = null,

	@field:SerializedName("complete")
	val complete: String? = null
)