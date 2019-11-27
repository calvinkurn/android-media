package com.tokopedia.home_recom.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by Lukas on 02/09/19
 */
object SimilarProductRecommendationTracking {
    private const val EVENT = "event"
    private const val EVENT_CATEGORY = "eventCategory"
    private const val EVENT_ACTION = "eventAction"
    private const val EVENT_LABEL = "eventLabel"

    private const val EVENT_PRODUCT_VIEW = "productView"
    private const val EVENT_PRODUCT_CLICK = "productClick"
    private const val EVENT_CLICK_RECOMMENDATION = "clickRecommendation"
    private const val EVENT_WISHLIST_RECOMMENDATION = "%s - wishlist on product recommendation"
    private const val EVENT_WISHLIST_RECOMMENDATION_NON_LOGIN = "add - wishlist on product recommendation - non login"
    private const val EVENT_CLICK_BACK_BUTTON = "click - back button"

    private const val EVENT_CATEGORY_SIMILAR_PRODUCT = "similar product recommendation page"
    private const val ACTION_IMPRESSION_PRODUCT_RECOMMENDATION = "impression - product recommendation"
    private const val ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN = "impression - product recommendation - non login"
    private const val ACTION_CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation"
    private const val ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN = "click - product recommendation - non login"

    private const val IMPRESSION = "impressions"
    private const val CLICK = "click"
    private const val ECOMMERCE = "ecommerce"
    private const val CURRENCY_CODE = "currencyCode"

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
    private const val FIELD_ATTRIBUTE = "attribution"
    private const val FIELD_DIMENSION_83 = "dimension83"

    private const val LIST_PRODUCT_RECOMMENDATION = "/similarrecommendation - rekomendasi untuk anda - %s - ref: %s%s"
    private const val LIST_PRODUCT_RECOMMENDATION_NON_LOGIN = "/similarrecommendation - non login - rekomendasi untuk anda - %s - ref: %s%s"

    private const val PRODUCT_TOP_ADS = " - product topads"

    private const val VALUE_NONE_OTHER = "none / other"
    private const val VALUE_IDR = "IDR"
    private const val VALUE_EMPTY = ""
    private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"

    private fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }

    private fun convertRecommendationItemToDataClickObject(item: RecommendationItem,
                                                           list: String,
                                                           position: String): Any {
        return DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                FIELD_PRODUCT_LIST, list
        ),
                FIELD_PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        FIELD_PRODUCT_NAME, item.name,
                        FIELD_PRODUCT_ID, item.productId,
                        FIELD_PRODUCT_PRICE, item.getPriceIntFromString(),
                        FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                        FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                        FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                        FIELD_PRODUCT_LIST, list,
                        FIELD_PRODUCT_POSITION, position,
                        FIELD_ATTRIBUTE, VALUE_EMPTY,
                        FIELD_DIMENSION_83, if(item.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                )
        )
        )
    }

    private fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem,
                                                                list: String,
                                                                position: String): Any {
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.getPriceIntFromString(),
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_LIST, list,
                FIELD_PRODUCT_POSITION, position,
                FIELD_DIMENSION_83, if(item.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
        )
    }

    fun eventImpression(
            trackingQueue: TrackingQueue,
            recommendationItem: RecommendationItem,
            position: String,
            ref: String
    ) {
        trackingQueue.putEETracking(
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_VIEW,
                        EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                        EVENT_ACTION, ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, VALUE_IDR,
                        IMPRESSION, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(
                                recommendationItem,
                                String.format(
                                        LIST_PRODUCT_RECOMMENDATION,
                                        recommendationItem.recommendationType,
                                        ref,
                                        if(recommendationItem.isTopAds) PRODUCT_TOP_ADS else ""
                                ), position)
                )
                )
                ) as HashMap<String, Any>?
        )
    }

    fun eventImpressionNonLogin(
            trackingQueue: TrackingQueue,
            recommendationItem: RecommendationItem,
            position: String,
            ref: String
    ) {
        trackingQueue.putEETracking(
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_VIEW,
                        EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                        EVENT_ACTION, ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, VALUE_IDR,
                        IMPRESSION, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(
                                recommendationItem,
                                String.format(
                                        LIST_PRODUCT_RECOMMENDATION_NON_LOGIN,
                                        recommendationItem.recommendationType,
                                        ref,
                                        if(recommendationItem.isTopAds) PRODUCT_TOP_ADS else ""
                                ), position)
                )
                )
                ) as HashMap<String, Any>?
        )
    }

    fun eventClick(
            recommendationItem: RecommendationItem,
            position: String,
            ref: String
    ) {
        val data =
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_CLICK,
                        EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                        EVENT_ACTION, ACTION_CLICK_PRODUCT_RECOMMENDATION,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                        CLICK, convertRecommendationItemToDataClickObject(
                        recommendationItem,
                        String.format(
                                LIST_PRODUCT_RECOMMENDATION,
                                recommendationItem.recommendationType,
                                ref,
                                if (recommendationItem.isTopAds) PRODUCT_TOP_ADS else ""
                        ),
                        position
                )
                )
                )

        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventClickNonLogin(
            recommendationItem: RecommendationItem,
            position: String,
            ref: String
    ) {
        val data =
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_CLICK,
                        EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                        EVENT_ACTION, ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                            CLICK, convertRecommendationItemToDataClickObject(
                                recommendationItem,
                                String.format(
                                        LIST_PRODUCT_RECOMMENDATION_NON_LOGIN,
                                        recommendationItem.recommendationType,
                                        ref,
                                        if(recommendationItem.isTopAds) PRODUCT_TOP_ADS else ""
                                ),
                                position
                            )
                        )
                )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventClickWishlist(isAddWishlist: Boolean){
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, String.format(EVENT_WISHLIST_RECOMMENDATION, if(isAddWishlist) "add" else "remove"),
                EVENT_LABEL, VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventClickWishlistNonLogin(){
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, EVENT_WISHLIST_RECOMMENDATION_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventClickBackButton() {
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_SIMILAR_PRODUCT,
                EVENT_ACTION, EVENT_CLICK_BACK_BUTTON,
                EVENT_LABEL, VALUE_EMPTY
        )
        getTracker().sendEnhanceEcommerceEvent(data)
    }
}