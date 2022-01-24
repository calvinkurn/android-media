package com.tokopedia.search.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

object RecommendationTracking {
    private const val EVENT = "event"
    private const val EVENT_CATEGORY = "eventCategory"
    private const val EVENT_ACTION = "eventAction"
    private const val EVENT_LABEL = "eventLabel"

    private const val ECOMMERCE = "ecommerce"
    private const val ECOMMERCE_CLICK = "click"
    private const val ECOMMERCE_IMPRESSIONS = "impressions"
    private const val ECOMMERCE_CURRENCY_CODE = "currencyCode"

    private const val PRODUCT_TOPADS = " - product topads"

    private const val FIELD_PRODUCTS = "products"
    private const val FIELD_PRODUCT_NAME = "name"
    private const val FIELD_PRODUCT_ID = "id"
    private const val FIELD_PRODUCT_PRICE = "price"
    private const val FIELD_PRODUCT_BRAND = "brand"
    private const val FIELD_PRODUCT_VARIANT = "variant"
    private const val FIELD_PRODUCT_CATEGORY = "category"
    private const val FIELD_PRODUCT_LIST = "list"
    private const val FIELD_PRODUCT_POSITION = "position"
    private const val FIELD_ACTION_FIELD = "actionField"


    private const val VALUE_NONE_OTHER = "none / other"
    private const val VALUE_IDR = "IDR"
    private const val VALUE_EMPTY = ""
    private const val VALUE_LIST_RECOMMENDATION_PRODUCT =
        "/searchproduct - rekomendasi untuk anda - empty_search - %s %s"
    private const val VALUE_LIST_RECOMMENDATION_PRODUCT_NON_LOGIN =
        "/searchproduct - non login - rekomendasi untuk anda - empty_search - %s %s"

    private const val EVENT_PRODUCT_VIEW = "productView"
    private const val EVENT_PRODUCT_CLICK = "productClick"
    private const val EVENT_SEARCH_RESULT = "clickSearchResult"


    private const val EVENT_CATEGORY_SEARCH_PAGE = "search result"

    private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN =
        "click - product recommendation"
    private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN =
        "click - product recommendation - non login"
    private const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN =
        "impression - product recommendation"
    private const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN =
        "impression - product recommendation - non login"

    private const val EVENT_ACTION_CLICK_WISHLIST_NON_LOGIN =
        "add - wishlist on product recommendation - non login"
    private const val EVENT_ACTION_CLICK_SEE_MORE = "click - see more on widget %s"
    private const val EVENT_ACTION_CLICK_WISHLIST_LOGIN = "%s - wishlist on product recommendation"


    private fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }


    private fun convertRecommendationItemToDataImpressionObject(
        item: RecommendationItem,
        list: String,
        position: String
    ): Any {
        return DataLayer.mapOf(
            FIELD_PRODUCT_NAME, item.name,
            FIELD_PRODUCT_ID, item.productId,
            FIELD_PRODUCT_PRICE, item.price,
            FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
            FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
            FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
            FIELD_PRODUCT_LIST, list,
            FIELD_PRODUCT_POSITION, position
        )
    }

    private fun convertRecommendationItemToDataClickObject(
        item: RecommendationItem,
        list: String,
        position: String
    ): Map<String, Any> {
        return DataLayer.mapOf(
            FIELD_ACTION_FIELD, DataLayer.mapOf(
                FIELD_PRODUCT_LIST, list
            ),
            FIELD_PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                    FIELD_PRODUCT_NAME, item.name,
                    FIELD_PRODUCT_ID, item.productId,
                    FIELD_PRODUCT_PRICE, item.price,
                    FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                    FIELD_PRODUCT_POSITION, position
                )
            )
        )
    }

    fun eventImpressionProductRecommendationLogin(
        trackingQueue: TrackingQueue,
        recommendationItem: RecommendationItem,
        position: String
    ) {
        val data = DataLayer.mapOf(
            EVENT, EVENT_PRODUCT_VIEW,
            EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_PAGE,
            EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
            EVENT_LABEL, VALUE_EMPTY,
            ECOMMERCE, DataLayer.mapOf(
                ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                    convertRecommendationItemToDataImpressionObject(
                        recommendationItem,
                        String.format(
                            VALUE_LIST_RECOMMENDATION_PRODUCT,
                            recommendationItem.recommendationType,
                            if (recommendationItem.isTopAds) PRODUCT_TOPADS else VALUE_EMPTY
                        ),
                        position
                    )
                )
            )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventImpressionProductRecommendationNonLogin(
        trackingQueue: TrackingQueue,
        recommendationItem: RecommendationItem,
        position: String
    ) {
        val data = DataLayer.mapOf(
            EVENT, EVENT_PRODUCT_VIEW,
            EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_PAGE,
            EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN,
            EVENT_LABEL, VALUE_EMPTY,
            ECOMMERCE, DataLayer.mapOf(
                ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                    convertRecommendationItemToDataImpressionObject(
                        recommendationItem,
                        String.format(
                            VALUE_LIST_RECOMMENDATION_PRODUCT_NON_LOGIN,
                            recommendationItem.recommendationType,
                            if (recommendationItem.isTopAds) PRODUCT_TOPADS else VALUE_EMPTY
                        ),
                        position
                    )
                )
            )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventClickProductRecommendationLogin(
        recommendationItem: RecommendationItem,
        position: String
    ) {
        val data = DataLayer.mapOf(
            EVENT, EVENT_PRODUCT_CLICK,
            EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_PAGE,
            EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN,
            EVENT_LABEL, VALUE_EMPTY,
            ECOMMERCE, DataLayer.mapOf(
                ECOMMERCE_CLICK,
                convertRecommendationItemToDataClickObject(
                    recommendationItem,
                    String.format(
                        VALUE_LIST_RECOMMENDATION_PRODUCT,
                        recommendationItem.recommendationType,
                        if (recommendationItem.isTopAds) PRODUCT_TOPADS else VALUE_EMPTY
                    ),
                    position
                )

            )
        )
        getTracker().sendEnhanceEcommerceEvent(data as HashMap<String, Any>)
    }

    fun eventClickProductRecommendationNonLogin(
        recommendationItem: RecommendationItem,
        position: String
    ) {
        val data = DataLayer.mapOf(
            EVENT, EVENT_PRODUCT_CLICK,
            EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_PAGE,
            EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN,
            EVENT_LABEL, VALUE_EMPTY,
            ECOMMERCE, DataLayer.mapOf(
                ECOMMERCE_CLICK,
                convertRecommendationItemToDataClickObject(
                    recommendationItem,
                    String.format(
                        VALUE_LIST_RECOMMENDATION_PRODUCT_NON_LOGIN,
                        recommendationItem.recommendationType,
                        if (recommendationItem.isTopAds) PRODUCT_TOPADS else VALUE_EMPTY
                    ),
                    position
                )

            )
        )
        getTracker().sendEnhanceEcommerceEvent(data as HashMap<String, Any>)
    }

    fun eventUserClickProductToWishlistForUserLogin(
        isAdded: Boolean
    ) {
        val data = DataLayer.mapOf(
            EVENT,
            EVENT_SEARCH_RESULT,
            EVENT_CATEGORY,
            EVENT_CATEGORY_SEARCH_PAGE,
            EVENT_ACTION,
            String.format(EVENT_ACTION_CLICK_WISHLIST_LOGIN, if (isAdded) "add" else "remove"),
            EVENT_LABEL,
            VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventUserClickProductToWishlistForNonLogin() {
        val data = DataLayer.mapOf(
            EVENT, EVENT_SEARCH_RESULT,
            EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_PAGE,
            EVENT_ACTION, EVENT_ACTION_CLICK_WISHLIST_NON_LOGIN,
            EVENT_LABEL, VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventUserClickSeeMore(pageName: String) {
        val data = DataLayer.mapOf(
            EVENT, EVENT_SEARCH_RESULT,
            EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_PAGE,
            EVENT_ACTION, String.format(EVENT_ACTION_CLICK_SEE_MORE, pageName),
            EVENT_LABEL, VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

}