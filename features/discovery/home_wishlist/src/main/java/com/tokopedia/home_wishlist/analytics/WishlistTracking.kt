package com.tokopedia.home_wishlist.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

/**
 * Created by Lukas 25/05/2019
 *
 * A main class of Recommendation Page is [WishlistTracking]
 * this class handling a whole tracking data
 * don't delete this!
 */
object WishlistTracking {

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
    private const val EVENT_CLICK_WISHLIST = "clickWishlist"
    private const val EVENT_LABEL_RECOM_WISHLIST = "%s - %s - %s"
    private const val EVENT_LABEL_REMOVE_BULK_WISHLIST = "%s - general - multiple remove"
    private const val EVENT_LABEL_RECOM_WISHLIST_EMPTY_WISHLIST = "%s - %s - %s - empty_wishlist"

    private const val EVENT_WISHLIST_PAGE = "wishlist page"
    private const val IMPRESSION_LIST = "/wishlist"
    private const val IMPRESSION_EMPTY_LIST = "/wishlist - rekomendasi untuk anda - empty_wishlist - %s"
    private const val IMPRESSION_EMPTY_LIST_TOPADS = "/wishlist - rekomendasi untuk anda - empty_wishlist - %s - product topads"
    private const val IMPRESSION_LIST_RECOMMENDATION = "/wishlist - rekomendasi untuk anda - %s%s"

    private const val EVENT_ACTION_REMOVE_WISHLIST = "remove wishlist - wishlist - login"
    private const val EVENT_ACTION_CANCEL_REMOVE_WISHLIST = "click batalkan hapus wishlist"
    private const val EVENT_ACTION_CLICK_REMOVE_WISHLIST = "click remove wishlist on product recommendation"
    private const val EVENT_ACTION_CLICK_ADD_WISHLIST = "click add wishlist on product recommendation"

    private const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN = "impression on product recommendation"
    private const val EVENT_ACTION_IMPRESSION_WISHLIST = "product impressions"
    private const val EVENT_ACTION_CLICK_PRODUCT = "click product"
    private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION = "click on product recommendation"
    private const val EVENT_ACTION_CLICK_SEE_CART = "click - cek keranjang on wishlist"
    private const val EVENT_ACTION_CLICK_BUY = "click - beli - app only"
    private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"

    private fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }

    private fun convertWishlistItemToDataImpressionObject(item: WishlistItem,
                                                          list: String,
                                                          position: String): Any {
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.id,
                FIELD_PRODUCT_PRICE, item.price,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumb,
                FIELD_PRODUCT_LIST, list,
                FIELD_PRODUCT_POSITION, position,
                FIELD_DIMENSION_83, if (item.freeOngkir.isActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
        )
    }

    private fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem,
                                                          list: String,
                                                          position: Int): Any {
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId.toString(),
                FIELD_PRODUCT_PRICE, item.price,
                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_LIST, list,
                FIELD_PRODUCT_POSITION, position,
                FIELD_DIMENSION_83, if (item.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
        )
    }

    private fun convertWishlistItemToDataClickObject(item: WishlistItem,
                                                           list: String,
                                                           position: String): Any {
        return DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                        FIELD_PRODUCT_LIST, list
                ),
                FIELD_PRODUCTS, DataLayer.listOf(
                        DataLayer.mapOf(
                                FIELD_PRODUCT_NAME, item.name,
                                FIELD_PRODUCT_ID, item.id,
                                FIELD_PRODUCT_PRICE, item.price,
                                FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumb,
                                FIELD_PRODUCT_POSITION, position,
                                FIELD_DIMENSION_83, if (item.freeOngkir.isActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                        )
                )
        )
    }

    private fun convertRecommendationItemToDataClickObject(item: RecommendationItem,
                                                     list: String,
                                                     position: Int): Any {
        return DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                FIELD_PRODUCT_LIST, list
        ),
                FIELD_PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        FIELD_PRODUCT_NAME, item.name,
                        FIELD_PRODUCT_ID, item.productId.toString(),
                        FIELD_PRODUCT_PRICE, item.price,
                        FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                        FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                        FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                        FIELD_PRODUCT_POSITION, position,
                        FIELD_DIMENSION_83, if (item.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                )
        )
        )
    }

    private fun convertProductToBuy(item: WishlistItem,
                                    cartId: String,
                                           list: String): Any {
        return DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                FIELD_PRODUCT_LIST, list
        ),
                FIELD_PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        FIELD_PRODUCT_NAME, item.name,
                        FIELD_PRODUCT_ID, item.id,
                        FIELD_PRODUCT_PRICE, item.price,
                        FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                        FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumb,
                        FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                        FIELD_PRODUCT_QUANTITY, 1,
                        FIELD_SHOP_ID, item.shop.id,
                        FIELD_SHOP_TYPE, VALUE_NONE_OTHER,
                        FIELD_SHOP_NAME, item.shop.name,
                        FIELD_CATEGORY_ID, VALUE_NONE_OTHER,
                        FIELD_DIMENSION_45, cartId,
                        FIELD_DIMENSION_83, if (item.freeOngkir.isActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                )
        )
        )
    }

    fun clickBuy(wishlistItem: WishlistItem, cartId: String){
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_WISHLIST,
                        EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                        EVENT_ACTION, EVENT_ACTION_CLICK_BUY,
                        EVENT_LABEL, wishlistItem.id,
                        ECOMMERCE, DataLayer.mapOf(
                                ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                                ECOMMERCE_ADD, convertProductToBuy(
                                        item = wishlistItem,
                                        cartId = cartId,
                                        list = IMPRESSION_LIST
                                )
                        )
                )
        )
    }

    fun clickSeeCart(){
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_WISHLIST,
                        EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                        EVENT_ACTION, EVENT_ACTION_CLICK_SEE_CART
                )
        )
    }

    fun clickConfirmRemoveWishlist(productId: String){
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_WISHLIST,
                        EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                        EVENT_LABEL, productId,
                        EVENT_ACTION, EVENT_ACTION_REMOVE_WISHLIST
                )
        )
    }

    fun clickConfirmBulkRemoveWishlist(trackingQueue: TrackingQueue, productIds: List<String>){
        productIds.forEach { id ->
            val data = DataLayer.mapOf(
                    EVENT, EVENT_CLICK_WISHLIST,
                    EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                    EVENT_LABEL, String.format(EVENT_LABEL_REMOVE_BULK_WISHLIST, id),
                    EVENT_ACTION, EVENT_ACTION_REMOVE_WISHLIST
            )
            trackingQueue.putEETracking(data as HashMap<String, Any>?)
        }
    }

    fun clickCancelDeleteWishlist(){
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_WISHLIST,
                        EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                        EVENT_LABEL, VALUE_EMPTY,
                        EVENT_ACTION, EVENT_ACTION_CANCEL_REMOVE_WISHLIST
                )
        )
    }

    fun productClick(wishlistItem: WishlistItem, position: String){
        getTracker().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_CLICK,
                        EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                        EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT,
                        EVENT_LABEL, position,
                        ECOMMERCE, DataLayer.mapOf(
                                ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                                ECOMMERCE_CLICK,
                                        convertWishlistItemToDataClickObject(
                                                item = wishlistItem,
                                                list = IMPRESSION_LIST,
                                                position = position
                                        )

                        )
                )
        )
    }

    fun impressionProduct(trackingQueue: TrackingQueue, wishlistItem: WishlistItem, position: String){
        val map = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_WISHLIST,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                                convertWishlistItemToDataImpressionObject(
                                        item = wishlistItem,
                                        list = IMPRESSION_LIST,
                                        position = position
                                )
                        )
                )
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun impressionRecommendation(trackingQueue: TrackingQueue, item: RecommendationItem, position: Int){
        val map = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                                convertRecommendationItemToDataImpressionObject(
                                        item = item,
                                        list = IMPRESSION_LIST,
                                        position = position
                                )
                        )
                )
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun impressionEmptyWishlistRecommendation(trackingQueue: TrackingQueue, item: RecommendationItem, position: Int){
        val map = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
                EVENT_LABEL, VALUE_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                                convertRecommendationItemToDataImpressionObject(
                                        item = item,
                                        list = String.format(if(item.isTopAds) IMPRESSION_EMPTY_LIST_TOPADS else IMPRESSION_EMPTY_LIST, item.recommendationType),
                                        position = position
                                )
                        )
                )
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun clickRecommendation(item: RecommendationItem, position: Int){
        getTracker().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_PRODUCT_CLICK,
                        EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                        EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION,
                        EVENT_LABEL, VALUE_EMPTY,
                        ECOMMERCE, DataLayer.mapOf(
                                ECOMMERCE_CLICK, DataLayer.listOf(
                                        convertRecommendationItemToDataClickObject(
                                                item = item,
                                                list = String.format(IMPRESSION_LIST_RECOMMENDATION, item.recommendationType, if(item.isTopAds) "- product topads" else ""),
                                                position = position
                                        )
                                )
                        )
                )
        )
    }

    fun clickWishlistIconRecommendation(productId: String, isTopAds: Boolean, recomTitle: String, isAdd: Boolean){
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_WISHLIST,
                        EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                        EVENT_LABEL, String.format(EVENT_LABEL_RECOM_WISHLIST, productId, if(isTopAds) "topads" else "general", recomTitle),
                        EVENT_ACTION, if(isAdd) EVENT_ACTION_CLICK_ADD_WISHLIST else EVENT_ACTION_CLICK_REMOVE_WISHLIST
                )
        )
    }

    fun clickEmptyWishlistIconRecommendation(productId: String, isTopAds: Boolean, recomTitle: String, isAdd: Boolean){
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_WISHLIST,
                        EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                        EVENT_LABEL, String.format(EVENT_LABEL_RECOM_WISHLIST_EMPTY_WISHLIST, productId, if(isTopAds) "topads" else "general", recomTitle),
                        EVENT_ACTION, if(isAdd) EVENT_ACTION_CLICK_ADD_WISHLIST else EVENT_ACTION_CLICK_REMOVE_WISHLIST
                )
        )
    }
}