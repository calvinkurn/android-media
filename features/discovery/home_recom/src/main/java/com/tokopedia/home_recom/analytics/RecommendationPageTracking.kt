package com.tokopedia.home_recom.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by Lukas 25/05/2019
 *
 * A main class of Recommendation Page is [RecommendationPageTracking]
 * this class handling a whole tracking data
 * don't delete this!
 */
class RecommendationPageTracking {
    companion object{
        private const val EVENT = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val SHOP_ID = "shopId"

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
        private const val FIELD_DIMENSION_40 = "dimension40"
        private const val FIELD_DIMENSION_45 = "dimension45"
        private const val FIELD_DIMENSION_83 = "dimension83"
        private const val FIELD_DIMENSION_90 = "dimension90"

        private const val VALUE_NONE_OTHER = "none / other"
        private const val VALUE_IDR = "IDR"
        private const val VALUE_EMPTY = ""
        private const val VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE = "/recommendation - primary product - ref: %s"
        private const val VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_PRODUCT_ID_WITH_SOURCE = "/recommendationwithproductid - primary product - ref: %s"
        private const val VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE_TOP_ADS = "/recommendation - primary product - ref: %s - product topads"
        private const val VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_PRODUCT_ID_WITH_SOURCE_TOP_ADS = "/recommendationwithproductid - primary product - ref: %s - product topads"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_WITH_SOURCE = "/recommendation - %s - rekomendasi untuk anda - %s - ref: %s"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_WITH_SOURCE = "/recommendationwithproductid - %s - rekomendasi untuk anda - %s - ref: %s"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_WITH_SOURCE = "/recommendation - %s - rekomendasi untuk anda - %s - ref: %s - product topads"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_TOP_ADS_WITH_SOURCE = "/recommendationwithproductid - %s - rekomendasi untuk anda - %s - ref: %s - product topads"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_WITH_SOURCE_NON_LOGIN = "/recommendation - %s - non login - rekomendasi untuk anda - %s - ref: %s"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_WITH_SOURCE_NON_LOGIN = "/recommendationwithproductid - %s - non login - rekomendasi untuk anda - %s - ref: %s"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_TOP_ADS_WITH_SOURCE_NON_LOGIN = "/recommendation - %s - non login - rekomendasi untuk anda - %s - ref: %s - product topads"
        private const val VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_TOP_ADS_WITH_SOURCE_NON_LOGIN = "/recommendationwithproductid - %s - non login - rekomendasi untuk anda - %s - ref: %s - product topads"
        private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        private const val VALUE_BEBAS_ONGKIR_EXTRA = "bebas ongkir extra"

        private const val EVENT_PRODUCT_VIEW = "productView"
        private const val EVENT_PRODUCT_CLICK = "productClick"
        private const val EVENT_ADD_TO_CART = "addToCart"
        private const val EVENT_CLICK_RECOMMENDATION = "clickRecommendation"
        private const val EVENT_LABEL_SOURCE = "source: %s"
        private const val EVENT_LABEL_SOURCE_WITH_PRODUCT_ID = "source: %s - product_id: %s"

        private const val EVENT_CATEGORY_RECOMMENDATION_PAGE = "recommendation page"
        private const val EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID = "recommendation page with product id"

        private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN = "click - product recommendation"
        private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN = "click - product recommendation - non login"

        private const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN = "impression - product recommendation"
        private const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN = "impression - product recommendation - non login"
        private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_WISHLIST_NON_LOGIN = "add - wishlist on product recommendation - non login"
        private const val EVENT_ACTION_CLICK_SEE_MORE = "click - see more on widget %s"
        private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_WISHLIST_LOGIN = "%s - wishlist on product recommendation"
        private const val EVENT_ACTION_CLICK_PRIMARY_PRODUCT_NON_LOGIN = "click add wishlist on primary product - non login"
        private const val EVENT_ACTION_CLICK_PRIMARY_PRODUCT_LOGIN = "click %s wishlist on primary product"
        private const val EVENT_ACTION_CLICK_ICON_SHARE = "click icon share"
        private const val EVENT_ACTION_ADD_TO_CART = "click add to cart on primary product"
        private const val EVENT_ACTION_ADD_TO_CART_NON_LOGIN = "click add to cart on primary product - non login"
        private const val EVENT_ACTION_BUY = "click buy on primary product"
        private const val EVENT_ACTION_BUY_NON_LOGIN = "click buy on primary product - non login"
        private const val EVENT_ACTION_CLICK_BACK = "click back"
        private const val EVENT_ACTION_CLICK_SEE_CART = "click see cart"
        private const val EVENT_ACTION_CLICK_PRIMARY_PRODUCT = "click primary product"
        private const val EVENT_ACTION_IMPRESSION_PRIMARY_PRODUCT = "impression primary product"

        private const val CUSTOM_DIMENSION_PAGE_SOURCE = "pageSource"
        private const val CUSTOM_DIMENSION_PRODUCT_ID = "productId"

        private fun getTracker(): ContextAnalytics {
            return TrackApp.getInstance().gtm
        }

        private fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem,
                                                                    list: String,
                                                                    position: String,
                                                                    internalRef: String): Any  {
            return DataLayer.mapOf(
                    FIELD_PRODUCT_NAME, item.name,
                    FIELD_PRODUCT_ID, item.productId,
                    FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                    FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                    FIELD_PRODUCT_LIST, list,
                    FIELD_PRODUCT_POSITION, position,
                    FIELD_DIMENSION_83, getBebasOngkirValue(item),
                    FIELD_DIMENSION_90, internalRef
            )
        }

        private fun convertRecommendationItemToDataClickObject(item: RecommendationItem,
                                                               list: String,
                                                               position: String,
                                                               internalRef: String): Any  {
            return DataLayer.mapOf(
                    FIELD_ACTION_FIELD, DataLayer.mapOf(
                    FIELD_PRODUCT_LIST, list
            ),
                    FIELD_PRODUCTS, DataLayer.listOf(
                    DataLayer.mapOf(
                            FIELD_PRODUCT_NAME, item.name,
                            FIELD_PRODUCT_ID, item.productId,
                            FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                            FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                            FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                            FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                            FIELD_PRODUCT_POSITION, position,
                            FIELD_DIMENSION_83, getBebasOngkirValue(item),
                            FIELD_DIMENSION_90, internalRef
                    )
            )
            )
        }

        private fun convertPrimaryProductToDataImpressionObject(item: RecommendationItem,
                                                                list: String,
                                                                position: String,
                                                                internalRef: String): Any {
            return DataLayer.mapOf(
                    FIELD_PRODUCT_NAME, item.name,
                    FIELD_PRODUCT_ID, item.productId,
                    FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                    FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                    FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_LIST, list,
                    FIELD_PRODUCT_POSITION, position,
                    FIELD_DIMENSION_90, internalRef
            )
        }

        private fun convertPrimaryProductToDataClickObject(item: RecommendationItem,
                                                           list: String,
                                                           position: String,
                                                           internalRef: String): Any {
            return DataLayer.mapOf(
                    FIELD_ACTION_FIELD, DataLayer.mapOf(
                    FIELD_PRODUCT_LIST, list
            ),
                    FIELD_PRODUCTS, DataLayer.listOf(
                    DataLayer.mapOf(
                            FIELD_PRODUCT_NAME, item.name,
                            FIELD_PRODUCT_ID, item.productId,
                            FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                            FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                            FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                            FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                            FIELD_PRODUCT_LIST, list,
                            FIELD_PRODUCT_POSITION, position,
                            FIELD_DIMENSION_90, internalRef
                    )
            )
            )
        }

        private fun convertProductToDataClickAddToCart(item: RecommendationItem,
                                               list: String, internalRef: String): Any {
            return DataLayer.mapOf(
                    FIELD_PRODUCTS, DataLayer.listOf(
                        DataLayer.mapOf(
                                FIELD_PRODUCT_NAME, item.name,
                                FIELD_PRODUCT_ID, item.productId.toString(),
                                FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                                FIELD_PRODUCT_QUANTITY, item.quantity.toString(),
                                FIELD_SHOP_ID, item.shopId.toString(),
                                FIELD_SHOP_TYPE, item.shopType,
                                FIELD_SHOP_NAME, item.shopName,
                                FIELD_CATEGORY_ID, item.departmentId.toString(),
                                FIELD_DIMENSION_40, list,
                                FIELD_DIMENSION_45, item.cartId.toString(),
                                FIELD_DIMENSION_90, internalRef
                        )
                    )
            )
        }

        private fun getBebasOngkirValue(item: RecommendationItem): String{
            val hasFulfillment = item.labelGroupList.hasLabelGroupFulfillment()
            return if(item.isFreeOngkirActive && hasFulfillment) VALUE_BEBAS_ONGKIR_EXTRA
            else if(item.isFreeOngkirActive && !hasFulfillment) VALUE_BEBAS_ONGKIR
            else VALUE_NONE_OTHER
        }

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
                ref: String,
                internalRef: String) {

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
                            if(ref.isEmpty()) "null" else ref),
                            position,
                            internalRef
                        )
                    )
            )

            tracker.sendGeneralEvent(data)
        }

        // 12
        fun eventImpressionPrimaryProduct(
                recommendationItem: RecommendationItem,
                position: String,
                ref: String,
                internalRef: String) {

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
                                            if(ref.isEmpty()) "null" else ref),
                                    position,
                                    internalRef
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
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)
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
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 15 Done
        fun eventUserClickAddToCart(
                recommendationItem: RecommendationItem,
                ref: String,
                internalRef: String
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
                            String.format(VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE, if(ref.isEmpty()) "null" else ref),
                            internalRef
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
                ref: String,
                internalRef: String
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
                                if(ref.isEmpty()) "null" else ref
                        ), position, internalRef)
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
                ref: String,
                internalRef: String
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
                            if(ref.isEmpty()) "null" else ref
                        ), position, internalRef)
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
                ref: String,
                internalRef: String
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
                                            if(ref.isEmpty()) "null" else ref
                                    ),
                                    position,
                                    internalRef)
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
                ref: String,
                internalRef: String
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
                                        recommendationItem.pageName, recommendationItem.recommendationType, if(ref.isEmpty()) "null" else ref
                                ), position, internalRef)
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
                ref: String,
                internalRef: String
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
                            String.format(VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_WITH_SOURCE, if(ref.isEmpty()) "null" else ref),
                            internalRef
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
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)
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
                    EVENT_LABEL, "$headerName - ${String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)}"
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
                    EVENT_LABEL, "$headerName - ${String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)}"
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // 68
        fun eventClickIconShareWithProductId() {
            getTracker().sendGeneralEvent(
                    DataLayer.mapOf(
                            EVENT, EVENT_CLICK_RECOMMENDATION,
                            EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                            EVENT_ACTION, EVENT_ACTION_CLICK_ICON_SHARE,
                            EVENT_LABEL, VALUE_EMPTY
                    )
            )
        }

        // 69
        fun eventClickPrimaryProductWithProductId(
                recommendationItem: RecommendationItem,
                position: String,
                ref: String,
                internalRef: String) {

            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_CLICK,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRIMARY_PRODUCT,
                    EVENT_LABEL, VALUE_EMPTY,
                    SHOP_ID, recommendationItem.shopId,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CLICK, convertPrimaryProductToDataClickObject(
                    recommendationItem,
                    String.format(
                            if(recommendationItem.isTopAds)
                                VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_PRODUCT_ID_WITH_SOURCE_TOP_ADS
                            else
                                VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_PRODUCT_ID_WITH_SOURCE,
                            if(ref.isEmpty()) "null" else ref),
                    position,
                    internalRef
            )
            )
            )

            tracker.sendEnhanceEcommerceEvent(data)
        }

        // 70
        fun eventImpressionPrimaryProductWithProductId(
                recommendationItem: RecommendationItem,
                position: String,
                ref: String,
                internalRef: String) {

            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_VIEW,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRIMARY_PRODUCT,
                    EVENT_LABEL, VALUE_EMPTY,
                    SHOP_ID, recommendationItem.shopId,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                    convertPrimaryProductToDataImpressionObject(
                            recommendationItem,
                            String.format(
                                    if(!recommendationItem.isTopAds)
                                        VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_PRODUCT_ID_WITH_SOURCE
                                    else VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_PRODUCT_ID_WITH_SOURCE_TOP_ADS,
                                    if(ref.isEmpty()) "null" else ref),
                            position,
                            internalRef
                    )
            )
            )
            )

            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 71
        fun eventUserClickProductToWishlistForUserLoginWithProductId(
                isAdded: Boolean,
                ref: String,
                shopId: String
        ){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, String.format(EVENT_ACTION_CLICK_PRIMARY_PRODUCT_LOGIN, if(isAdded) "add" else "remove"),
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref),
                    SHOP_ID, shopId
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 72
        fun eventUserClickProductToWishlistForNonLoginWithProductId(ref: String, shopId: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRIMARY_PRODUCT_NON_LOGIN,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref),
                    SHOP_ID, shopId
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 73
        fun eventUserClickAddToCartWithProductId(
                recommendationItem: RecommendationItem,
                ref: String,
                internalRef: String
        ){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_ADD_TO_CART,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_ADD_TO_CART,
                    EVENT_LABEL, VALUE_EMPTY,
                    SHOP_ID, recommendationItem.shopId,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_ADD, convertProductToDataClickAddToCart(
                    recommendationItem,
                    String.format(VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_PRODUCT_ID_WITH_SOURCE, if(ref.isEmpty()) "null" else ref),
                    internalRef
            )
            )
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 74
        fun eventUserAddToCartNonLoginWithProductId(ref: String, shopId: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_ADD_TO_CART_NON_LOGIN,
                    SHOP_ID, shopId,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 75
        fun eventUserClickBackWithProductId(){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_CLICK_BACK,
                    EVENT_LABEL, VALUE_EMPTY
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 76
        fun eventUserClickSeeToCartWithProductId(){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_CLICK_SEE_CART,
                    EVENT_LABEL, VALUE_EMPTY
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 77
        fun eventUserClickBuyWithProductId(
                recommendationItem: RecommendationItem,
                ref: String,
                internalRef: String
        ){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_ADD_TO_CART,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_BUY,
                    EVENT_LABEL, VALUE_EMPTY,
                    SHOP_ID, recommendationItem.shopId,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_ADD, convertProductToDataClickAddToCart(
                    recommendationItem,
                    String.format(VALUE_LIST_PRIMARY_PRODUCT_RECOMMENDATION_PRODUCT_ID_WITH_SOURCE, if(ref.isEmpty()) "null" else ref),
                    internalRef
            )
            )
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 78
        fun eventUserClickBuyNonLoginWithProductId(ref: String, shopId: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_BUY_NON_LOGIN,
                    SHOP_ID, shopId,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 79
        fun eventImpressionProductRecommendationOnHeaderNameLoginWithProductId(
                trackingQueue: TrackingQueue,
                headerName: String,
                recommendationItem: RecommendationItem,
                position: String,
                ref: String,
                internalRef: String
        ){
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_VIEW,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
                    EVENT_LABEL, headerName,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                    convertRecommendationItemToDataImpressionObject(
                            recommendationItem,
                            String.format(
                                    if(recommendationItem.isTopAds)
                                        VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_TOP_ADS_WITH_SOURCE
                                    else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_WITH_SOURCE,
                                    recommendationItem.pageName, recommendationItem.recommendationType,
                                    if(ref.isEmpty()) "null" else ref
                            ), position, internalRef)
            )
            )
            )
            trackingQueue.putEETracking(data as HashMap<String, Any>)
        }

        // No 80
        fun eventImpressionProductRecommendationOnHeaderNameWithProductId(
                trackingQueue: TrackingQueue,
                headerName: String,
                recommendationItem: RecommendationItem,
                position: String,
                ref: String,
                internalRef: String
        ){
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_VIEW,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, String.format(EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_NON_LOGIN, headerName),
                    EVENT_LABEL, VALUE_EMPTY,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                    convertRecommendationItemToDataImpressionObject(recommendationItem,
                            String.format(
                                    if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_TOP_ADS_WITH_SOURCE_NON_LOGIN else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_WITH_SOURCE_NON_LOGIN,
                                    recommendationItem.pageName, recommendationItem.recommendationType, if(ref.isEmpty()) "null" else ref
                            ), position, internalRef)
            )
            )
            )
            trackingQueue.putEETracking(data as HashMap<String, Any>)
        }

        // No 81
        fun eventUserClickOnHeaderNameProductWithProductId(
                headerName: String,
                recommendationItem: RecommendationItem,
                position: String,
                ref: String,
                internalRef: String
        ){

            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_CLICK,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_LOGIN,
                    EVENT_LABEL, headerName,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_CLICK, convertRecommendationItemToDataClickObject(recommendationItem, String.format(
                    if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_TOP_ADS_WITH_SOURCE else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_WITH_SOURCE,
                    recommendationItem.pageName,
                    recommendationItem.recommendationType,
                    if(ref.isEmpty()) "null" else ref
            ), position, internalRef)
            )
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 82
        fun eventUserClickOnHeaderNameProductNonLoginWithProductId(
                headerName: String,
                recommendationItem: RecommendationItem,
                position: String,
                ref: String,
                internalRef: String
        ){

            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_CLICK,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, String.format(EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_NON_LOGIN, headerName),
                    EVENT_LABEL, headerName,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_CLICK, convertRecommendationItemToDataClickObject(recommendationItem, String.format(
                    if(recommendationItem.isTopAds) VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_TOP_ADS_WITH_SOURCE_NON_LOGIN else VALUE_LIST_RECOMMENDATION_PRODUCT_CLICK_PRODUCT_ID_WITH_SOURCE_NON_LOGIN,
                    recommendationItem.pageName,
                    recommendationItem.recommendationType,
                    if(ref.isEmpty()) "null" else ref
            ), position, internalRef)
            )
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 83
        fun eventUserClickRecommendationWishlistForLoginWithProductId(
                isAdded: Boolean,
                ref: String
        ){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, String.format(EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_WISHLIST_LOGIN, if(isAdded) "add" else "remove"),
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 84
        fun eventUserClickRecommendationWishlistForNonLoginWithProductId(
                ref: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION_WISHLIST_NON_LOGIN,
                    EVENT_LABEL, String.format(EVENT_LABEL_SOURCE, if(ref.isEmpty()) "null" else ref)
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 90 Done
        fun sendScreenRecommendationPage(
                screenName: String,
                productId: String?,
                pageSource: String
        ){
            val customDimension= DataLayer.mapOf(
                    CUSTOM_DIMENSION_PAGE_SOURCE, pageSource,
                    CUSTOM_DIMENSION_PRODUCT_ID, productId
            ) as MutableMap<String, String>

            getTracker().sendScreenAuthenticated(screenName, customDimension)
        }

        // No 91 Done
        fun sendScreenSimilarProductRecommendationPage(
                screenName: String,
                productId: String?,
                pageSource: String
        ){
            val customDimension= DataLayer.mapOf(
                    CUSTOM_DIMENSION_PAGE_SOURCE, pageSource,
                    CUSTOM_DIMENSION_PRODUCT_ID, productId
            ) as MutableMap<String, String>

            getTracker().sendScreenAuthenticated(screenName, customDimension)
        }

        // No 108 - 110
        fun eventUserClickSeeMore(
                pageName: String,
                productId: String){
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_RECOMMENDATION,
                    EVENT_CATEGORY, if(productId.isEmpty() || productId.isBlank()) EVENT_CATEGORY_RECOMMENDATION_PAGE else EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID,
                    EVENT_ACTION, String.format(EVENT_ACTION_CLICK_SEE_MORE, pageName),
                    EVENT_LABEL, VALUE_EMPTY
            )
            tracker.sendEnhanceEcommerceEvent(data)
        }
    }
}
