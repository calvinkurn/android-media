package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.data.model.variant.ProductVariant

data class ProductInfoP1(
        var productInfo: ProductInfo = ProductInfo(),
        var productVariant: ProductVariant? = null
)