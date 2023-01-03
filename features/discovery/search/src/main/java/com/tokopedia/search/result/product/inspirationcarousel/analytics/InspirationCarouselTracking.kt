package com.tokopedia.search.result.product.inspirationcarousel.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.analytics.SearchTrackingConstant
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option.Product
import com.tokopedia.search.result.product.inspirationlistatc.analytics.InspirationListAtcTracking
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object InspirationCarouselTracking {

    private const val IMPRESSION_CAROUSEL_PRODUCT = "impression - carousel product"
    private const val CAROUSEL_UNIFICATION_LIST_NAME = " /search - carousel %s - component:%s"
    private const val IMPRESSION_INSPIRATION_CAROUSEL_INFO_PRODUCT = "impression - carousel banner"
    private const val CLICK_INSPIRATION_CAROUSEL_GRID_BANNER = "click lihat sekarang - carousel banner"

    data class Data(
        val keyword: String,
        val product: Product,
        val filterSortParams: String,
        val cartId: String,
        val quantity: Int,
    ) {
        val eventLabel: String
            get() = "$keyword - ${product.inspirationCarouselTitle} - ${product.optionTitle}"

        val productDataLayer: Any
            get() = product.asUnificationObjectDataLayer(filterSortParams)

        val productAtcDataLayer: Any
            get() = product.asUnificationAtcObjectDataLayer(filterSortParams, cartId, quantity)
    }

    fun getInspirationCarouselUnificationListName(type: String, componentId: String): String =
        CAROUSEL_UNIFICATION_LIST_NAME.format(type, componentId)

    fun trackCarouselImpression(
        trackingQueue: TrackingQueue,
        data: Data,
    ) {
        val impressionDataLayer = DataLayer.mapOf(
            SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
            SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
            SearchTrackingConstant.EVENT_ACTION, IMPRESSION_CAROUSEL_PRODUCT,
            SearchTrackingConstant.EVENT_LABEL, data.eventLabel,
            SearchTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                SearchEventTracking.ECommerce.CURRENCY_CODE, SearchEventTracking.ECommerce.IDR,
                SearchEventTracking.ECommerce.IMPRESSIONS, arrayListOf(data.productDataLayer)
            )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(impressionDataLayer)
    }

    fun trackCarouselClick(data: Data) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_CAROUSEL_PRODUCT,
                SearchTrackingConstant.EVENT_LABEL, data.eventLabel,
                SearchTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                    SearchEventTracking.ECommerce.CLICK, DataLayer.mapOf(
                        SearchEventTracking.ECommerce.ACTION_FIELD, DataLayer.mapOf(
                            SearchEventTracking.ECommerce.LIST, getInspirationCarouselUnificationListName(
                                data.product.inspirationCarouselType,
                                data.product.componentId,
                            )
                        ),
                        SearchEventTracking.ECommerce.PRODUCTS, arrayListOf(data.productDataLayer)
                    )
                )
            )
        )
    }

    fun trackCarouselClickSeeAll(
        keyword: String,
        option: Option,
    ) {
        val searchComponentTracking = option.asSearchComponentTracking(keyword)

        searchComponentTracking.click(TrackApp.getInstance().gtm)
    }

    fun trackCarouselClickAtc(data: Data) {
        InspirationListAtcTracking.trackEventClickAddToCartInspirationCarouselUnification(
            data.eventLabel,
            arrayListOf(data.productAtcDataLayer),
        )
    }

    private fun Option.asSearchComponentTracking(keyword: String): SearchComponentTracking =
        searchComponentTracking(
            trackingOption = trackingOption,
            keyword = keyword,
            valueId = "0",
            valueName = "$carouselTitle - $title",
            componentId = componentId,
            applink = applink,
            dimension90 = dimension90
        )

    fun trackImpressionInspirationCarouselInfo(
        trackingQueue: TrackingQueue,
        type: String,
        keyword: String,
        list: ArrayList<Any>,
    ) {
        val map = DataLayer.mapOf(
            SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_VIEW,
            SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
            SearchTrackingConstant.EVENT_ACTION, IMPRESSION_INSPIRATION_CAROUSEL_INFO_PRODUCT,
            SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
            SearchTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                SearchEventTracking.Event.PROMO_VIEW, DataLayer.mapOf(
                    SearchEventTracking.ECommerce.PROMOTIONS, list
                )
            )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    fun trackEventClickInspirationCarouselInfoProduct(type: String,
                                                      keyword: String,
                                                      products: ArrayList<Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK,
                SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                SearchTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                    SearchEventTracking.Event.PROMO_CLICK, DataLayer.mapOf(
                        SearchEventTracking.ECommerce.PROMOTIONS, products
                    )
                )
            )
        )
    }

    fun trackEventClickInspirationCarouselGridBanner(
        type: String, keyword: String, bannerData: Any?, userId: String?
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, CLICK_INSPIRATION_CAROUSEL_GRID_BANNER,
                SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                SearchTrackingConstant.USER_ID, userId,
                SearchTrackingConstant.ECOMMERCE, DataLayer.mapOf(
                    SearchEventTracking.Event.PROMO_CLICK, DataLayer.mapOf(
                        SearchEventTracking.ECommerce.PROMOTIONS, DataLayer.listOf(bannerData)
                    )
                )
            )
        )
    }
}
