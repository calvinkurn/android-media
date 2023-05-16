package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class BottomBar(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("is_shop_active")
    val isShopActive: Boolean = true,
    @SerializedName("total_price_fmt")
    val totalPriceFmt: String = ""
)
