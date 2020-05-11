package com.tokopedia.home.account.analytics.data.model


import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("shop_item_sold")
    val shopItemSold: String = ""
)