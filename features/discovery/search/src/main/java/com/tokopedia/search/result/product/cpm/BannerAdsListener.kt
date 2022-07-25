package com.tokopedia.search.result.product.cpm

import com.tokopedia.topads.sdk.domain.model.CpmData

interface BannerAdsListener {
    val userId: String

    fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?)

    fun onBannerAdsImpressionListener(position: Int, data: CpmData?)

    fun onTopAdsCarouselItemImpressionListener(impressionCount: Int) {}
}