package com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ByUser(
        @SerializedName("in_cart")
        val inCart: Int = 0,
        @SerializedName("last_stock_less_than")
        val lastStockLessThan: Int = 0
)