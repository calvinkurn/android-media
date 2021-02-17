package com.tokopedia.mvcwidget

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

/*
* Confusing Trackers
* 2,3, 19, 20
* */

object Tracker {

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    object Constants {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val USER_ID = "userId"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
    }

    object Event {
        const val CLICK_PDP = "clickPDP"
        const val PROMO_VIEW = "promoView"
    }

    object Category {
        const val MERCHANT_VOUCHER = "merchant voucher"
        const val GIFT_BOX_60 = "gift box 60"
    }

    object Action {
        const val CLICK_COUPON_ENTRY_POINT = "click coupon entry point"
        const val VIEW_FOLLOW_WIDGET = "view follow widget"
    }

    object Label {
        const val PDP_VIEW = "pdp view"
    }

    fun fillCommonItems(map:MutableMap<String,Any>,userId:String?){
        map[Constants.BUSINESS_UNIT] = ""
        map[Constants.CURRENT_SITE] = ""
        userId?.let {
            map[Constants.USER_ID] = userId
        }
    }

    //1 Pdp
    //13 Shop
    fun userClickEntryPoints(shopId: String, userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[Constants.EVENT] = Event.CLICK_PDP
        map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
        map[Constants.EVENT_ACTION] = Action.CLICK_COUPON_ENTRY_POINT
        map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //4 - pdp - it can appear multiple times because we hit api and reload page. Do we need to again send this event
    //21 - shop
    fun followButtonAppear(shopId: String,userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[Constants.EVENT] = Event.PROMO_VIEW
        map[Constants.EVENT_CATEGORY] = Category.MERCHANT_VOUCHER
        map[Constants.EVENT_ACTION] = Action.VIEW_FOLLOW_WIDGET
        map[Constants.EVENT_LABEL] = "${Label.PDP_VIEW}-$shopId"
        fillCommonItems(map, userId)
        getTracker().sendGeneralEvent(map)
    }

    //5, 22
    fun clickFollowButton() {

    }

    //6, 23
    fun successToastAfterFollowCta() {

    }

    //7 and 11 are similar - pdp
    // 24, 28 - shop
    fun couponsArePresent() {

    }

    //8, 25
    fun jadiMemberAppear() {

    }

    //9, 26
    fun clickJadiMemberButton() {

    }

    //10, 27
    fun successToastAfterJadiMemberCta() {

    }

    //11 and 7 are similar - pdp
    // 24 , 28 - shop
    fun buyer_eligible_for_membership_coupon_and_see_the_coupon_on_buttomsheet() {

    }

    //12, 29
    fun errorToastAfterFollowCta() {

    }

    //13, 30
    fun errorToastAfterJadiMemberCta() {

    }

    //14, 31
    fun clickCekButton() {

    }

    //15, 32
    fun viewTokomemberBottomSheet() {

    }

    //16, 33
    fun clickJadiMemberFromTokomemberBottomSheet() {

    }

    //34 - shop
    fun closeBottomSheet() {

    }

    //Outside MVC - entryPoint
    //35,36,37,38,39,40,41,42,43,44,45
}

