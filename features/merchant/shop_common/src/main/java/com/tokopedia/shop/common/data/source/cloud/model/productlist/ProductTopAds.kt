package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName

data class ProductTopAds(
    @SerializedName("status")
    val status: Int,
    @SerializedName("management")
    val management: Int
)