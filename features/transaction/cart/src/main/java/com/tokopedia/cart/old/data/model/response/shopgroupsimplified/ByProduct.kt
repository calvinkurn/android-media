package com.tokopedia.cart.old.data.model.response.shopgroupsimplified

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ByProduct(

	@field:SerializedName("in_cart")
	val inCart: Int = 0,

	@field:SerializedName("last_stock_less_than")
	val lastStockLessThan: Int = 0
)