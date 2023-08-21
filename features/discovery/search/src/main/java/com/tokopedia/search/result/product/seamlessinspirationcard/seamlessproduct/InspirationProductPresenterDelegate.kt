package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import javax.inject.Inject

@SearchScope
class InspirationProductPresenterDelegate @Inject constructor(
    private val inspirationProductTracker: InspirationProductView,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val classNameProvider: ClassNameProvider
) : InspirationProductPresenter {
    override fun onInspirationProductItemImpressed(inspirationProductData: InspirationProductItemDataView) {
        if (inspirationProductData.isOrganicAds)
            sendTrackingImpressInspirationCarouselAds(inspirationProductData)

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

        inspirationProductTracker.openLink(inspirationProductData.applink, inspirationProductData.url)

        if (inspirationProductData.isOrganicAds)
            sendTrackingClickInspirationCarouselAds(inspirationProductData)
    }

    private fun sendTrackingImpressInspirationCarouselAds(product: InspirationProductItemDataView) {
        topAdsUrlHitter.hitImpressionUrl(
            classNameProvider.className,
            product.topAdsViewUrl,
            product.id,
            product.name,
            product.imageUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }

    private fun sendTrackingClickInspirationCarouselAds(product: InspirationProductItemDataView) {
        topAdsUrlHitter.hitClickUrl(
            classNameProvider.className,
            product.topAdsClickUrl,
            product.id,
            product.name,
            product.imageUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }
}
