package com.tokopedia.topads.sdk.domain.model

data class ShopAdsWithSingleProductModel(
    var isOfficial: Boolean = false,
    var isPMPro: Boolean = false,
    var isPowerMerchant: Boolean = false,
    var shopBadge: String = "",
    var shopName: String = "",
    var applink: String = "",
    var shopImageUrl: String = "",
    var variant: Int = 10,
    var shopWidgetImageUrl: String = "",
    var slogan: String = "",
    var merchantVouchers: MutableList<String> = mutableListOf<String>(),
    var listItem: Product? = null,
    val shopApplink: String,
    val adsClickUrl: String,
    val hasAddToCartButton: Boolean,
)
