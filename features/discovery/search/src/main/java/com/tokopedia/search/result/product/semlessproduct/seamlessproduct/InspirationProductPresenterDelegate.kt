package com.tokopedia.search.result.product.semlessproduct.seamlessproduct

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import javax.inject.Inject

@SearchScope
class InspirationProductPresenterDelegate @Inject constructor(
    private val broadMatchView: InspirationProductView,
    private val inspirationProductTracker: InspirationProductItemTracker,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val classNameProvider: ClassNameProvider
) : InspirationProductPresenter {
    override fun onInspirationProductItemImpressed(broadMatchItemDataView: InspirationProductItemDataView) {
        if (broadMatchItemDataView.isOrganicAds)
            sendTrackingImpressBroadMatchAds(broadMatchItemDataView)

        val seamlessInspirationProductType = broadMatchItemDataView.seamlessInspirationProductType
        inspirationProductTracker.trackInspirationProductSeamlessImpression(
            seamlessInspirationProductType.type,
            seamlessInspirationProductType.inspirationCarouselProduct
        )
    }

    override fun onInspirationProductItemClick(broadMatchItemDataView: InspirationProductItemDataView) {
        val seamlessInspirationProductType = broadMatchItemDataView.seamlessInspirationProductType
        inspirationProductTracker.trackInspirationProductSeamlessClick(
            seamlessInspirationProductType.type,
            seamlessInspirationProductType.inspirationCarouselProduct
        )

        broadMatchView.openLink(broadMatchItemDataView.applink, broadMatchItemDataView.url)

        if (broadMatchItemDataView.isOrganicAds)
            sendTrackingClickBroadMatchAds(broadMatchItemDataView)
    }

    private fun sendTrackingImpressBroadMatchAds(broadMatchItemDataView: InspirationProductItemDataView) {
        topAdsUrlHitter.hitImpressionUrl(
            classNameProvider.className,
            broadMatchItemDataView.topAdsViewUrl,
            broadMatchItemDataView.id,
            broadMatchItemDataView.name,
            broadMatchItemDataView.imageUrl,
            SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
        )
    }

    private fun sendTrackingClickBroadMatchAds(broadMatchItemDataView: InspirationProductItemDataView) {
        topAdsUrlHitter.hitClickUrl(
            classNameProvider.className,
            broadMatchItemDataView.topAdsClickUrl,
            broadMatchItemDataView.id,
            broadMatchItemDataView.name,
            broadMatchItemDataView.imageUrl,
            SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
        )
    }
}
