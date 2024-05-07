package com.tokopedia.topads.sdk.domain.model

import com.tokopedia.topads.sdk.common.adapter.Item
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.listener.ShopWidgetAddToCartClickListener

data class ShopAdsWithThreeProductModel(
    var isOfficial: Boolean = false,
    var isPMPro: Boolean = false,
    var isPowerMerchant: Boolean = false,
    var shopBadge: String = "",
    var shopName: String = "",
    var applink: String = "",
    var shopImageUrl: String = "",
    var variant: Int = 9,
    var shopWidgetImageUrl: String = "",
    var merchantVouchers: MutableList<String> = mutableListOf<String>(),
    var listItems: MutableList<Item<*>> = mutableListOf(),
    var items: CpmData = CpmData(),
    val shopApplink: String,
    val adsClickUrl: String,
    val topAdsBannerClickListener: TopAdsBannerClickListener?,
    val hasAddToCartButton: Boolean,
    val impressionListener: TopAdsItemImpressionListener?,
    val shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener?
)
