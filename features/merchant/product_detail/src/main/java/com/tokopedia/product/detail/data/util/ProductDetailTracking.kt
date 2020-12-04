package com.tokopedia.product.detail.data.util

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.Action.PRODUCT_VIEW
import com.tokopedia.product.detail.data.util.ProductTrackingConstant.Action.RECOMMENDATION_CLICK
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject


class ProductDetailTracking @Inject constructor(private val trackingQueue: TrackingQueue) {

    fun eventAtcClickLihat(productId: String?) {
        if (productId.isNullOrEmpty()) {
            return
        }
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "click - cek keranjang",
                productId
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventAddToCartRecommendationATCClick(product: RecommendationItem, position: Int, isSessionActive: Boolean, pageName: String, pageTitle: String, mainProductId: String) {
        val valueLoginOrNotLogin = if (!isSessionActive)
            " ${ProductTrackingConstant.Tracking.USER_NON_LOGIN} - "
        else ""
        val listValue = LIST_PRODUCT_AFTER_ATC + pageName + LIST_RECOMMENDATION + valueLoginOrNotLogin +
                product.recommendationType + (if (product.isTopAds) " - product topads - $mainProductId" else " - $mainProductId")
        val actionValuePostfix = if (!isSessionActive)
            " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}"
        else
            ""

        val data = DataLayer.mapOf(
                KEY_EVENT, ProductTrackingConstant.Action.PRODUCT_CLICK,
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP_AFTER_ATC,
                KEY_ACTION, ProductTrackingConstant.Action.TOPADS_ATC_CLICK + actionValuePostfix,
                KEY_LABEL, pageTitle,
                KEY_ECOMMERCE, DataLayer.mapOf(ProductTrackingConstant.Action.ADD, DataLayer.mapOf(
                ACTION_FIELD, DataLayer.mapOf(LIST, listValue),
                PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        PROMO_NAME, product.name,
                        ID, product.productId.toString(),
                        PRICE, removeCurrencyPrice(product.price),
                        BRAND, DEFAULT_VALUE,
                        CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                        VARIANT, DEFAULT_VALUE,
                        PROMO_POSITION, position,
                        DATA_DIMENSION_83, if (product.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                )
        ))
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

    fun eventAddToCartRecommendationImpression(position: Int, product: RecommendationItem, isSessionActive: Boolean, pageName: String, pageTitle: String, mainProductId: String, trackingQueue: TrackingQueue) {
        val valueLoginOrNotLogin = if (!isSessionActive)
            " ${ProductTrackingConstant.Tracking.USER_NON_LOGIN} - "
        else ""
        val listValue = LIST_PRODUCT_AFTER_ATC + pageName + LIST_RECOMMENDATION + valueLoginOrNotLogin +
                product.recommendationType + (if (product.isTopAds) " - product topads - $mainProductId" else " - $mainProductId")
        val valueActionPostfix = if (!isSessionActive)
            " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}"
        else ""

        val enhanceEcommerceData = DataLayer.mapOf(
                KEY_EVENT, PRODUCT_VIEW,
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP_AFTER_ATC,
                KEY_ACTION, ProductTrackingConstant.Action.TOPADS_IMPRESSION + valueActionPostfix,
                KEY_LABEL, pageTitle,
                KEY_ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, CURRENCY_DEFAULT_VALUE,
                IMPRESSIONS, DataLayer.listOf(
                DataLayer.mapOf(
                        PROMO_NAME, product.name,
                        ID, product.productId.toString(),
                        PRICE, removeCurrencyPrice(product.price),
                        BRAND, DEFAULT_VALUE,
                        CATEGORY, product.categoryBreadcrumbs.toLowerCase(),
                        VARIANT, DEFAULT_VALUE,
                        LIST, listValue,
                        PROMO_POSITION, position,
                        DATA_DIMENSION_83, if (product.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                )
        ))
        )
        trackingQueue.putEETracking(enhanceEcommerceData as HashMap<String, Any>?)
    }

    fun eventAddToCartRecommendationWishlist(product: RecommendationItem, isSessionActive: Boolean, isAddWishlist: Boolean) {
        val valueActionPostfix = if (!isSessionActive) " - ${ProductTrackingConstant.Tracking.USER_NON_LOGIN}"
        else ""
        val valueActionPrefix = if (isAddWishlist) "add"
        else "remove"


        val enhanceEcommerceData = DataLayer.mapOf(
                KEY_EVENT, RECOMMENDATION_CLICK,
                KEY_CATEGORY, ProductTrackingConstant.Category.PDP_AFTER_ATC,
                KEY_ACTION, valueActionPrefix + ProductTrackingConstant.Action.ACTION_WISHLIST_ON_PRODUCT_RECOMMENDATION + valueActionPostfix,
                KEY_LABEL, product.header
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(enhanceEcommerceData)
    }

    fun eventClickWishlistOnAffiliate(userId: String,
                                      productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mutableMapOf<String, Any>(KEY_EVENT to ProductTrackingConstant.Affiliate.CLICK_AFFILIATE,
                        KEY_CATEGORY to ProductTrackingConstant.Affiliate.CATEGORY,
                        KEY_ACTION to ProductTrackingConstant.Affiliate.ACTION_CLICK_WISHLIST,
                        KEY_LABEL to productId,
                        KEY_USER_ID to userId))
    }

    fun sendGeneralEvent(event: String, category: String, action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(event,
                category,
                action,
                label)
    }

    private fun removeCurrencyPrice(priceFormatted: String): String {
        return try {
            priceFormatted.replace("[^\\d]".toRegex(), "")
        } catch (t: Throwable) {
            "0"
        }
    }

    fun eventViewHelpPopUpWhenAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_VIEW_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.VIEW_HELP_POP_UP_WHEN_ATC,
                ProductTrackingConstant.Label.EMPTY_LABEL
        )
    }

    fun eventClickReportOnHelpPopUpAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_REPORT_ON_HELP_POP_UP_ATC,
                ProductTrackingConstant.Label.EMPTY_LABEL
        )
    }

    fun eventClickCloseOnHelpPopUpAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_CLOSE_ON_HELP_POP_UP_ATC,
                ProductTrackingConstant.Label.EMPTY_LABEL
        )
    }

    fun eventClickDescriptionTabOnProductDescription(productId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_TAB_DESCRIPTION_ON_PRODUCT_DESCRIPTION,
                ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_CATEGORY = "eventCategory"
        private const val KEY_ACTION = "eventAction"
        private const val KEY_LABEL = "eventLabel"
        private const val KEY_ECOMMERCE = "ecommerce"
        private const val KEY_USER_ID = "user_id"

        const val PDP = "PDP"

        private const val ID = "id"
        private const val PROMO_NAME = "name"
        private const val PROMO_POSITION = "position"

        private const val ACTION_FIELD = "actionField"
        private const val LIST = "list"
        private const val PRODUCTS = "products"
        private const val IMPRESSIONS = "impressions"
        private const val PRICE = "price"
        private const val BRAND = "brand"
        private const val DEFAULT_VALUE = "none / other"
        private const val VARIANT = "variant"
        private const val CATEGORY = "category"
        private const val LIST_RECOMMENDATION = " - rekomendasi untuk anda - "
        private const val LIST_PRODUCT_AFTER_ATC = "/productafteratc  - "
        private const val CURRENCY_CODE = "currencyCode"
        private const val CURRENCY_DEFAULT_VALUE = "IDR"
        private const val DATA_DIMENSION_83 = "dimension83"
        private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        private const val VALUE_NONE_OTHER = "none / other"
        private const val KEY_PRODUCT_ID = "productId"
    }
}
