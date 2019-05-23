package com.tokopedia.home_recom.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

class RecommendationPageTracking {

    val EVENT = "event"
    val EVENT_CATEGORY = "eventCategory"
    val EVENT_ACTION = "eventAction"
    val EVENT_LABEL = "eventLabel"

    val ECOMMERCE = "ecommerce"
    val ECOMMERCE_IMPRESSIONS = "impressions"
    val ECOMMERCE_CURRENCY_CODE = "currencyCode"

    val FIELD_PRODUCT_NAME = "name"
    val FIELD_PRODUCT_ID = "id"
    val FIELD_PRODUCT_PRICE = "price"
    val FIELD_PRODUCT_BRAND = "brand"
    val FIELD_PRODUCT_VARIANT = "variant"
    val FIELD_PRODUCT_CATEGORY = "category"
    val FIELD_PRODUCT_LIST = "list"
    val FIELD_PRODUCT_POSITION = "position"

    val VALUE_NONE_OTHER = "none / other"
    val VALUE_IDR = "IDR"
    val VALUE_EMPTY = ""
    val VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION = "/product - rekomendasi untuk anda - %s"
    val VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION = "/product - rekomendasi untuk anda - %s - product topads"

    val EVENT_PRODUCT_VIEW = "productView"
    val EVENT_CATEGORY_PRODUCT_DETAIL_PAGE = "product detail page"
    val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN = "impression - product recommendation"

    fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }

    fun convertRecommendationItemToDataObject(item: RecommendationItem,
                                              list: String,
                                              position: String) {
        DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.priceInt,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_LIST, list,
                FIELD_PRODUCT_POSITION, position
        )
    }

    fun eventImpressionOnOrganicProductRecommendationForLoginUser(
            recommendationItem: RecommendationItem,
            tabName: String) {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                        convertRecommendationItemToDataObject(recommendationItem,
                                VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION)
                    )
                )
        )

        tracker.sendEnhanceEcommerceEvent(data)
    }
}
