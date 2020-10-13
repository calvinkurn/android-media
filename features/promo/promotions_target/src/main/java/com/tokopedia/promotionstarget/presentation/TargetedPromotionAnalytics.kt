package com.tokopedia.promotionstarget.presentation

import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

const val KEY_EVENT = "event"
const val KEY_EVENT_CATEGORY = "eventCategory"
const val KEY_EVENT_ACTION = "eventAction"
const val KEY_EVENT_LABEL = "eventLabel"
const val KEY_USER_ID = "userId"

object TargetedPromotionAnalytics {

    private const val TARGETED_PROMOTION = "targeted promotion"
    private const val VIEW_TARGETED_PROMO_IRIS = "viewTargetedPromoIris"
    private const val CLICK_TARGETED_PROMO = "clickTargetedPromo"
    private const val PROMO_VIEW = "promoView"
    private const val LOGIN = "login"
    private const val NON_LOGIN = "non login"

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    //3, 17
    fun viewCoupon(catalogId: String, isLoogedIn: Boolean, userId:String) {
        val map = mutableMapOf<String, Any>()

        val isLoggedInText = if (isLoogedIn) LOGIN else NON_LOGIN
        map[KEY_EVENT] = PROMO_VIEW
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "view coupon - $isLoggedInText"
        map[KEY_EVENT_LABEL] = catalogId
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    //4,18
    fun clickClaimCoupon(catalogId: String, isLoogedIn: Boolean, buttonText: String, userId: String) {
        val map = mutableMapOf<String, Any>()

        val isLoggedInText = if (isLoogedIn) LOGIN else NON_LOGIN
        map[KEY_EVENT] = CLICK_TARGETED_PROMO
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click $buttonText - $isLoggedInText"
        map[KEY_EVENT_LABEL] = catalogId
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    //5
    fun viewClaimSuccess(screenTitle: String?, userId: String) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = VIEW_TARGETED_PROMO_IRIS
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "view claim succeed"
        screenTitle?.let {
            map[KEY_EVENT_LABEL] = it
        }
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    //6
    fun userClickCheckMyCoupon(buttonText: String, userId: String) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = CLICK_TARGETED_PROMO
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click cek Kupon saya"
        map[KEY_EVENT_LABEL] = buttonText
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    //7
    fun claimSucceedPopup(label: String, userId: String) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = VIEW_TARGETED_PROMO_IRIS
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "auto-apply case, when user has dismissed the dialog/pop-up after claiming coupon"
        map[KEY_EVENT_LABEL] = label
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    //9
    fun couponClaimedLastOccasion(label: String, userId: String) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = VIEW_TARGETED_PROMO_IRIS
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "view error"
        map[KEY_EVENT_LABEL] = label
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    //13
    fun performButtonAction(label: String, userId: String) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = CLICK_TARGETED_PROMO
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click"
        map[KEY_EVENT_LABEL] = label
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    //15
    fun tryAgain(userId: String) {
        val map = mutableMapOf<String, Any>()

        map[KEY_EVENT] = CLICK_TARGETED_PROMO
        map[KEY_EVENT_CATEGORY] = TARGETED_PROMOTION
        map[KEY_EVENT_ACTION] = "click try again"
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }


}