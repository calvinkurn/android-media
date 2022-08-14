package com.tokopedia.shop.common.widget.bundle.model

import com.tokopedia.kotlin.model.ImpressHolder

data class ShopHomeProductBundleItemUiModel(
        var bundleGroupId: String = "0",
        var bundleType: String = "",
        var bundleName: String = "",
        var bundleDetails: List<ShopHomeProductBundleDetailUiModel> = listOf(),
        var bundleProducts: List<ShopHomeBundleProductUiModel> = listOf(),
        var warehouseId: String = "0",
        var shopId: String = "0"
): ImpressHolder() {
    companion object {
        const val DEFAULT_BUNDLE_PRODUCT_PARENT_ID = "0"
        const val DEFAULT_BUNDLE_QUANTITY = 1
    }
}