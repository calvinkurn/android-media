package com.tokopedia.shop.common.data.source.cloud.model.productlist


import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("GetProductList")
    val getProductList: GetProductList = GetProductList()
)