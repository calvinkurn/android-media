package com.tokopedia.cart.data.model.response.shopgroupsimplified

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ByUser(

	@field:SerializedName("in_cart")
	val inCart: Int = 0,

	@field:SerializedName("last_stock_less_than")
	val lastStockLessThan: Int = 0
)