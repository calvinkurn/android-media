package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import javax.inject.Inject

class InspirationCarouselPresenter @Inject constructor(
    private val topAdsUrlHitter: TopAdsUrlHitter,
) : BaseDaggerPresenter<InspirationCarouselContract.View>(),
    InspirationCarouselContract.Presenter {
    override fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product) {
        if (isViewNotAttached) return

        if(product.isOrganicAds) sendTrackingImpressInspirationCarouselAds(product)

        when(product.layout) {
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID ->
                view.trackEventImpressionInspirationCarouselGridItem(product)
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS ->
                view.trackEventImpressionInspirationCarouselChipsItem(product)
            else -> view.trackEventImpressionInspirationCarouselListItem(product)
        }
    }

    private fun sendTrackingImpressInspirationCarouselAds(product: InspirationCarouselDataView.Option.Product) {
        topAdsUrlHitter.hitImpressionUrl(
            view.className,
            product.topAdsViewUrl,
            product.id,
            product.name,
            product.imgUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }

    override fun onInspirationCarouselProductClick(product: InspirationCarouselDataView.Option.Product) {
        if (isViewNotAttached) return

        view.redirectionStartActivity(product.applink, product.url)

        when(product.layout) {
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_GRID ->
                view.trackEventClickInspirationCarouselGridItem(product)
            SearchConstant.InspirationCarousel.LAYOUT_INSPIRATION_CAROUSEL_CHIPS ->
                view.trackEventClickInspirationCarouselChipsItem(product)
            else -> view.trackEventClickInspirationCarouselListItem(product)
        }

        if(product.isOrganicAds) sendTrackingClickInspirationCarouselAds(product)
    }

    private fun sendTrackingClickInspirationCarouselAds(product: InspirationCarouselDataView.Option.Product) {
        topAdsUrlHitter.hitClickUrl(
            view.className,
            product.topAdsClickUrl,
            product.id,
            product.name,
            product.imgUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }
}