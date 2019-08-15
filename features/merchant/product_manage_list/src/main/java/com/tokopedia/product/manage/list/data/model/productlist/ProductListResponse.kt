package com.tokopedia.product.manage.list.data.model.productlist


import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("GetProductList")
    val getProductList: GetProductList = GetProductList()
)