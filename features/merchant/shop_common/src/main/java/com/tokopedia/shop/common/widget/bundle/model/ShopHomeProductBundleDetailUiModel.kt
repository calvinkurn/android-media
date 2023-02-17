package com.tokopedia.shop.common.widget.bundle.model

data class ShopHomeProductBundleDetailUiModel(
    var bundleId: String = "0",
    var originalPrice: String = "0",
    var displayPrice: String = "0",
    var displayPriceRaw: Long = 0,
    var discountPercentage: Int = 0,
    var isPreOrder: Boolean = false,
    var isProductsHaveVariant: Boolean = false,
    var preOrderInfo: String = "",
    var savingAmountWording: String = "",
    var minOrder: Int = 0,
    var minOrderWording: String = "",
    var isSelected: Boolean = false,
    var isFestivity: Boolean = false
)
