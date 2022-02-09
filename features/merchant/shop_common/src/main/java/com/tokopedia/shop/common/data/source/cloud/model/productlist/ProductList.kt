package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductList(
    @SerializedName("header")
    @Expose
    val header: Header?,
    @SerializedName("meta")
    @Expose
    val meta: Meta? = Meta(),
    @SerializedName("data")
    @Expose
    val data: List<Product>?
)