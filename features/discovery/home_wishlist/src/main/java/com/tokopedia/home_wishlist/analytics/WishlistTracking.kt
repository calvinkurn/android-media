package com.tokopedia.home_wishlist.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by Lukas 25/05/2019
 *
 * A main class of Recommendation Page is [WishlistTracking]
 * this class handling a whole tracking data
 * don't delete this!
 */
class WishlistTracking {
    companion object {
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
        private const val FIELD_DIMENSION_83 = "dimension83"

        private const val VALUE_NONE_OTHER = "none / other"
        private const val VALUE_IDR = "IDR"
        private const val VALUE_EMPTY = ""

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
        private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        private const val CUSTOM_DIMENSION_PRODUCT_ID = "productId"

        private fun getTracker(): ContextAnalytics {
            return TrackApp.getInstance().gtm
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
                    FIELD_DIMENSION_83, if (item.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
            )
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
                            FIELD_PRODUCT_POSITION, position,
                            FIELD_DIMENSION_83, if (item.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                    )
            )
            )
        }

        private fun convertPrimaryProductToDataImpressionObject(item: RecommendationItem,
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

        private fun convertPrimaryProductToDataClickObject(item: RecommendationItem,
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
    }
}
