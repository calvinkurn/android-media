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
    val ECOMMERCE_ADD = "add"

    val ACTION_FIELD = "actionField"

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
    val VALUE_LIST_CLICK_ADD_TO_CART = "/recommendation - primary product"

    val EVENT_PRODUCT_VIEW = "productView"
    val EVENT_CLICK_RECOMMENDATION = "clickRecommendation"
    val EVENT_ADD_TO_CART = "addToCart"
    val EVENT_PRODUCT_CLICK = "productClick"
    val EVENT_CATEGORY_PRODUCT_DETAIL_PAGE = "product detail page"
    val EVENT_RECOMMENDATION_PAGE = "recommendation page"
    val EVENT_ACTION_ADD_TO_CART = "click add to cart"
    val EVENT_ACTION_CLICK_BACK = "click back"
    val EVENT_ACTION_CLICK_SEE_CART = "click see cart"
    val EVENT_CLICK_PRODUCT_ON_HEADER = "click product recommendation on %s - non login"
    val EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER = "impression product recommendation on %s - non login"
    val EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER_LOGIN = "impression product recommendation on %s"
    val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN = "impression - product recommendation"
    val EVENT_ACTION_IMPRESSION_PRODUCT_WISHLIST = "click %s wishlist on primary product"
    val EVENT_ACTION_IMPRESSION_PRODUCT_WISHLIST_NON_LOGIN = "click add wishlist on primary product - non login"

    val ACTION_FIELD_LIST = "list"
    val ACTION_FIELD_PRODUCTS = "products"
    val ACTION_FIELD_LIST_CLICK_HEADER = "/recommendation - non login - %s - rekomendasi untuk anda - %s"
    val ACTION_FIELD_LIST_CLICK_HEADER_TOP_ADS = "/recommendation - non login - %s - rekomendasi untuk anda - %s - product topads"

    val CLICK = "click"


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

    fun convertRecommendationItemToDataObject(item: RecommendationItem,
                                              list: String) {
        DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.priceInt,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_LIST, list
        )
    }

    fun convertRecommendationItemToDataObject(item: RecommendationItem) {
        DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.priceInt,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs
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

    fun eventImpressionUserClickProductToWishlistForUserLogin(
            isAdded: Boolean
    ){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_WISHLIST,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventImpressionUserClickProductToWishlistForNonLogin(){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_WISHLIST_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }


    fun eventUserClickAddToCart(
            recommendationItem: RecommendationItem
    ){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_ADD_TO_CART,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_ADD_TO_CART,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_ADD, DataLayer.listOf(
                                ACTION_FIELD, DataLayer.listOf(
                                  ACTION_FIELD_LIST, VALUE_LIST_CLICK_ADD_TO_CART
                                ),
                                ACTION_FIELD_PRODUCTS, listOf(convertRecommendationItemToDataObject(recommendationItem))
                        )
                )
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventUserClickRecommendation(){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, VALUE_EMPTY,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventUserClickOnHeaderNameProduct(
            headerName: String,
            recommendationItem: RecommendationItem,
            position: Int
    ){

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, String.format(EVENT_CLICK_PRODUCT_ON_HEADER, headerName),
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(
                            ACTION_FIELD_LIST, String.format
                                (if(recommendationItem.isTopAds)
                                    ACTION_FIELD_LIST_CLICK_HEADER_TOP_ADS
                                else ACTION_FIELD_LIST_CLICK_HEADER,
                                        headerName, recommendationItem.recommendationType)
                        ),
                        ACTION_FIELD_PRODUCTS, listOf(convertRecommendationItemToDataObject(recommendationItem, position.toString())
                        )
                    )
                )
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventImpressionProductRecommendationOnHeaderNameLogin(
            headerName: String,
            recommendationItem: RecommendationItem,
            position: Int
    ){

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, String.format(EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER_LOGIN, headerName),
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, listOf(convertRecommendationItemToDataObject(recommendationItem,
                            VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION, position.toString())
                    )
                )
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventImpressionProductRecommendationOnHeaderName(
            recommendationItem: RecommendationItem,
            position: Int
    ){

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, listOf(convertRecommendationItemToDataObject(recommendationItem,
                            VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION, position.toString())
                    )
                )
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventImpressionUserClickBack(){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_BACK,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventImpressionUserClickSeeToCart(){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_SEE_CART,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }
}
