package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ByUserText(
    @SerializedName("in_cart")
    val inCart: String = "",
    @SerializedName("last_stock_less_than")
    val lastStockLessThan: String = "",
    @SerializedName("complete")
    val complete: String = ""
)
