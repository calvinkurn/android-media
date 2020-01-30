package com.tokopedia.promotionstarget.presentation

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object TargetedPromotionAnalytics {

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"


    private const val TARGETED_PROMOTION = "targeted promotion"
    private const val VIEW_TARGETED_PROMO_IRIS = "viewTargetedPromoIris"
    private const val CLICK_TARGETED_PROMO = "clickTargetedPromo"
    private const val PROMO_VIEW = "promoView"

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    //3, 17
    fun viewCoupon(catalogId:String, isLoogedIn:Boolean) {
        val map = mutableMapOf<String, Any>()

        val isLoggedInText = if (isLoogedIn) "login" else "nonlogin"
        map[KEY_EVENT] = PROMO_VIEW
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "view coupon - $isLoggedInText"
        map[KEY_EVENT_LABEL] = catalogId
        getTracker().sendGeneralEvent(map)
    }

    //4,18
    fun clickClaimCoupon(catalogId:String, isLoogedIn:Boolean) {
        val map = mutableMapOf<String, Any>()

        val isLoggedInText = if (isLoogedIn) "login" else "nonlogin"
        map[KEY_EVENT] = CLICK_TARGETED_PROMO
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click claim coupon - $isLoggedInText"
        map[KEY_EVENT_LABEL] = catalogId
        getTracker().sendGeneralEvent(map)
    }

    //5
    fun viewClaimSuccess() {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = VIEW_TARGETED_PROMO_IRIS
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "view claim succeed"
        getTracker().sendGeneralEvent(map)
    }

    //6
    fun userClickCheckMyCoupon() {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = CLICK_TARGETED_PROMO
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click cek Kupon saya"
        getTracker().sendGeneralEvent(map)
    }

    //7
    fun claimSucceedPopup(){
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = VIEW_TARGETED_PROMO_IRIS
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click cek Kupon saya"
        getTracker().sendGeneralEvent(map)
    }

    //9
    fun couponClaimedLastOccasion(label:String){
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = VIEW_TARGETED_PROMO_IRIS
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "view error"
        map[KEY_EVENT_LABEL] = label
        getTracker().sendGeneralEvent(map)
    }

    //13 todo Rahul
    //Picture 5 and 6
    //https://tokopedia.atlassian.net/wiki/spaces/G/pages/552405246/PopGratification
    //https://docs.google.com/spreadsheets/d/17UFj2siq74F-tYN5abYlARx7yNNDQxDNreY8YazdzRA/edit#gid=1542726793
    fun goHomePage(){
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = CLICK_TARGETED_PROMO
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click go to homepage"
        getTracker().sendGeneralEvent(map)
    }

    //15
    fun tryAgain(){
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = CLICK_TARGETED_PROMO
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click try again"
        getTracker().sendGeneralEvent(map)
    }


}