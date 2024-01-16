package com.tokopedia.shop.info.data.request

import com.google.gson.annotations.SerializedName

data class GetShopStatsRawDataRequestParam(
    @SerializedName("shopID")
    val shopID: String,
    @SerializedName("source")
    val source: String
)
