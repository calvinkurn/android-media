package com.tokopedia.home_recom.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationPageTracking {
    companion object{
        private const val EVENT = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"

        private const val ECOMMERCE = "ecommerce"
        private const val ECOMMERCE_ADD = "add"
        private const val ECOMMERCE_CLICK = "click"
        private const val ECOMMERCE_IMPRESSIONS = "impressions"
        private const val ECOMMERCE_CURRENCY_CODE = "currencyCode"

        private const val FIELD_PRODUCTS = "products"
        private const val FIELD_PRODUCT_NAME = "name"
        private const val FIELD_PRODUCT_ID = "id"
        private const val FIELD_PRODUCT_PRICE = "price"
        private const val FIELD_PRODUCT_BRAND = "brand"
        private const val FIELD_PRODUCT_VARIANT = "variant"
        private const val FIELD_PRODUCT_CATEGORY = "category"
        private const val FIELD_PRODUCT_LIST = "list"
        private const val FIELD_PRODUCT_QUANTITY = "quantity"
        private const val FIELD_PRODUCT_POSITION = "position"
        private const val FIELD_ACTION_FIELD = "actionField"
        private const val FIELD_CATEGORY_ID = "category_id"
        private const val FIELD_SHOP_ID = "shop_id"
        private const val FIELD_SHOP_TYPE = "shop_type"
        private const val FIELD_SHOP_NAME = "shop_name"
        private const val FIELD_DIMENSION_45 = "dimension45"

        private const val VALUE_NONE_OTHER = "none / other"
        private const val VALUE_IDR = "IDR"
        private const val VALUE_EMPTY = ""
        private const val VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION = "/recommendation - primary product"
        private const val VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE = "/recommendation - primary product - %s"
        private const val VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE_TOP_ADS = "/recommendation - primary product - %s - product topads"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK = "/recommendation - %s - rekomendasi untuk anda - %s"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_WITH_SOURCE = "/recommendation - %s - rekomendasi untuk anda - %s - %s"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS = "/recommendation - %s - rekomendasi untuk anda - %s - product topads"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_WITH_SOURCE = "/recommendation - %s - rekomendasi untuk anda - %s - %s - product topads"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_NON_LOGIN = "/recommendation - %s - non login - rekomendasi untuk anda - %s"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_WITH_SOURCE_NON_LOGIN = "/recommendation - %s - non login - rekomendasi untuk anda - %s - %s"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_NON_LOGIN = "/recommendation - %s - non login - rekomendasi untuk anda - %s - product topads"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_WITH_SOURCE_NON_LOGIN = "/recommendation - %s - non login - rekomendasi untuk anda - %s - %s - product topads"

        private const val EVENT_PRODUCT_VIEW = "productView"
        private const val EVENT_PRODUCT_CLICK = "productClick"
        private const val EVENT_ADD_TO_CART = "addToCart"
        private const val EVENT_CLICK_RECOMMENDATION = "clickRecommendation"
        private const val EVENT_LABEL_SOURCE = "source: %s"
        private const val EVENT_LABEL_SOURCE_WITH_PRODUCT_ID = "source: %s - product_id: %s"

        private const val EVENT_CATEGORY_PRODUCT_DETAIL_PAGE = "product detail page"
        private const val EVENT_CATEGORY_RECOMMENDATION_PAGE = "recommendation page"
        private const val EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID = "recommendation page with product id"

        private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN = "click - product recommendation"
        private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN = "click - product recommendation - non login"
        private const val EVENT_CLICK_PRODUCT_ON_HEADER_LOGIN = "click product recommendation on %s"
        private const val EVENT_CLICK_PRODUCT_ON_HEADER_NON_LOGIN = "click product recommendation on %s - non login"
        private const val EVENT_ACTION_IMPRESSION_PRODUCT_ON_HEADER = "impression product recommendation - non login"

        private const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN = "impression - product recommendation"
        private const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN = "impression - product recommendation - non login"
        private const val EVENT_ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_LOGIN = "add - wishlist on product recommendation"
        private const val EVENT_ACTION_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION_LOGIN = "remove - wishlist on product recommendation"
        private const val EVENT_ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_NON_LOGIN = "add - wishlist on product recommendation - non login"
        private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_WISHLIST_NON_LOGIN = "add - wishlist on product recommendation - non login"
        private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_WISHLIST_LOGIN = "%s - wishlist on product recommendation"
        private const val EVENT_ACTION_CLICK_PRIMARY_PRODUCT_NON_LOGIN = "click add wishlist on primary product - non login"
        private const val EVENT_ACTION_CLICK_PRIMARY_PRODUCT_LOGIN = "click %s wishlist on primary product"
        private const val EVENT_ACTION_CLICK_ICON_SHARE = "click icon share"
        private const val EVENT_ACTION_ADD_TO_CART = "click add to cart on primary product"
        private const val EVENT_ACTION_VIEW_RECOMMENDATION_PAGE = "view recommendation page"
        private const val EVENT_ACTION_ADD_TO_CART_NON_LOGIN = "click add to cart on primary product - non login"
        private const val EVENT_ACTION_BUY = "click buy on primary product"
        private const val EVENT_ACTION_BUY_NON_LOGIN = "click buy on primary product - non login"
        private const val EVENT_ACTION_CLICK_BACK = "click back"
        private const val EVENT_ACTION_CLICK_SEE_CART = "click see cart"
        private const val EVENT_ACTION_CLICK_PRIMARY_PRODUCT = "click primary product"
        private const val EVENT_ACTION_IMPRESSION_PRIMARY_PRODUCT = "impression primary product"
        private const val ACTION_FIELD_LIST_CLICK_HEADER = "/recommendation - non login - %s - rekomendasi untuk anda - %s"
        private const val ACTION_FIELD_LIST_CLICK_HEADER_TOP_ADS = "/recommendation - non login - %s - rekomendasi untuk anda - %s - product topads"

        fun getTracker(): ContextAnalytics {
            return TrackApp.getInstance().gtm
        }

        fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem,
                                                            list: String,
                                                            position: String): Any  {
            return DataLayer.mapOf(
                    FIELD_PRODUCT_NAME, item.name,
                    FIELD_PRODUCT_ID, item.productId,
                    FIELD_PRODUCT_PRICE, item.getPriceIntFromString(),
                    FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                    FIELD_PRODUCT_LIST, list,
                    FIELD_PRODUCT_POSITION, position
            )
        }

        fun convertRecommendationItemToDataClickObject(item: RecommendationItem,
                                                       list: String,
                                                       position: String): Any  {
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
                            FIELD_PRODUCT_POSITION, position
                    )
            )
            )
        }

        fun convertPrimaryProductToDataImpressionObject(item: RecommendationItem,
                                                        list: String,
                                                        position: String): Any {
            return DataLayer.mapOf(
                    FIELD_PRODUCT_NAME, item.name,
                    FIELD_PRODUCT_ID, item.productId,
                    FIELD_PRODUCT_PRICE, item.getPriceIntFromString(),
                    FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                    FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_LIST, list,
                    FIELD_PRODUCT_POSITION, position
            )
        }

        fun convertPrimaryProductToDataClickObject(item: RecommendationItem,
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
                            FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                            FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                            FIELD_PRODUCT_LIST, list,
                            FIELD_PRODUCT_POSITION, position
                    )
            )
            )
        }

        // TODO: COMPLETE THIS WITH OBJECT, ACTUALLY DATA FROM API ISN'T READY
        private fun convertProductToDataClickAddToCart(item: RecommendationItem,
                                               list: String): Any {
            return DataLayer.mapOf(
                    FIELD_ACTION_FIELD, DataLayer.mapOf(
                        FIELD_PRODUCT_LIST, list
                    ),
                    FIELD_PRODUCTS, DataLayer.listOf(
                        DataLayer.mapOf(
                                FIELD_PRODUCT_NAME, item.name,
                                FIELD_PRODUCT_ID, item.productId.toString(),
                                FIELD_PRODUCT_PRICE, item.getPriceIntFromString(),
                                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                                FIELD_PRODUCT_QUANTITY, item.quantity.toString(),
                                FIELD_SHOP_ID, item.shopId.toString(),
                                FIELD_SHOP_TYPE, item.shopType,
                                FIELD_SHOP_NAME, item.shopName,
                                FIELD_CATEGORY_ID, item.departmentId.toString(),
                                FIELD_DIMENSION_45, item.cartId.toString()
                        )
                    )
            )
        }

//        // 12
//        fun eventImpressionOnOrganicProductRecommendationForLoginUser(
//                trackingQueue: TrackingQueue,
//                recommendationItem: RecommendationItem,
//                position: String,
//                ref: String) {
//
//            val data = DataLayer.mapOf(
//                    EVENT, EVENT_PRODUCT_VIEW,
//                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
//                    EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRIMARY_PRODUCT,
//                    EVENT_LABEL, VALUE_EMPTY,
//                    ECOMMERCE, DataLayer.mapOf(
//                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
//                        ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
//                                convertRecommendationItemToDataImpressionObject(
//                                        recommendationItem,
//                                        String.format(
//                                                VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE,
//                                                recommendationItem.recommendationType,
//                                                ref
//                                        ),
//                                        position)
//                        )
//                    )
//            )
//
//            trackingQueue.putEETracking(data as HashMap<String, Any>)
//        }
//
//        // 12
//        fun eventImpressionOnTopAdsProductRecommendationForLoginUser(
//                trackingQueue: TrackingQueue,
//                recommendationItem: RecommendationItem,
//                position: String,
//                ref: String) {
//
//            val data = DataLayer.mapOf(
//                    EVENT, EVENT_PRODUCT_VIEW,
//                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
//                    EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRIMARY_PRODUCT,
//                    EVENT_LABEL, VALUE_EMPTY,
//                    ECOMMERCE, DataLayer.mapOf(
//                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
//                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
//                    convertRecommendationItemToDataImpressionObject(
//                            recommendationItem,
//                            String.format(
//                                    VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE_TOP_ADS,
//                                    recommendationItem.recommendationType,
//                                    ref
//                            ),
//                            position)
//                        )
//                    )
//            )
//
//            trackingQueue.putEETracking(data as HashMap<String, Any>)
//        }
//
//        // 4
//        fun eventClickOnOrganicProductRecommendationForLoginUser(
//                recommendationItem: RecommendationItem,
//                position: String,
//                ref: String) {
//
//            val tracker = getTracker()
//            val data = DataLayer.mapOf(
//                    EVENT, EVENT_PRODUCT_CLICK,
//                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
//                    EVENT_ACTION, EVENT_ACTION_CLICK_PRIMARY_PRODUCT,
//                    EVENT_LABEL, VALUE_EMPTY,
//                    ECOMMERCE, DataLayer.mapOf(
//                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
//                        ECOMMERCE_CLICK,
//                            convertRecommendationItemToDataClickObject(
//                                    recommendationItem,
//                                    String.format(
//                                            VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_LOGIN,
//                                            recommendationItem.recommendationType),
//                                    position)
//                    )
//            )
//
//            tracker.sendEnhanceEcommerceEvent(data)
//        }
//
//        // 4
//        fun eventClickOnTopAdsProductRecommendationForLoginUser(
//                recommendationItem: RecommendationItem,
//                position: String,
//                ref: String) {
//
//            val tracker = getTracker()
//            val data = DataLayer.mapOf(
//                    EVENT, EVENT_PRODUCT_CLICK,
//                    EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
//                    EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN,
//                    EVENT_LABEL, VALUE_EMPTY,
//                    ECOMMERCE, DataLayer.mapOf(
//                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
//                    ECOMMERCE_CLICK,
//                    convertRecommendationItemToDataClickObject(
//                            recommendationItem,
//                            String.format(
//                                    VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_LOGIN,
//                                    recommendationItem.recommendationType
//                            ),
//                            position)
//            )
//            )
//
//            tracker.sendEnhanceEcommerceEvent(data)
//        }
//
//        // 5
//        fun eventImpressionOnOrganicProductRecommendationForNonLoginUser(
//                trackingQueue: TrackingQueue,
//                recommendationItem: RecommendationItem,
//                position: String,
//                ref: String) {
//
//            val data = DataLayer.mapOf(
//                    EVENT, EVENT_PRODUCT_VIEW,
//                    EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
//                    EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN,
//                    EVENT_LABEL, VALUE_EMPTY,
//                    ECOMMERCE, DataLayer.mapOf(
//                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
//                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
//                    convertRecommendationItemToDataImpressionObject(
//                            recommendationItem,
//                            String.format(
//                                    VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_NON_LOGIN,
//                                    recommendationItem.recommendationType
//                            ),
//                            position)
//            )
//            )
//            )
//
//            trackingQueue.putEETracking(data as HashMap<String, Any>)
//        }
//
//        // 5
//        fun eventImpressionOnTopAdsProductRecommendationForNonLoginUser(
//                trackingQueue: TrackingQueue,
//                recommendationItem: RecommendationItem,
//                position: String,
//                ref: String) {
//
//            val data = DataLayer.mapOf(
//                    EVENT, EVENT_PRODUCT_VIEW,
//                    EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
//                    EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN,
//                    EVENT_LABEL, VALUE_EMPTY,
//                    ECOMMERCE, DataLayer.mapOf(
//                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
//                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
//                    convertRecommendationItemToDataImpressionObject(
//                            recommendationItem,
//                            String.format(
//                                    VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_NON_LOGIN,
//                                    recommendationItem.recommendationType
//                            ),
//                            position)
//            )
//            )
//            )
//
//            trackingQueue.putEETracking(data as HashMap<String, Any>)
//        }
//
//        // 6
//        fun eventClickOnOrganicProductRecommendationForNonLoginUser(
//                recommendationItem: RecommendationItem,
//                position: String,
//                ref: String) {
//
//            val tracker = getTracker()
//            val data = DataLayer.mapOf(
//                    EVENT, EVENT_PRODUCT_CLICK,
//                    EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
//                    EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN,
//                    EVENT_LABEL, VALUE_EMPTY,
//                    ECOMMERCE, DataLayer.mapOf(
//                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
//                    ECOMMERCE_CLICK,
//                    convertRecommendationItemToDataClickObject(
//                            recommendationItem,
//                            String.format(
//                                    VALUE_LIST_ORGANIC_PRODUCT_RECOMMENDATION_NON_LOGIN,
//                                    recommendationItem.recommendationType
//                            ),
//                            position)
//            )
//            )
//
//            tracker.sendEnhanceEcommerceEvent(data)
//        }
//
//        // 6
//        fun eventClickOnTopAdsProductRecommendationForNonLoginUser(
//                recommendationItem: RecommendationItem,
//                position: String,
//                ref: String) {
//
//            val tracker = getTracker()
//            val data = DataLayer.mapOf(
//                    EVENT, EVENT_PRODUCT_CLICK,
//                    EVENT_CATEGORY, EVENT_CATEGORY_PRODUCT_DETAIL_PAGE,
//                    EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN,
//                    EVENT_LABEL, VALUE_EMPTY,
//                    ECOMMERCE, DataLayer.mapOf(
//                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
//                    ECOMMERCE_CLICK,
//                    convertRecommendationItemToDataClickObject(
//                            recommendationItem,
//                            String.format(
//                                    VALUE_LIST_TOPADS_PRODUCT_RECOMMENDATION_NON_LOGIN,
//                                    recommendationItem.recommendationType
//                            ),
//                            position)
//            )
//            )
//
//            tracker.sendEnhanceEcommerceEvent(data)
//        }

        // 10
        fun eventClickIconShare() {
            getTracker().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_RECOMMENDATION,
                        EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                        EVENT_ACTION, EVENT_ACTION_CLICK_ICON_SHARE,
                        EVENT_LABEL, VALUE_EMPTY
                )
            )
        }

        // 11
        fun eventClickPrimaryProduct(
                recommendationItem: RecommendationItem,
                position: String,
                ref: String) {

            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_CLICK,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRIMARY_PRODUCT,
                    EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CLICK, convertPrimaryProductToDataClickObject(
                            recommendationItem,
                            String.format(
                                    if(recommendationItem.isTopAds)
                                        VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE_TOP_ADS
                                    else
                                        VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE,
                            ref),
                            position
                        )
                    )
            )

            tracker.sendGeneralEvent(data)
        }

        // 12
        fun eventImpressionPrimaryProduct(
                recommendationItem: RecommendationItem,
                position: String,
                ref: String) {

            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_VIEW,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRIMARY_PRODUCT,
                    EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                            convertPrimaryProductToDataImpressionObject(
                                    recommendationItem,
                                    String.format(
                                            if(!recommendationItem.isTopAds)
                                                VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE
                                            else VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE_TOP_ADS,
                                            ref),
                                    position
                            )
                        )
                    )
            )

            tracker.sendGeneralEvent(data)
        }

        // No 13 Done
        fun eventUserClickProductToWishlistForUserLogin(
                isAdded: Boolean,
                ref: String
        ){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, String.format(EVENT_ACTION_CLICK_PRIMARY_PRODUCT_LOGIN, if(isAdded) "add" else "remove"),
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 14 Done
        fun eventUserClickProductToWishlistForNonLogin(ref: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRIMARY_PRODUCT_NON_LOGIN,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 15 Done
        fun eventUserClickAddToCart(
                recommendationItem: RecommendationItem,
                ref: String
        ){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_ADD_TO_CART,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_ADD_TO_CART,
                    EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_ADD, convertProductToDataClickAddToCart(
                            recommendationItem,
                            String.format(VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE, ref)
                        )
                    )
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 16 Done
        fun eventUserAddToCartNonLogin(ref: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_ADD_TO_CART_NON_LOGIN,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 17 Done
        // Replaced with No 51
        fun eventUserClickOnHeaderNameProduct(
                headerName: String,
                recommendationItem: RecommendationItem,
                position: String,
                ref: String
        ){

            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_CLICK,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN,
                    EVENT_LABEL, headerName,
                    ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_CLICK, convertRecommendationItemToDataClickObject(recommendationItem, String.format(
                                if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_WITH_SOURCE else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_WITH_SOURCE,
                                recommendationItem.pageName,
                                recommendationItem.recommendationType,
                                ref
                        ), position)
                    )
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 18 Done
        // Replaced with No 52
        fun eventUserClickOnHeaderNameProductNonLogin(
                headerName: String,
                recommendationItem: RecommendationItem,
                position: String,
                ref: String
        ){

            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_CLICK,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, String.format(EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN, headerName),
                    EVENT_LABEL, headerName,
                    ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_CLICK, convertRecommendationItemToDataClickObject(recommendationItem, String.format(
                            if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_WITH_SOURCE_NON_LOGIN else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_WITH_SOURCE_NON_LOGIN,
                            recommendationItem.pageName,
                            recommendationItem.recommendationType,
                            ref
                        ), position)
                    )
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 19 Done
        // Replaced with No 49
        fun eventImpressionProductRecommendationOnHeaderNameLogin(
                trackingQueue: TrackingQueue,
                headerName: String,
                recommendationItem: RecommendationItem,
                position: String,
                ref: String
        ){
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_VIEW,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
                    EVENT_LABEL, headerName,
                    ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                            convertRecommendationItemToDataImpressionObject(
                                    recommendationItem,
                                    String.format(
                                            if(recommendationItem.isTopAds)
                                                VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_WITH_SOURCE
                                            else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_WITH_SOURCE,
                                            recommendationItem.pageName, recommendationItem.recommendationType,
                                            ref
                                    ), position)
                        )
                    )
            )
            trackingQueue.putEETracking(data as HashMap<String, Any>)
        }

        // No 20 Done
        // Replaced with No 50
        fun eventImpressionProductRecommendationOnHeaderName(
                trackingQueue: TrackingQueue,
                headerName: String,
                recommendationItem: RecommendationItem,
                position: String,
                ref: String
        ){
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_VIEW,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, String.format(EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN, headerName),
                    EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                            convertRecommendationItemToDataImpressionObject(recommendationItem,
                                String.format(
                                        if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_WITH_SOURCE_NON_LOGIN else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_WITH_SOURCE_NON_LOGIN,
                                        recommendationItem.pageName, recommendationItem.recommendationType, ref
                                ), position)
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

        // No 47 Done
        fun eventUserClickBuy(
                recommendationItem: RecommendationItem,
                ref: String
        ){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_ADD_TO_CART,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_BUY,
                    EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_ADD, convertProductToDataClickAddToCart(
                            recommendationItem,
                            String.format(VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE, ref)
                        )
                    )
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 48 Done
        fun eventUserClickBuyNonLogin(ref: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_BUY_NON_LOGIN,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 53 Done
        fun eventUserClickRecommendationWishlistForLogin(
                isAdded: Boolean,
                headerName: String,
                ref: String
        ){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, String.format(EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_WISHLIST_LOGIN, if(isAdded) "add" else "remove"),
                    EVENT_LABEL, "$headerName - ${String.format(EVENT_LABEL_SOURCE, ref)}"
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 54 Done
        fun eventUserClickRecommendationWishlistForNonLogin(
                headerName: String,
                ref: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_WISHLIST_NON_LOGIN,
                    EVENT_LABEL, "$headerName - ${String.format(EVENT_LABEL_SOURCE, ref)}"
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        //No 66 NEW
        fun eventUserViewRecommendationPage(ref: String){
            val data = DataLayer.mapOf(
                    EVENT, VALUE_EMPTY,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE,
                    EVENT_ACTION, EVENT_ACTION_VIEW_RECOMMENDATION_PAGE,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, ref)
            )
            getTracker().sendEnhanceEcommerceEvent(data)
        }

        //No 67 NEW
        fun eventUserViewRecommendationPageWithProductId(
                productId: String,
                ref: String
        ){
            val data = DataLayer.mapOf(
                    EVENT, VALUE_EMPTY,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_VIEW_RECOMMENDATION_PAGE,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE_WITH_PRODUCT_ID, ref, productId)
            )
            getTracker().sendEnhanceEcommerceEvent(data)
        }
    }
}
