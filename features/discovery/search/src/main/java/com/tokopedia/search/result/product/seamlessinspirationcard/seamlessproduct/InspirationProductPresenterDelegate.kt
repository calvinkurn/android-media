package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import javax.inject.Inject

@SearchScope
class InspirationProductPresenterDelegate @Inject constructor(
    private val inspirationProductView: InspirationProductView,
    private val inspirationProductTracker: InspirationProductItemTracker,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val classNameProvider: ClassNameProvider
) : InspirationProductPresenter {
    override fun onInspirationProductItemImpressed(inspirationProductData: InspirationProductItemDataView) {
        if (inspirationProductData.isOrganicAds)
            sendTrackingImpressAsBroadMatchAds(inspirationProductData)

        val seamlessInspirationProductType = inspirationProductData.seamlessInspirationProductType
        inspirationProductTracker.trackInspirationProductSeamlessImpression(
            seamlessInspirationProductType.type,
            seamlessInspirationProductType.inspirationCarouselProduct
        )
    }

    override fun onInspirationProductItemClick(inspirationProductData: InspirationProductItemDataView) {
        val seamlessInspirationProductType = inspirationProductData.seamlessInspirationProductType
        inspirationProductTracker.trackInspirationProductSeamlessClick(
            seamlessInspirationProductType.type,
            seamlessInspirationProductType.inspirationCarouselProduct
        )

        inspirationProductView.openLink(inspirationProductData.applink, inspirationProductData.url)

        if (inspirationProductData.isOrganicAds)
            sendTrackingClickAsBroadMatchAds(inspirationProductData)
    }

    private fun sendTrackingImpressAsBroadMatchAds(inspirationProductItemDataView: InspirationProductItemDataView) {
        topAdsUrlHitter.hitImpressionUrl(
            classNameProvider.className,
            inspirationProductItemDataView.topAdsViewUrl,
            inspirationProductItemDataView.id,
            inspirationProductItemDataView.name,
            inspirationProductItemDataView.imageUrl,
            SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
        )
    }

    private fun sendTrackingClickAsBroadMatchAds(broadMatchItemDataView: InspirationProductItemDataView) {
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
