package com.tokopedia.home_recom.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationPageTracking {

    val EVENT = "event"
    val EVENT_CATEGORY = "eventCategory"
    val EVENT_ACTION = "eventAction"
    val EVENT_LABEL = "eventLabel"

    val ECOMMERCE = "ecommerce"
    val ECOMMERCE_ADD = "add"
    val ECOMMERCE_CLICK = "click"
    val ECOMMERCE_IMPRESSIONS = "impressions"
    val ECOMMERCE_CURRENCY_CODE = "currencyCode"

    val FIELD_PRODUCTS = "products"
    val FIELD_PRODUCT_NAME = "name"
    val FIELD_PRODUCT_ID = "id"
    val FIELD_PRODUCT_PRICE = "price"
    val FIELD_PRODUCT_BRAND = "brand"
    val FIELD_PRODUCT_VARIANT = "variant"
    val FIELD_PRODUCT_CATEGORY = "category"
    val FIELD_PRODUCT_LIST = "list"
    val FIELD_PRODUCT_POSITION = "position"
    val FIELD_ACTION_FIELD = "actionField"
    val FIELD_CATEGORY_ID = "category_id"
    val FIELD_SHOP_ID = "shop_id"
    val FIELD_SHOP_TYPE = "shop_type"
    val FIELD_SHOP_NAME = "shop_name"
    val FIELD_DIMENSION_42 = "dimension42"

    val VALUE_NONE_OTHER = "none / other"
    val VALUE_IDR = "IDR"
    val VALUE_EMPTY = ""
    val VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_LOGIN = "/product - rekomendasi untuk anda - %s"
    val VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_LOGIN = "/product - rekomendasi untuk anda - %s - product topads"
    val VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_NON_LOGIN = "/product - non login - rekomendasi untuk anda - %s"
    val VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_NON_LOGIN = "/product - non login - rekomendasi untuk anda - %s - product topads"
    val VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION = "/recommendation - primary product"
    val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK = "/recommendation - %s - rekomendasi untuk anda - %s"
    val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS = "/recommendation - %s - rekomendasi untuk anda - %s - product topads"
    val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_NON_LOGIN = "/recommendation - non login - %s - rekomendasi untuk anda - %s"
    val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_NON_LOGIN = "/recommendation - non login - %s - rekomendasi untuk anda - %s - product topads"

    val EVENT_PRODUCT_VIEW = "productView"
    val EVENT_PRODUCT_CLICK = "productClick"
    val EVENT_CLICK_PDP = "clickPDP"
    val EVENT_ADD_TO_CART = "addToCart"
    val EVENT_CLICK_RECOMMENDATION = "clickRecommendation"

    val EVENT_CATEGORY_PRODUCT_DETAIL_PAGE = "product detail page"
    val EVENT_CATEGORY_RECOMMENDATION_PAGE = "recommendation page"

    val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN = "click - product recommendation"
    val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN = "click - product recommendation - non login"
    val EVENT_CLICK_PRODUCT_ON_HEADER_LOGIN = "click product recommendation on %s"
    val EVENT_CLICK_PRODUCT_ON_HEADER_NON_LOGIN = "click product recommendation on %s - non login"
    val EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER = "impression product recommendation on %s - non login"
    val EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER_LOGIN = "impression product recommendation on %s"

    val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN = "impression - product recommendation"
    val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN = "impression - product recommendation - non login"
    val EVENT_ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_LOGIN = "add - wishlist on product recommendation"
    val EVENT_ACTION_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION_LOGIN = "remove - wishlist on product recommendation"
    val EVENT_ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_NON_LOGIN = "add - wishlist on product recommendation - non login"
    val EVENT_ACTION_IMPRESSION_PRODUCT_WISHLIST = "click %s wishlist on primary product"
    val EVENT_ACTION_IMPRESSION_PRODUCT_WISHLIST_NON_LOGIN = "click add wishlist on primary product - non login"
    val EVENT_ACTION_CLICK_ICON_SHARE = "click icon share"
    val EVENT_ACTION_ADD_TO_CART = "click add to cart"
    val EVENT_ACTION_CLICK_BACK = "click back"
    val EVENT_ACTION_CLICK_SEE_CART = "click see cart"
    val EVENT_ACTION_CLICK_PRIMARY_PRODUCT = "click primary product"
    val EVENT_ACTION_IMPRESSION_PRIMARY_PRODUCT = "impression primary product"
    val ACTION_FIELD_LIST_CLICK_HEADER = "/recommendation - non login - %s - rekomendasi untuk anda - %s"
    val ACTION_FIELD_LIST_CLICK_HEADER_TOP_ADS = "/recommendation - non login - %s - rekomendasi untuk anda - %s - product topads"

    fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }

    fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem,
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

    fun convertRecommendationItemToDataClickObject(item: RecommendationItem,
                                                   list: String,
                                                   position: String) {
        DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                    FIELD_PRODUCT_LIST, list
                ),
                FIELD_PRODUCTS, DataLayer.listOf(
                    FIELD_PRODUCT_NAME, item.name,
                    FIELD_PRODUCT_ID, item.productId,
                    FIELD_PRODUCT_PRICE, item.priceInt,
                    FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                    FIELD_PRODUCT_POSITION, position
                )
        )
    }

    fun convertPrimaryProductToDataImpressionObject(item: RecommendationItem,
                                                    list: String,
                                                    position: String) {
        DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.priceInt,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_LIST, list,
                FIELD_PRODUCT_POSITION, position
        )
    }

    fun convertPrimaryProductToDataClickObject(item: RecommendationItem,
                                               list: String,
                                               position: String) {
        DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                FIELD_PRODUCT_LIST, list
        ),
                FIELD_PRODUCTS, DataLayer.listOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.priceInt,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_LIST, list,
                FIELD_PRODUCT_POSITION, position
        )
        )
    }

    fun convertPrimaryProductToDataClickObject(item: RecommendationItem,
                                               list: String) {
        DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                FIELD_PRODUCT_LIST, list
        ),
                FIELD_PRODUCTS, DataLayer.listOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.priceInt,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_LIST, list
        )
        )
    }

    // TODO: COMPLETE THIS WITH OBJECT, ACTUALLY DATA FROM API ISN'T READY
    fun convertProductToDataClickAddToCart(item: RecommendationItem,
                                               list: String) {
        DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                FIELD_PRODUCT_LIST, list
        ),
                FIELD_PRODUCTS, DataLayer.listOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId,
                FIELD_PRODUCT_PRICE, item.priceInt,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_SHOP_ID, VALUE_EMPTY,
                FIELD_SHOP_TYPE, VALUE_EMPTY,
                FIELD_SHOP_NAME, VALUE_EMPTY,
                FIELD_CATEGORY_ID, VALUE_EMPTY,
                FIELD_DIMENSION_42, VALUE_EMPTY
        )
        )
    }


    fun eventImpressionOnOrganicProductRecommendationForLoginUser(
            trackingQueue: TrackingQueue,
            recommendationItem: RecommendationItem,
            position: String) {

        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(
                                recommendationItem,
                                String.format(
                                        VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_LOGIN,
                                        recommendationItem.recommendationType
                                ),
                                position)
                    )
                )
        )

        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventImpressionOnTopAdsProductRecommendationForLoginUser(
            trackingQueue: TrackingQueue,
            recommendationItem: RecommendationItem,
            position: String) {

        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(
                                recommendationItem,
                                String.format(
                                        VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_LOGIN,
                                        recommendationItem.recommendationType
                                ),
                                position)
                    )
                )
        )

        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventClickOnOrganicProductRecommendationForLoginUser(
            recommendationItem: RecommendationItem,
            position: String) {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_CLICK,
                        convertRecommendationItemToDataClickObject(
                        recommendationItem,
                                String.format(
                                        VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_LOGIN,
                                        recommendationItem.recommendationType),
                        position)
                )
        )

        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventClickOnTopAdsProductRecommendationForLoginUser(
            recommendationItem: RecommendationItem,
            position: String) {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_CLICK,
                        convertRecommendationItemToDataClickObject(
                                recommendationItem,
                                String.format(
                                        VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_LOGIN,
                                        recommendationItem.recommendationType
                                ),
                                position)
                )
        )

        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventImpressionOnOrganicProductRecommendationForNonLoginUser(
            trackingQueue: TrackingQueue,
            recommendationItem: RecommendationItem,
            position: String) {

        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(
                                recommendationItem,
                                String.format(
                                        VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_NON_LOGIN,
                                        recommendationItem.recommendationType
                                ),
                                position)
                    )
                )
        )

        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventImpressionOnTopAdsProductRecommendationForNonLoginUser(
            trackingQueue: TrackingQueue,
            recommendationItem: RecommendationItem,
            position: String) {

        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(
                                recommendationItem,
                                String.format(
                                        VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_NON_LOGIN,
                                        recommendationItem.recommendationType
                                ),
                                position)
                    )
                )
        )

        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventClickOnOrganicProductRecommendationForNonLoginUser(
            recommendationItem: RecommendationItem,
            position: String) {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_CLICK,
                        convertRecommendationItemToDataClickObject(
                                recommendationItem,
                                String.format(
                                        VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_NON_LOGIN,
                                        recommendationItem.recommendationType
                                ),
                                position)
                )
        )

        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventClickOnTopAdsProductRecommendationForNonLoginUser(
            recommendationItem: RecommendationItem,
            position: String) {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_CLICK,
                        convertRecommendationItemToDataClickObject(
                                recommendationItem,
                                String.format(
                                        VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_NON_LOGIN,
                                        recommendationItem.recommendationType
                                ),
                                position)
                )
        )

        tracker.sendEnhanceEcommerceEvent(data)
    }

    fun eventAddWishlistOnProductRecommendationLogin() {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_PDP,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY
        )

        tracker.sendGeneralEvent(data)
    }

    fun eventRemoveWishlistOnProductRecommendationLogin() {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_PDP,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY
        )

        tracker.sendGeneralEvent(data)
    }

    fun eventAddWishlistOnProductRecommendationNonLogin() {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_PDP,
                EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
                EVENT_ACTION, EVENT_ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY
        )

        tracker.sendGeneralEvent(data)
    }

    fun eventClickIconShare() {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ICON_SHARE,
                EVENT_LABEL, VALUE_EMPTY
        )

        tracker.sendGeneralEvent(data)
    }

    fun eventClickPrimaryProduct(
            recommendationItem: RecommendationItem,
            position: String) {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_PRIMARY_PRODUCT,
                EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CLICK, DataLayer.mapOf(
                        convertPrimaryProductToDataClickObject(
                                recommendationItem,
                                VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION,
                                position
                        )
                )
            )
        )

        tracker.sendGeneralEvent(data)
    }

    fun eventImpressionPrimaryProduct(
            recommendationItem: RecommendationItem,
            position: String) {

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRIMARY_PRODUCT,
                EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_IMPRESSIONS, DataLayer.mapOf(
                        convertPrimaryProductToDataImpressionObject(
                                recommendationItem,
                                VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION,
                                position
                        )
                )
            )
        )

        tracker.sendGeneralEvent(data)
    }

    // No 13 Done
    fun eventUserClickProductToWishlistForUserLogin(
            isAdded: Boolean
    ){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, String.format(EVENT_ACTION_IMPRESSION_PRODUCT_WISHLIST, if(isAdded) "add" else "remove"),
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    // No 14 Done
    fun eventnUserClickProductToWishlistForNonLogin(){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_WISHLIST_NON_LOGIN,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    // No 15 Almost Done (Just wait with complete Data)
    fun eventUserClickAddToCart(
            recommendationItem: RecommendationItem
    ){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_ADD_TO_CART,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_ADD_TO_CART,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_ADD, convertProductToDataClickAddToCart(recommendationItem, VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION)
                )
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    // No 16 Done
    fun eventUserClickRecommendation(){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, VALUE_EMPTY,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    // No 17 Done
    fun eventUserClickOnHeaderNameProduct(
            headerName: String,
            recommendationItem: RecommendationItem,
            position: Int
    ){

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, String.format(EVENT_CLICK_PRODUCT_ON_HEADER_LOGIN, headerName),
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CLICK, convertRecommendationItemToDataClickObject(recommendationItem, String.format(
                        if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK,
                        headerName,
                        recommendationItem.recommendationType
                    ), position.toString())
                )
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    // No 18 Done
    fun eventUserClickOnHeaderNameProductNonLogin(
            headerName: String,
            recommendationItem: RecommendationItem,
            position: Int
    ){

        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, String.format(EVENT_CLICK_PRODUCT_ON_HEADER_NON_LOGIN, headerName),
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CLICK, convertRecommendationItemToDataClickObject(recommendationItem, String.format(
                        if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_NON_LOGIN else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_NON_LOGIN,
                        headerName,
                        recommendationItem.recommendationType
                    ), position.toString())
                )
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    // No 19 Done
    fun eventImpressionProductRecommendationOnHeaderNameLogin(
            trackingQueue: TrackingQueue,
            headerName: String,
            recommendationItem: RecommendationItem,
            position: Int
    ){
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, String.format(EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER_LOGIN, headerName),
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(recommendationItem,
                                String.format(
                                    if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK,
                                    headerName, recommendationItem.recommendationType
                                ), position.toString())
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    // No 20 Done
    fun eventImpressionProductRecommendationOnHeaderName(
            trackingQueue: TrackingQueue,
            headerName: String,
            recommendationItem: RecommendationItem,
            position: Int
    ){
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, String.format(EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER, headerName),
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                            convertRecommendationItemToDataImpressionObject(recommendationItem,
                                    String.format(
                                            if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_NON_LOGIN else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_NON_LOGIN,
                                            headerName, recommendationItem.recommendationType
                                    ), position.toString())
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    // No 21 Done
    fun eventUserClickBack(){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_BACK,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }

    // No 22 Done
    fun eventUserClickSeeToCart(){
        val tracker = getTracker()
        val data = DataLayer.mapOf(
                EVENT, EVENT_CLICK_RECOMMENDATION,
                EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_SEE_CART,
                EVENT_LABEL, VALUE_EMPTY
        )
        tracker.sendEnhanceEcommerceEvent(data)
    }
}
