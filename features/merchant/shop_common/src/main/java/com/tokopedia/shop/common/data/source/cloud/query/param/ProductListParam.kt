package com.tokopedia.shop.common.data.source.cloud.query.param

import com.google.gson.annotations.SerializedName

data class ProductListParam(
    @SerializedName("id")
    val id: String,
    @SerializedName("value")
    val value: Any
)