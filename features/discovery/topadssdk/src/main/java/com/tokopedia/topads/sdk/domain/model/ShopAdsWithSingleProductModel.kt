package com.tokopedia.topads.sdk.domain.model

import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener

data class ShopAdsWithSingleProductModel(
    var isOfficial: Boolean = false,
    var isPMPro: Boolean = false,
    var isPowerMerchant: Boolean = false,
    var shopBadge: String = "",
    var shopName: String = "",
    var shopImageUrl: String = "",
    var variant: Int = 10,
    var shopWidgetImageUrl: String = "",
    var slogan: String = "",
    var merchantVouchers: MutableList<String> = mutableListOf(),
    var listItem: Product? = null,
    val shopApplink: String,
    val adsClickUrl: String,
    val hasAddToCartButton: Boolean,
    val topAdsBannerClickListener: TopAdsBannerClickListener?,
    val cpmData: CpmData,
)
