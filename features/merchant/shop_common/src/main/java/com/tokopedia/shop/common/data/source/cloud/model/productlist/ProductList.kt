package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName

data class ProductList(
    @SerializedName("header")
    val header: Header?,
    @SerializedName("data")
    val data: List<Product>?
)