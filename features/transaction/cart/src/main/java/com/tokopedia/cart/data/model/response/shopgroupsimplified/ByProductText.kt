package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ByProductText(
        @SerializedName("in_cart")
        val inCart: String = "",
        @SerializedName("last_stock_less_than")
        val lastStockLessThan: String? = null,
        @SerializedName("complete")
        val complete: String? = null
)