package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName

data class ProductListData(
    @SerializedName("ProductList")
    val productList: ProductList? = null
)