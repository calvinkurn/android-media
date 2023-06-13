package com.tokopedia.minicart.common.data.tracker

data class ProductBundleRecomTracker(
    val shopId: String = "",
    val warehouseId: String = "",
    val bundleId: String = "",
    val bundleName: String = "",
    val bundleType: String = "",
    val bundlePosition: Int = 0,
    val priceCut: String = "",
    val state: String = "",
    val atcItems: List<ProductBundleRecomAtcItemTracker> = emptyList()
)
