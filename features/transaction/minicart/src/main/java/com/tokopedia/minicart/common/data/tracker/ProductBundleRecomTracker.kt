package com.tokopedia.minicart.common.data.tracker

data class ProductBundleRecomTracker(
    val shopId: String = "",
    val warehouseId: String = "",
    val bundleId: String = "",
    val bundleName: String = "",
    val bundleType: String = "",
    val bundlePosition: Int = 0,
    val priceCut: String = "",
    val cartId: String = "",
    val quantity: String = "",
    val state: String = ""
)