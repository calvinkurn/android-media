package com.tokopedia.product.manage.common.list.data.model.filter

import com.google.gson.annotations.SerializedName

data class ProductListMetaResponse(
        @SerializedName("ProductListMeta")
        val productListMetaWrapper: ProductListMetaWrapper = ProductListMetaWrapper()
)