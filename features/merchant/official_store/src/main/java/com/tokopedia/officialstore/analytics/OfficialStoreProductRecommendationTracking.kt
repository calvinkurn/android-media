package com.tokopedia.officialstore.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

class OfficialStoreProductRecommendationTracking {

    companion object {
        private const val EVENT = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"

        private const val ECOMMERCE = "ecommerce"
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

        private const val VALUE_NONE_OTHER = "none / other"
        private const val VALUE_IDR = "IDR"
        private const val VALUE_EMPTY = ""

        private const val EVENT_PRODUCT_VIEW = "productView"
        private const val EVENT_PRODUCT_CLICK = "productClick"
        private const val ATTRIBUTION = "attribution"


        private const val PRODUCT_EVENT_ACTION = "impression - product recommendation"

        private const val EVENT_CATEGORY_PRODUCT_RECOMMENDATION = "os microsite - "
        private const val EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID = "recommendation page with product id"

        private fun getTracker(): ContextAnalytics {
            return TrackApp.getInstance().gtm
        }

        // No 21
        fun eventClickProductRecommendation(
                item: RecommendationItem,
                position: String,
                recommendationTitle: String,
                isLogin: Boolean,
                categoryName: String
        ) {
            val tracker = getTracker()
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_CLICK,
                    EVENT_CATEGORY, String.format(EVENT_CATEGORY_PRODUCT_RECOMMENDATION, categoryName), // Here
                    EVENT_ACTION, PRODUCT_EVENT_ACTION,
                    EVENT_LABEL, recommendationTitle,
                    ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CLICK, DataLayer.mapOf(
                        FIELD_ACTION_FIELD, DataLayer.mapOf(
                            FIELD_PRODUCT_LIST, getListProductClickInsideActionField(categoryName, item.recommendationType),
                            FIELD_PRODUCTS, DataLayer.listOf(
                                convertRecommendationItemToDataImpressionObject(item, isLogin, position)
            )))))
            tracker.sendEnhanceEcommerceEvent(data)
        }

        // No 22
        fun eventImpressionProductRecommendation(
                item: RecommendationItem,
                isLogin: Boolean,
                categoryName: String,
                recommendationTitle: String,
                position: String,
                trackingQueue: TrackingQueue) {
            val data = DataLayer.mapOf(
                    EVENT, EVENT_PRODUCT_VIEW,
                    EVENT_CATEGORY, String.format(EVENT_CATEGORY_PRODUCT_RECOMMENDATION, categoryName),
                    EVENT_ACTION, PRODUCT_EVENT_ACTION,
                    EVENT_LABEL, recommendationTitle,
                    ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                            convertRecommendationItemToDataImpressionObject(item, isLogin, position)
            )))
            trackingQueue.putEETracking(data as HashMap<String, Any>)
        }

        private fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem, isLogin: Boolean, position: String): Any {
            return DataLayer.mapOf(
                    FIELD_PRODUCT_NAME, item.name,
                    FIELD_PRODUCT_ID, item.productId,
                    FIELD_PRODUCT_PRICE, item.getPriceIntFromString(),
                    FIELD_PRODUCT_BRAND, item.shopName,
                    FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                    FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_LIST, getListProductInsideProductField(item.recommendationType, item.isTopAds, isLogin),
                    FIELD_PRODUCT_POSITION, position,
                    ATTRIBUTION, VALUE_NONE_OTHER
            )
        }

        private fun getListProductClickInsideActionField(categoryName: String, recommendationType: String): String {
            return "/official-store/${categoryName} - rekomendasi untuk anda - ${recommendationType}"
        }

        private fun getListProductInsideProductField(recommendationType: String, isTopAds: Boolean, isLogin: Boolean): String {
            val stringTopAds = if (isTopAds) " - product topads" else ""
            val stringIsLogin = if (isLogin) "" else " - non login"
            return "/official-store${stringIsLogin} - rekomendasi untuk anda - ${recommendationType}${stringTopAds}"
        }
    }
}