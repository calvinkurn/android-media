package com.tokopedia.product_bundle.multiple.presentation.model

data class ProductBundleMaster(
        val bundleId: Long = 0L,
        val bundleName: String = "",
        val preOrderStatus: String = "",
        val processDay: Long = 0L,
        val processTypeNum: Int = 0
)