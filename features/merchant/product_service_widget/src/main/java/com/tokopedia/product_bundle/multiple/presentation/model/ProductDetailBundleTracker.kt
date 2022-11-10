package com.tokopedia.product_bundle.multiple.presentation.model

data class ProductDetailBundleTracker (
        val productId: String = "0",
        val productName: String = "",
        val productPrice: String = "0",
        val cartId: String = "0",
        val quantity: Int = 0
)