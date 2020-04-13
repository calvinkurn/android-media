package com.tokopedia.product.manage.feature.filter.data.model

import com.google.gson.annotations.SerializedName

data class ProductListMetaWrapper(
        @SerializedName("header")
        val productListMetaHeader: ProductListMetaHeader = ProductListMetaHeader(),
        @SerializedName("data")
        val productListMetaData: ProductListMetaData =  ProductListMetaData()
)