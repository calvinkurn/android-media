package com.tokopedia.product.manage.common.feature.list.data.model.filter

import com.google.gson.annotations.SerializedName

data class ProductListMetaWrapper(
    @SerializedName("header")
        val productListMetaHeader: ProductListMetaHeader = ProductListMetaHeader(),
    @SerializedName("data")
        val productListMetaData: ProductListMetaData = ProductListMetaData()
)