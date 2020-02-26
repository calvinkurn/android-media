package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("min")
    val min: Int? = null,
    @SerializedName("max")
    val max: Int? = null
)