package com.tokopedia.shop.common.widget.bundle.model

data class BundleDetailUiModel(
    var bundleId: String = "0",
    var originalPrice: String = "0",
    var displayPrice: String = "0",
    var displayPriceRaw: Long = 0,
    var discountPercentage: Int = 0,
    var isPreOrder: Boolean = false,
    var preOrderInfo: String = "",
    var savingAmountWording: String = "",
    var minOrder: Int = 0,
    var minOrderWording: String = "",
    var isSelected: Boolean = false,
    var totalSold: Int = 0,
    var shopInfo: BundleShopUiModel? = null,
    var products: List<BundleProductUiModel> = emptyList(),
    val productSoldInfo: String = "",
    val useProductSoldInfo: Boolean = true,
    var applink: String = "",
    var selectedBundleId: String = "0",
    var selectedBundleApplink: String = "",
    var bundleType: String = "",
)
