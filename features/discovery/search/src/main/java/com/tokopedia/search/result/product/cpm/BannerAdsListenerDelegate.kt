package com.tokopedia.search.result.product.cpm

import android.content.Context
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.CLICK_SHOP_NAME
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

    override fun onBannerAdsClicked(
        position: Int,
        applink: String?,
        data: CpmData?,
        dataView: CpmDataView,
        adapterPosition: Int,
    ) {
        if (applink == null || data == null) return

        redirectionListener?.startActivityWithApplink(applink)
        trackBannerAdsClicked(position, applink, data, dataView, adapterPosition)
    }

    override fun onBannerAdsImpressionListener(
        position: Int,
        data: CpmData?,
        dataView: CpmDataView,
        adapterPosition: Int,
    ) {
        if (data == null) return

        TopAdsGtmTracker.eventTopAdsHeadlineShopView(position, data, queryKey, userId)
    }

    override fun onTopAdsCarouselItemImpressionListener(impressionCount: Int) {
        bannerAdsPresenter.shopAdsImpressionCount(impressionCount)
    }

    private fun trackBannerAdsClicked(
        position: Int,
        applink: String,
        data: CpmData,
        dataView: CpmDataView,
        adapterPosition: Int,
    ) {
        if (applink.contains(SHOP)) {
            TopAdsGtmTracker.eventTopAdsHeadlineShopClick(position, queryKey, data, userId)
            TopAdsGtmTracker.eventSearchResultPromoShopClick(data, position)
            AppLogSearch.eventSearchResultClick(dataView.asByteIOSearchResult(adapterPosition, CLICK_SHOP_NAME))
        } else {
            TopAdsGtmTracker.eventTopAdsHeadlineProductClick(position, data, userId)
            TopAdsGtmTracker.eventSearchResultPromoProductClick(data, position)
            trackByteIOProductClick(data, applink, dataView, adapterPosition, position)
        }
    }

    private fun trackByteIOProductClick(
        data: CpmData,
        applink: String,
        dataView: CpmDataView,
        adapterPosition: Int,
        productPosition: Int,
    ) {
        val cpmProduct = data.cpm.cpmShop.products.find { it.applinks == applink } ?: return

        AppLogSearch.eventSearchResultClick(
            dataView.productAsByteIOSearchResult(
                cpmProduct,
                adapterPosition,
                productPosition,
                "",
            )
        )
        AppLogSearch.eventProductClick(
            dataView.productAsByteIOProduct(cpmProduct, adapterPosition, productPosition)
        )
    }

    override fun onBannerAdsImpression1PxListener(adapterPosition: Int, data: CpmDataView) {
        AppLogSearch.eventSearchResultShow(data.asByteIOSearchResult(adapterPosition, null))
    }

    companion object {
        private const val SHOP = "shop"
    }
}
