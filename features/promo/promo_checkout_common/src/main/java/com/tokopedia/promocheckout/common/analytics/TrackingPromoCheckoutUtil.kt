package com.tokopedia.promocheckout.common.analytics

class TrackingPromoCheckoutUtil(val trackingPromoCheckoutRouter : TrackingPromoCheckoutRouter?) {

    fun cartClickUseTickerPromoOrCoupon(){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_ATC, CART, CLICK_USE_PROMO_OR_COUPON, "")
    }

    fun cartImpressionTicker(promoCode : String){
        trackingPromoCheckoutRouter?.sendEventTracking(VIEW_ATC, CART, VIEW_ON_TICKER, promoCode)
    }

    fun cartClickTicker(promoCode : String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_ATC, CART, CLICK_ON_TICKER, promoCode)
    }

    fun cartClickUsePromoCodeSuccess(promoCode: String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_ATC, CART, CLICK_USE_PROMO_CODE, SUCCESS + promoCode)
    }

    fun cartClickUsePromoCodeFailed(){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_ATC, CART, CLICK_USE_PROMO_CODE, FAILED )
    }

    fun cartImpressionCoupon(promoCode: String){
        trackingPromoCheckoutRouter?.sendEventTracking(VIEW_ATC, CART, VIEW_KUPON, promoCode)
    }

    fun cartClickCoupon(promoCode: String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_ATC, CART, CLICK_KUPON, promoCode)
    }

    fun cartClickUsePromoCouponSuccess(promoCode : String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_ATC, CART, CLICK_GUNAKAN_KUPON, SUCCESS + promoCode)
    }

    fun cartClickUsePromoCouponFailed(){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_ATC, CART, CLICK_GUNAKAN_KUPON, ERROR_INELIGIBLE)
    }

    fun cartClickCancelPromoCoupon(promoCode: String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_ATC, CART, CLICK_CANCEL_USE_COUPON, promoCode)
    }

    fun checkoutClickUseTickerPromoOrCoupon(){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_OR_COUPON, "")
    }

    fun checkoutImpressionTicker(message : String){
        trackingPromoCheckoutRouter?.sendEventTracking(VIEW_COURIER, COURIER_SELECTION, VIEW_ON_TICKER, message)
    }

    fun checkoutClickTicker(message : String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_COURIER, COURIER_SELECTION, CLICK_ON_TICKER, message)
    }

    fun checkoutClickUsePromoCodeSuccess(promoCode: String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_CODE, SUCCESS + promoCode)
    }

    fun checkoutClickUsePromoCodeFailed(){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_COURIER, COURIER_SELECTION, CLICK_USE_PROMO_CODE, FAILED )
    }

    fun checkoutImpressionCoupon(promoCode: String){
        trackingPromoCheckoutRouter?.sendEventTracking(VIEW_COURIER, COURIER_SELECTION, VIEW_KUPON, promoCode)
    }

    fun checkoutClickCoupon(promoCode: String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_COURIER, COURIER_SELECTION, CLICK_KUPON, promoCode)
    }

    fun checkoutClickUsePromoCouponSuccess(promoCode : String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_COURIER, COURIER_SELECTION, CLICK_GUNAKAN_KUPON, SUCCESS + promoCode)
    }

    fun checkoutClickUsePromoCouponFailed(){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_COURIER, COURIER_SELECTION, CLICK_GUNAKAN_KUPON, ERROR_INELIGIBLE)
    }

    fun checkoutClickCancelPromoCoupon(promoCode: String){
        trackingPromoCheckoutRouter?.sendEventTracking(CLICK_COURIER, COURIER_SELECTION, CLICK_CANCEL_USE_COUPON, promoCode)
    }
}