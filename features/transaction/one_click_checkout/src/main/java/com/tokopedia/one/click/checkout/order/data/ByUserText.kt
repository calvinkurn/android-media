package com.tokopedia.one.click.checkout.order.data

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ByUserText(

	@field:SerializedName("in_cart")
	val inCart: String = "",

	@field:SerializedName("last_stock_less_than")
	val lastStockLessThan: String = "",

	@field:SerializedName("complete")
	val complete: String = ""
)