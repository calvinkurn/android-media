package com.tokopedia.purchase_platform.atc_variant.model

import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

data class ProductInfoAndVariant(
        var productInfo: ProductInfo = ProductInfo(),
        var productVariant: ProductVariant = ProductVariant()
)