package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName

data class Flags(
    @SerializedName("isOrderTradeIn")
    val isOrderTradeIn:Boolean = false
)