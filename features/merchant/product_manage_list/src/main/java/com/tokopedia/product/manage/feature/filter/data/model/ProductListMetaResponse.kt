package com.tokopedia.product.manage.feature.filter.data.model

import com.google.gson.annotations.SerializedName

data class ProductListMetaResponse(
        @SerializedName("ProductListMeta")
        val productListMetaWrapper: ProductListMetaWrapper = ProductListMetaWrapper()
)