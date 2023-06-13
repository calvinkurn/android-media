package com.tokopedia.search.result.product.cpm

import android.content.Context
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.CpmData

class BannerAdsListenerDelegate(
    queryKeyProvider: QueryKeyProvider,
    context: Context?,
    private val redirectionListener: RedirectionListener?,
    private val bannerAdsPresenter: BannerAdsPresenter,
    private val userId: String,
): BannerAdsListener,
    QueryKeyProvider by queryKeyProvider,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
        if (applink == null || data == null) return

        redirectionListener?.startActivityWithApplink(applink)
        trackBannerAdsClicked(position, applink, data)
    }

    override fun onBannerAdsImpressionListener(position: Int, data: CpmData?) {
        if (data == null) return

        TopAdsGtmTracker.eventTopAdsHeadlineShopView(position, data, queryKey, userId)
    }

    override fun onTopAdsCarouselItemImpressionListener(impressionCount: Int) {
        bannerAdsPresenter.shopAdsImpressionCount(impressionCount)
    }

    private fun trackBannerAdsClicked(position: Int, applink: String, data: CpmData) {
        if (applink.contains(SHOP)) {
            TopAdsGtmTracker.eventTopAdsHeadlineShopClick(position, queryKey, data, userId)
            TopAdsGtmTracker.eventSearchResultPromoShopClick(data, position)
        } else {
            TopAdsGtmTracker.eventTopAdsHeadlineProductClick(position, data, userId)
            TopAdsGtmTracker.eventSearchResultPromoProductClick(data, position)
        }
    }

    companion object {
        private const val SHOP = "shop"
    }
}