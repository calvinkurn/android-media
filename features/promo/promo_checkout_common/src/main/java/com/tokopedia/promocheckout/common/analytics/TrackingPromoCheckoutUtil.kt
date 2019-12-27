package com.tokopedia.promocheckout.common.analytics

import com.google.gson.Gson
import com.tokopedia.track.TrackApp
import timber.log.Timber

class TrackingPromoCheckoutUtil {

    private fun createMapEvent(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to event,
                KEY_CATEGORY to category,
                KEY_ACTION to action,
                KEY_LABEL to label
        )
    }

    fun cartClickUseTickerPromoOrCoupon() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_USE_PROMO_OR_COUPON, "")
    }

    fun cartImpressionTicker(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_ATC, CART, VIEW_ON_TICKER, promoCode)
    }

    fun cartClickTicker(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_ON_TICKER, promoCode)
    }

    fun cartClickUsePromoCodeSuccess(promoCode: String) {
        val eventMap = createMapEvent(CLICK_ATC, CART, CLICK_USE_PROMO_FROM_SELECT_MVC, SUCCESS + promoCode)
        eventMap[KEY_PROMO_ID] = ""

        Timber.d("cartClickUsePromoCodeSuccess : %s", Gson().toJsonTree(eventMap))

        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun cartClickUsePromoCodeFailed(e: Throwable) {
        val eventMap = createMapEvent(CLICK_ATC, CART, CLICK_USE_PROMO_FROM_SELECT_MVC, FAILED + e.message)
        //eventMap[KEY_PROMO_ID] = promoCode
        Timber.d("cartClickUsePromoCodeSuccess : %s", Gson().toJsonTree(eventMap))

        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_USE_PROMO_CODE, FAILED)
    }

    fun cartImpressionCoupon(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_ATC, CART, VIEW_KUPON, promoCode)
    }

    fun cartClickCoupon(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_KUPON, promoCode)
    }

    fun cartClickUsePromoCouponSuccess(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_GUNAKAN_KUPON, SUCCESS + promoCode)
    }

    fun cartClickUsePromoCouponFailed() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_GUNAKAN_KUPON, ERROR_INELIGIBLE)
    }

    fun cartClickCancelPromoCoupon(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_CANCEL_USE_COUPON, promoCode)
    }

    fun checkoutClickUseTickerPromoOrCoupon() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_OR_COUPON, "")
    }

    fun checkoutImpressionTicker(message: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_COURIER, COURIER_SELECTION, VIEW_ON_TICKER, message)
    }

    fun checkoutClickTicker(message: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_ON_TICKER, message)
    }

    fun checkoutClickUsePromoCodeSuccess(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_CODE, SUCCESS + promoCode)
    }

    fun checkoutClickUsePromoCodeFailed() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_CODE, FAILED)
    }

    fun checkoutImpressionCoupon(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_COURIER, COURIER_SELECTION, VIEW_KUPON, promoCode)
    }

    fun checkoutClickCoupon(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_KUPON, promoCode)
    }

    fun checkoutClickUsePromoCouponSuccess(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_GUNAKAN_KUPON, SUCCESS + promoCode)
    }

    fun checkoutClickUsePromoCouponFailed() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_GUNAKAN_KUPON, ERROR_INELIGIBLE)
    }

    fun checkoutClickCancelPromoCoupon(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_CANCEL_USE_COUPON, promoCode)
    }
}