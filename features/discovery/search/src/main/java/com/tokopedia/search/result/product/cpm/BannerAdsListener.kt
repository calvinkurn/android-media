package com.tokopedia.search.result.product.cpm

import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product

interface BannerAdsListener {
    fun onBannerAdsClicked(
        position: Int,
        applink: String?,
        data: CpmData?,
        dataView: CpmDataView,
    )

    fun onBannerAdsImpressionListener(
        position: Int,
        data: CpmData?,
        dataView: CpmDataView,
        adapterPosition: Int,
    )

    fun onBannerAdsProductImpressionListener(
        position: Int,
        product: Product?,
        dataView: CpmDataView,
    )

    fun onTopAdsCarouselItemImpressionListener(impressionCount: Int)

    fun onBannerAdsImpression1PxListener(
        data: CpmDataView,
        isReimagine: Boolean,
    )
}
