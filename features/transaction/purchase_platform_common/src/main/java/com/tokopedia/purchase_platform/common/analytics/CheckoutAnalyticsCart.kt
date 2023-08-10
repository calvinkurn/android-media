package com.tokopedia.purchase_platform.common.analytics

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.ExtraKey
import com.tokopedia.track.TrackApp
import java.util.*

/**
 * @author anggaprasetiyo on 18/05/18.
 */
class CheckoutAnalyticsCart(context: Context) : TransactionAnalytics() {
    private var irisSession = IrisSession(context)

    @Deprecated("")
    fun eventClickAtcCartClickKuponFromGunakanKodePromoAtauKupon() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun eventClickAtcCartClickKodePromoFromGunakanPromoAtauKupon() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun eventClickAtcCartClickKuponSayaFromGunakanPromoAtauKupon() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    @Deprecated("")
    fun eventClickAtcCartClickGunakanKodeFormGunakanKodePromoAtauKupon() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun eventClickAtcCartClickShop(shopId: String, shopName: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_SHOP,
            "$shopId - $shopName"
        )
    }

    fun eventClickAtcCartClickProductName(productName: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_PRODUCT_NAME,
            productName
        )
    }

    fun eventClickAtcCartClickButtonPlus() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_PLUS
        )
    }

    fun eventClickAtcCartClickButtonMinus() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_MIN
        )
    }

    fun eventClickAtcCartClickTrashBin() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_TRASH_BIN
        )
    }

    fun eventClickAtcCartClickArrowBack() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ARROW_BACK
        )
    }

    fun eventClickAtcCartClickBelanjaSekarangOnEmptyCart() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_BELANJA_SEKARANG_ON_EMPTY_CART
        )
    }

    fun eventViewAtcCartImpressionCartEmpty() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.IMPRESSION_CART_EMPTY
        )
    }

    @Deprecated("")
    fun eventClickAtcCartClickArrowBackFromHapus() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ARROW_BACK_FROM_HAPUS
        )
    }

    fun eventClickAtcCartClickTulisCatatan() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_TULIS_CATATAN
        )
    }

    fun eventClickAtcCartClickInputQuantity(qty: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_INPUT_QUANTITY,
            qty
        )
    }

    @Deprecated("")
    fun eventClickAtcCartClickXFromGunakanKodePromoAtauKupon() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_X_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON
        )
    }

    fun eventViewAtcCartImpressionOnPopUpKupon() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.IMPRESSION_ON_POP_UP_KUPON,
            ConstantTransactionAnalytics.EventLabel.KUOTA_PENUKARAN
        )
    }

    fun enhancedECommerceRemoveFromCartClickHapusFromTrashBin(cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.REMOVE_FROM_CART,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_FROM_TRASH_BIN,
            ""
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedECommerceRemoveFromCartClickHapusProdukBerkendala(cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.REMOVE_FROM_CART,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_PRODUK_BERKENDALA,
            ""
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedECommerceRemoveFromCartClickHapusFromHapusProdukBerkendala(cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.REMOVE_FROM_CART,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_FROM_HAPUS_PRODUK_BERKENDALA,
            ""
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    private fun sendEnhancedECommerce(step: Int, cartMap: Map<String, Any>, eventLabel: String, userId: String, promoFlag: Boolean) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.CHECKOUT,
            ConstantTransactionAnalytics.EventCategory.CART,
            if (step == 0) ConstantTransactionAnalytics.EventAction.VIEW_CART_PAGE else ConstantTransactionAnalytics.EventAction.CLICK_CHECKOUT,
            eventLabel
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        dataLayer[ConstantTransactionAnalytics.Key.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        dataLayer[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        dataLayer[KEY_SESSION_IRIS] = irisSession.getSessionId()
        dataLayer[ExtraKey.USER_ID] = userId
        dataLayer[ExtraKey.PROMO_FLAG] = promoFlag.toString()
        sendEnhancedEcommerce(dataLayer)
    }

    private fun flushEnhancedECommerce() {
        val dataLayer = hashMapOf<String, Any?>(
            ConstantTransactionAnalytics.Key.E_COMMERCE to null,
            ConstantTransactionAnalytics.Key.CURRENT_SITE to null
        )
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedECommerceCartLoadedStep0(cartMap: Map<String, Any>, userId: String, promoFlag: Boolean) {
        sendEnhancedECommerce(0, cartMap, "", userId, promoFlag)
        flushEnhancedECommerce()
    }

    fun enhancedECommerceGoToCheckoutStep1SuccessDefault(cartMap: Map<String, Any>, eligibleCod: Boolean, userId: String, promoFlag: Boolean) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_DEFAULT_ELIGIBLE_COD, userId, false)
        } else {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_DEFAULT, userId, false)
        }
        flushEnhancedECommerce()
    }

    fun enhancedECommerceGoToCheckoutStep1SuccessCheckAll(cartMap: Map<String, Any>, eligibleCod: Boolean, userId: String, promoFlag: Boolean) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_CHECK_ALL_ELIGIBLE_COD, userId, promoFlag)
        } else {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_CHECK_ALL, userId, promoFlag)
        }
        flushEnhancedECommerce()
    }

    fun enhancedECommerceGoToCheckoutStep1SuccessPartialShop(cartMap: Map<String, Any>, eligibleCod: Boolean, userId: String, promoFlag: Boolean) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_ELIGIBLE_COD, userId, promoFlag)
        } else {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP, userId, promoFlag)
        }
        flushEnhancedECommerce()
    }

    fun enhancedECommerceGoToCheckoutStep1SuccessPartialProduct(cartMap: Map<String, Any>, eligibleCod: Boolean, userId: String, promoFlag: Boolean) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT_ELIGIBLE_COD, userId, promoFlag)
        } else {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_PARTIAL_PRODUCT, userId, promoFlag)
        }
        flushEnhancedECommerce()
    }

    fun enhancedECommerceGoToCheckoutStep1SuccessPartialShopAndProduct(cartMap: Map<String, Any>, eligibleCod: Boolean, userId: String, promoFlag: Boolean) {
        if (eligibleCod) {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT_ELIGIBLE_COD, userId, promoFlag)
        } else {
            sendEnhancedECommerce(1, cartMap, ConstantTransactionAnalytics.EventLabel.CHECKOUT_SUCCESS_PARTIAL_SHOP_AND_PRODUCT, userId, promoFlag)
        }
        flushEnhancedECommerce()
    }

    // PHASE 2
    fun eventClickCheckoutCartClickCheckoutFailed() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_CHECKOUT,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_CHECKOUT,
            ConstantTransactionAnalytics.EventLabel.FAILED
        )
    }

    fun eventClickCouponCartClickGunakanKodeFormGunakanKodePromoAtauKuponSuccess() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COUPON,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
            ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickCouponCartClickGunakanKodeFormGunakanKodePromoAtauKuponFailed() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COUPON,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
            ConstantTransactionAnalytics.EventLabel.FAILED
        )
    }

    fun eventClickCouponCartClickKuponFromGunakanKodePromoAtauKuponSuccess() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COUPON,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
            ConstantTransactionAnalytics.EventLabel.SUCCESS
        )
    }

    fun eventClickCouponCartClickKuponFromGunakanKodePromoAtauKuponFailed() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_COUPON,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON,
            ConstantTransactionAnalytics.EventLabel.FAILED
        )
    }

    fun enhancedEcommerceProductViewWishList(cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.PRODUCT_VIEW,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_PRODUCT,
            ConstantTransactionAnalytics.EventLabel.PRODUCT_WISHLIST
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedEcommerceProductViewLastSeen(cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.PRODUCT_VIEW,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_PRODUCT,
            ConstantTransactionAnalytics.EventLabel.PRODUCT_LAST_SEEN
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedEcommerceClickProductWishListOnEmptyCart(position: String, cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.PRODUCT_CLICK,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_PRODUCT_WISHLIST,
            position
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedEcommerceClickProductWishListOnCartList(position: String, cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.PRODUCT_CLICK,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_PRODUCT_WISHLIST_ON_CART_LIST,
            position
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedEcommerceClickProductLastSeenOnEmptyCart(position: String, cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.PRODUCT_CLICK,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_PRODUCT_LAST_SEEN,
            position
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedEcommerceClickProductLastSeenOnCartList(position: String, cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.PRODUCT_CLICK,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_PRODUCT_LAST_SEEN_ON_CART_LIST,
            position
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun enhancedEcommerceClickProductRecommendationOnEmptyCart(cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.PRODUCT_CLICK,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_PRODUCT_RECOMMENDATION,
            ""
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventViewCartListFinishRender() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_CART,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_CART_LIST
        )
    }

    // impression on user merchant voucher list
    fun eventImpressionUseMerchantVoucher(voucherId: String, ecommerceMap: Map<String, Any>) {
        val eventMap = getGtmData(
            ConstantTransactionAnalytics.EventName.PROMO_VIEW,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.IMPRESSION_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER,
            ""
        )
        eventMap[ConstantTransactionAnalytics.Key.PROMO_ID] = voucherId
        eventMap[ConstantTransactionAnalytics.Key.E_COMMERCE] = ecommerceMap
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    // on success use merchant voucher from list
    fun eventClickUseMerchantVoucherSuccess(promoCode: String, promoId: String, isFromList: Boolean) {
        val eventAction = if (isFromList) ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER else ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER
        val eventMap = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            eventAction,
            ConstantTransactionAnalytics.EventLabel.SUCCESS + " - " + promoCode
        )
        eventMap[ConstantTransactionAnalytics.Key.PROMO_ID] = promoId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    // on error use merchant voucher
    fun eventClickUseMerchantVoucherFailed(errorMessage: String?, promoId: String, isFromList: Boolean) {
        val eventAction = if (isFromList) ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_ON_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER else ConstantTransactionAnalytics.EventAction.CLICK_GUNAKAN_FROM_PILIH_MERCHANT_VOUCHER
        val eventMap = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            eventAction,
            ConstantTransactionAnalytics.EventLabel.ERROR + " - " + errorMessage
        )
        eventMap[ConstantTransactionAnalytics.Key.PROMO_ID] = promoId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    // on merchant voucher click detail
    fun eventClickDetailMerchantVoucher(ecommerceMap: Map<String, Any>, voucherId: String, promoCode: String) {
        val eventMap = getGtmData(
            ConstantTransactionAnalytics.EventName.PROMO_CLICK,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_MERCHANT_VOUCHER_FROM_PILIH_MERCHANT_VOUCHER,
            promoCode
        )
        eventMap[ConstantTransactionAnalytics.Key.PROMO_ID] = voucherId
        eventMap[ConstantTransactionAnalytics.Key.E_COMMERCE] = ecommerceMap
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun enhancedEcommerceViewRecommendationOnCart(cartMap: Map<String, Any>) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.PRODUCT_VIEW,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.IMPRESSION_ON_PRODUCT_RECOMMENDATION,
            ""
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = cartMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventClickMoreLikeThis() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_MORE_LIKE_THIS
        )
    }

    fun eventViewErrorWhenCheckout(errorMessage: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_ERROR_ON_CHECKOUT,
            ConstantTransactionAnalytics.EventLabel.NOT_SUCCESS + " - " + errorMessage
        )
    }

    fun eventClickAddWishlistOnProductRecommendation() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION
        )
    }

    fun eventClickAddWishlistOnProductRecommendationEmptyCart() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_EMPTY_CART
        )
    }

    fun eventClickRemoveWishlistOnProductRecommendation() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION
        )
    }

    fun eventClickRemoveWishlistOnProductRecommendationEmptyCart() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION_EMPTY_CART
        )
    }

    fun sendEnhancedECommerceAddToCart(
        atcMap: Map<String, Any>,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.ADD_TO_CART,
            eventCategory,
            eventAction,
            eventLabel
        )
        dataLayer[ConstantTransactionAnalytics.Key.E_COMMERCE] = atcMap
        sendEnhancedEcommerce(dataLayer)
    }

    fun eventAddWishlistAvailableSection(isEmptyCart: Boolean, productId: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CART,
            if (isEmptyCart) ConstantTransactionAnalytics.EventCategory.EMPTY_CART else ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.ADD_WISHLIST_AVAILABLE_SECTION,
            productId
        )
    }

    fun eventAddWishlistUnavailableSection(isEmptyCart: Boolean, productId: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CART,
            if (isEmptyCart) ConstantTransactionAnalytics.EventCategory.EMPTY_CART else ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.ADD_WISHLIST_UNAVAILABLE_SECTION,
            productId
        )
    }

    fun eventRemoveWishlistUnvailableSection(isEmptyCart: Boolean, productId: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CART,
            if (isEmptyCart) ConstantTransactionAnalytics.EventCategory.EMPTY_CART else ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.REMOVE_WISHLIST_UNAVAILABLE_SECTION,
            productId
        )
    }

    fun eventAddWishlistLastSeenSection(isEmptyCart: Boolean, productId: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CART,
            if (isEmptyCart) ConstantTransactionAnalytics.EventCategory.EMPTY_CART else ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.ADD_WISHLIST_LAST_SEEN,
            productId
        )
    }

    fun eventRemoveWishlistLastSeenSection(isEmptyCart: Boolean, productId: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CART,
            if (isEmptyCart) ConstantTransactionAnalytics.EventCategory.EMPTY_CART else ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.REMOVE_WISHLIST_LAST_SEEN,
            productId
        )
    }

    fun eventAddWishlistWishlistsSection(isEmptyCart: Boolean, productId: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CART,
            if (isEmptyCart) ConstantTransactionAnalytics.EventCategory.EMPTY_CART else ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.ADD_WISHLIST_WISHLIST,
            productId
        )
    }

    fun eventRemoveWishlistWishlistsSection(isEmptyCart: Boolean, productId: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CART,
            if (isEmptyCart) ConstantTransactionAnalytics.EventCategory.EMPTY_CART else ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.REMOVE_WISHLIST_WISHLIST,
            productId
        )
    }

    fun eventClickBrowseButtonOnTickerProductContainTobacco() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_BROWSE_BUTTON_ON_TICKER_PRODUCT_CONTAIN_TOBACCO
        )
    }

    fun eventViewTickerProductContainTobacco() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.VIEW_ATC_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_TICKER_PRODUCT_CONTAIN_TOBACCO
        )
    }

    fun eventClickHapusButtonOnProductContainTobacco() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_BUTTON_ON_PRODUCT_CONTAIN_TOBACCO
        )
    }

    fun eventClickTrashIconButtonOnProductContainTobacco() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_TRASH_ICON_BUTTON_ON_PRODUCT_CONTAIN_TOBACCO
        )
    }

    // Cart Revamp
    fun eventViewRemainingStockInfo(userId: String, productId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_ATC_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_REMAINING_STOCK_INFO,
            productId
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventViewInformationLabelInProductCard(userId: String, productId: String, informationLabel: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_ATC_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_INFORMATION_LABEL_IN_PRODUCT_CARD,
            "$productId - $informationLabel"
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickDetailTagihan(userId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_DETAIL_TAGIHAN,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickMoveToWishlistOnAvailableSection(userId: String, productId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.ADD_WISHLIST_CART_LOGIN,
            "$productId - available section"
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickMoveToWishlistOnUnavailableSection(userId: String, productId: String, errorType: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.ADD_WISHLIST_CART_LOGIN,
            "$productId - $errorType - unavailable section"
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickDeleteProductOnUnavailableSection(userId: String, productId: String, errorType: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_DELETE_PRODUCT_ON_UNAVAILABLE_SECTION,
            "$productId - $errorType"
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickSeeOtherProductOnUnavailableSection(userId: String, productId: String, errorType: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_LIHAT_PRODUK_SERUPA_ON_UNAVAILABLE_SECTION,
            "$productId - $errorType"
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickCheckoutMelaluiBrowserOnUnavailableSection(userId: String, productId: String, errorType: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_CHECKOUT_MELALUI_BROWSER_ON_UNAVAILABLE_SECTION,
            "$productId - $errorType"
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickDeleteAllUnavailableProduct(userId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_DELETE_ALL_UNAVAILABLE_PRODUCT,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickAccordionButtonOnUnavailableProduct(userId: String, buttonWording: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ACCORDION_ON_UNAVAILABLE_PRODUCT.replace("%s", buttonWording.lowercase(Locale.getDefault())),
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickUndoAfterDeleteProduct(userId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_UNDO_AFTER_DELETE_PRODUCT,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventViewErrorPageWhenLoadCart(userId: String, errorType: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_ATC_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_ERROR_PAGE_WHEN_LOAD_CART,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickWishlistIcon(userId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_WISHLIST,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_WISHLIST_ICON_IN_CART_PAGE,
            ""
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickRemoveWishlist(userId: String, productId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_WISHLIST,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.REMOVE_WISHLIST_CART_LOGIN,
            "$productId - wishlist section"
        )
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun eventClickFollowShop(userId: String, errorType: String, shopId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_FOLLOW_SHOP_ON_UNAVAILABLE_SECTION,
            "$errorType - $shopId"
        )
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.USER_ID] = userId
        sendGeneralEvent(gtmData)
    }

    fun eventClickTopNavMenuNavToolbar(userId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_NAVIGATION_DRAWER,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_GLOBAL_MENU_NAV,
            ""
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.USER_ID] = userId
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE
        gtmData[ExtraKey.PAGE_TYPE] = ""
        gtmData[ExtraKey.PAGE_PATH] = ""
        sendGeneralEvent(gtmData)
    }

    // Global checkbox resurrection
    fun eventCheckUncheckGlobalCheckbox(isCheck: Boolean) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_CHECKOUT,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_PILIH_SEMUA_PRODUK,
            if (isCheck) ConstantTransactionAnalytics.EventLabel.CHECKLIST else ConstantTransactionAnalytics.EventLabel.UN_CHECKLIST
        )
    }

    fun eventClickGlobalDelete() {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_HAPUS_ON_TOP_RIGHT_CORNER
        )
    }

    // TokoNow
    fun eventViewToasterErrorInCartPage(errorMessage: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_CART_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_TOASTER_ERROR_IN_CART_PAGE,
            errorMessage
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    fun eventLoadCartWithUnavailableProduct(shopId: String, errorGrouping: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_CART_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.LOAD_CART_WITH_UNAVAILABLE_PRODUCT,
            "$shopId - $errorGrouping"
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    fun eventClickCollapsedProductImage(shopId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_CART,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ON_PRODUCT_IMAGE_ON_COLLAPSE_VIEW,
            shopId
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    fun eventClickLihatSelengkapnyaOnNowProduct(shopId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_CART,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_LIHAT_SELENGKAPNYA_FOR_NOW_PRODUCT,
            shopId
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    fun eventClickLihatOnPlusLainnyaOnNowProduct(shopId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_CART,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_LIHAT_ON_PLUS_LAINNYA_ON_NOW_PRODUCT,
            shopId
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    // Bundling
    // TrackerId: 21780
    fun eventClickUbahInProductBundlingPackageProductCard(bundlingId: String, bundlingType: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_UBAH_IN_PRODUCT_BUNDLING_PACKAGE_PRODUCT_CARD,
            "bundling_id:$bundlingId\nbundling_type:$bundlingType"
        )
    }

    // TrackerId: 21781
    fun eventClickMoreLikeThisOnBundleProduct(bundlingId: String, bundlingType: String) {
        sendGeneralEvent(
            ConstantTransactionAnalytics.EventName.CLICK_ATC,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_LIHAT_BARANG_SERUPA_FOR_UNAVAILABLE_BUNDLE_PACKAGE,
            "bundling_id:$bundlingId\nbundling_type:$bundlingType"
        )
    }

    // Cart Bundling Bottom Sheet
    // TrackerId: 41253
    fun eventViewCartBundlingBottomSheetBundle(
        userId: String,
        bundleId: String,
        bundleType: String
    ) {
        val bundle = bundleOf(
            ExtraKey.EVENT to ConstantTransactionAnalytics.EventName.VIEW_ITEM,
            ExtraKey.EVENT_CATEGORY to ConstantTransactionAnalytics.EventCategory.CART,
            ExtraKey.EVENT_ACTION to ConstantTransactionAnalytics.EventAction.IMPRESSION_BUNDLING_COMPONENT,
            ExtraKey.EVENT_LABEL to "$bundleId - $bundleType",
            ExtraKey.CURRENT_SITE to ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE,
            ExtraKey.BUSINESS_UNIT to ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM,
            ExtraKey.TRACKER_ID to ConstantTransactionAnalytics.TrackerId.IMPRESSION_CART_BUNDLING_BOTTOM_SHEET_BUNDLE,
            ExtraKey.USER_ID to userId
        )
        sendEnhancedEcommerce(ConstantTransactionAnalytics.EventName.VIEW_ITEM, bundle)
    }

    // TrackerId: 41254
    fun eventClickCartBundlingBottomSheetBundleWidgetAction(
        userId: String,
        bundleId: String,
        bundleType: String,
        promotions: List<Bundle>
    ) {
        val bundle = bundleOf(
            ExtraKey.EVENT to ConstantTransactionAnalytics.EventName.SELECT_CONTENT,
            ExtraKey.EVENT_CATEGORY to ConstantTransactionAnalytics.EventCategory.CART,
            ExtraKey.EVENT_ACTION to ConstantTransactionAnalytics.EventAction.CLICK_PRODUCT_BUNDLING,
            ExtraKey.EVENT_LABEL to "$bundleId - $bundleType",
            ExtraKey.CURRENT_SITE to ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE,
            ExtraKey.BUSINESS_UNIT to ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM,
            ExtraKey.TRACKER_ID to ConstantTransactionAnalytics.TrackerId.CLICK_CART_BUNDLING_BOTTOM_SHEET_BUNDLE_WIDGET_ACTION,
            ExtraKey.PROMOTIONS to promotions,
            ExtraKey.USER_ID to userId
        )
        sendEnhancedEcommerce(ConstantTransactionAnalytics.EventName.SELECT_CONTENT, bundle)
    }

    // Cart Shop Group Ticker
    // TrackerId: 41252
    fun eventClickCartShopGroupTickerForBundleCrossSell(tickerText: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_PP,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_BUNDLING_WIDGET,
            tickerText
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.TRACKER_ID] = ConstantTransactionAnalytics.TrackerId.CLICK_CART_SHOP_GROUP_TICKER_BUNDLE_CROSS_SELL
        sendGeneralEvent(gtmData)
    }

    // Bo Affordability
    // TrackerId: 26977
    fun eventClickArrowInBoTickerToReachShopPage(cartIds: String, shopId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_PP,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ARROW_IN_BO_TICKER_TO_REACH_SHOP_PAGE,
            "$cartIds - $shopId"
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    // TrackerId: 27029
    fun eventViewBoTickerWording(isAfford: Boolean, cartIds: String, shopId: String) {
        val fulfillLabel = if (isAfford) ConstantTransactionAnalytics.EventLabel.BO_FULFILL else ConstantTransactionAnalytics.EventLabel.BO_UNFULFILL
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_BO_TICKER_WORDING,
            "$fulfillLabel - $cartIds - $shopId"
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    // gifting
    // tracker id : 28310
    fun eventViewAddOnsWidget(productId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_ADD_ONS_WIDGET,
            productId
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    // tracker id : 28311
    fun eventClickAddOnsWidget(productId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_PP,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ADD_ONS_DETAIL,
            productId
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    fun eventViewGotoplusTicker() {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_GOTOPLUS_TICKER,
            ""
        )
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ExtraKey.TRACKER_ID] = ConstantTransactionAnalytics.TrackerId.VIEW_GOTOPLUS_TICKER_CART
        sendGeneralEvent(gtmData)
    }

    // addons product service
    // tracker id : 45176
    fun eventViewAddOnsProductWidgetCart(addOnType: Int, productId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.VIEW_ADD_ONS_PRODUCT_WIDGET,
            "$addOnType - $productId"
        )
        gtmData[ExtraKey.TRACKER_ID] = ConstantTransactionAnalytics.TrackerId.VIEW_ADDONS_PRODUCT_WIDGET_CART
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }

    // tracker id : 45177
    fun eventClickAddOnsWidgetCart(addOnType: Int, productId: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_PP,
            ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.EventAction.CLICK_ADD_ONS_PRODUCT_WIDGET,
            "$addOnType - $productId"
        )
        gtmData[ExtraKey.TRACKER_ID] = ConstantTransactionAnalytics.TrackerId.CLICK_ADDONS_PRODUCT_WIDGET_CART
        gtmData[ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        sendGeneralEvent(gtmData)
    }
}
