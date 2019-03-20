package com.tokopedia.normalcheckout.model

import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

data class ProductInfoAndVariant(
        var productInfo: ProductInfo = ProductInfo(),
        var productVariant: ProductVariant = ProductVariant()
)