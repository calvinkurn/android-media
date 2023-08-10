package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ByProduct(
    @SerializedName("in_cart")
    val inCart: Int = 0,
    @SerializedName("last_stock_less_than")
    val lastStockLessThan: Int = 0
)
