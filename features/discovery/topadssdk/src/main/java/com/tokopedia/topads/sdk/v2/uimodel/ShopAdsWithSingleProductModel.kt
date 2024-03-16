package com.tokopedia.topads.sdk.v2.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener

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
    val impressionListener: TopAdsItemImpressionListener?,
    val cpmData: CpmData,
    val impressHolder: ImpressHolder? = null,
)
