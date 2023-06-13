package com.tokopedia.product_bundle.multiple.presentation.model

data class ProductBundleMaster(
        val shopId: String = "0",
        val bundleId: Long = 0L,
        val bundleName: String = "",
        val quota: Int = 0,
        val preOrderStatus: String = "",
        val processDay: Long = 0L,
        val processTypeNum: Int = 0,
        val isSelected: Boolean = false,
        val totalSold: Int = 0
)
