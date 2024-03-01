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
import com.tokopedia.topads.sdk.domain.model.Product

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
    ) {
        if (applink == null || data == null) return

        redirectionListener?.startActivityWithApplink(applink)
        trackBannerAdsClicked(position, applink, data, dataView)
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

    override fun onBannerAdsProductImpressionListener(
        position: Int,
        product: Product?,
        dataView: CpmDataView,
    ) {
        if (dataView.isTrackByteIO() && product != null) {
            AppLogSearch.eventSearchResultShow(
                dataView.productItemAsByteIOSearchResult(
                    product,
                    position,
                    "",
                )
            )
            AppLogSearch.eventProductShow(
                dataView.productItemAsByteIOProduct(product, position)
            )
        }
    }

    override fun onTopAdsCarouselItemImpressionListener(impressionCount: Int) {
        bannerAdsPresenter.shopAdsImpressionCount(impressionCount)
    }

    private fun trackBannerAdsClicked(
        position: Int,
        applink: String,
        data: CpmData,
        dataView: CpmDataView,
    ) {
        if (applink.contains(SHOP)) {
            TopAdsGtmTracker.eventTopAdsHeadlineShopClick(position, queryKey, data, userId)
            TopAdsGtmTracker.eventSearchResultPromoShopClick(data, position)

            if (dataView.isTrackByteIO())
                AppLogSearch.eventSearchResultClick(
                    dataView.asByteIOSearchResult(CLICK_SHOP_NAME)
                )
        } else {
            TopAdsGtmTracker.eventTopAdsHeadlineProductClick(position, data, userId)
            TopAdsGtmTracker.eventSearchResultPromoProductClick(data, position)

            if (dataView.isTrackByteIO())
                trackByteIOProductClick(data, applink, dataView, position)
        }
    }

    private fun trackByteIOProductClick(
        data: CpmData,
        applink: String,
        dataView: CpmDataView,
        productPosition: Int,
    ) {
        val cpmProduct = data.cpm.cpmShop.products.find { it.applinks == applink } ?: return

        AppLogSearch.eventSearchResultClick(
            dataView.productItemAsByteIOSearchResult(
                cpmProduct,
                productPosition,
                "",
            )
        )
        AppLogSearch.eventProductClick(
            dataView.productItemAsByteIOProduct(cpmProduct, productPosition)
        )
    }

    override fun onBannerAdsImpression1PxListener(
        data: CpmDataView,
        isReimagine: Boolean,
    ) {
        if (data.isTrackByteIO()) {
            AppLogSearch.eventSearchResultShow(
                data.asByteIOSearchResult(null)
            )

            if (!isReimagine && data.isShopBig())
                AppLogSearch.eventSearchResultShow(
                    data.shopItemAsByteIOSearchResult(null)
                )
        }
    }

    companion object {
        private const val SHOP = "shop"
    }
}
