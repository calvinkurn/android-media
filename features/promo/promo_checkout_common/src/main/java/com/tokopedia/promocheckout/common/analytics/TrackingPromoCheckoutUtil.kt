package com.tokopedia.promocheckout.common.analytics

import com.tokopedia.track.TrackApp

class TrackingPromoCheckoutUtil {

    fun cartClickUseTickerPromoOrCoupon(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_USE_PROMO_OR_COUPON, "")
    }

    fun cartImpressionTicker(promoCode : String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_ATC, CART, VIEW_ON_TICKER, promoCode)
    }

    fun cartClickTicker(promoCode : String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_ON_TICKER, promoCode)
    }

    fun cartClickUsePromoCodeSuccess(promoCode: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_USE_PROMO_CODE, SUCCESS + promoCode)
    }

    fun cartClickUsePromoCodeFailed(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_USE_PROMO_CODE, FAILED )
    }

    fun cartImpressionCoupon(promoCode: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_ATC, CART, VIEW_KUPON, promoCode)
    }

    fun cartClickCoupon(promoCode: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_KUPON, promoCode)
    }

    fun cartClickUsePromoCouponSuccess(promoCode : String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_GUNAKAN_KUPON, SUCCESS + promoCode)
    }

    fun cartClickUsePromoCouponFailed(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_GUNAKAN_KUPON, ERROR_INELIGIBLE)
    }

    fun cartClickCancelPromoCoupon(promoCode: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_ATC, CART, CLICK_CANCEL_USE_COUPON, promoCode)
    }

    fun checkoutClickUseTickerPromoOrCoupon(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_OR_COUPON, "")
    }

    fun checkoutImpressionTicker(message : String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_COURIER, COURIER_SELECTION, VIEW_ON_TICKER, message)
    }

    fun checkoutClickTicker(message : String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_ON_TICKER, message)
    }

    fun checkoutClickUsePromoCodeSuccess(promoCode: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_CODE, SUCCESS + promoCode)
    }

    fun checkoutClickUsePromoCodeFailed(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_CODE, FAILED )
    }

    fun checkoutImpressionCoupon(promoCode: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_COURIER, COURIER_SELECTION, VIEW_KUPON, promoCode)
    }

    fun checkoutClickCoupon(promoCode: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_KUPON, promoCode)
    }

    fun checkoutClickUsePromoCouponSuccess(promoCode : String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_GUNAKAN_KUPON, SUCCESS + promoCode)
    }

    fun checkoutClickUsePromoCouponFailed(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_GUNAKAN_KUPON, ERROR_INELIGIBLE)
    }

    fun checkoutClickCancelPromoCoupon(promoCode: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_COURIER, COURIER_SELECTION, CLICK_CANCEL_USE_COUPON, promoCode)
    }
}