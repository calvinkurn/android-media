package com.tokopedia.search.result.product.cpm

import com.tokopedia.topads.sdk.domain.model.CpmData

interface BannerAdsListener {
    fun onBannerAdsClicked(
        position: Int,
        applink: String?,
        data: CpmData?,
        dataView: CpmDataView,
        adapterPosition: Int,
    )

    fun onBannerAdsImpressionListener(
        position: Int,
        data: CpmData?,
        dataView: CpmDataView,
        adapterPosition: Int,
    )

    fun onTopAdsCarouselItemImpressionListener(impressionCount: Int)

    fun onBannerAdsImpression1PxListener(adapterPosition: Int, data: CpmDataView)
}
