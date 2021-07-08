package com.tokopedia.product_bundle.multiple.presentation.model

class ProductBundleDetail(
        val productImageUrl: String = "",
        val productName: String = "",
        val originalPrice: Long = 0L,
        val bundlePrice: Long = 0L,
        val discountPercentage: Int = 0
)