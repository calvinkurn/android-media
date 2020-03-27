package com.tokopedia.gamification.giftbox.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object GtmEvents {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    //3
    fun viewGiftBoxPage(campaignSlug:String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] = campaignSlug
        getTracker().sendGeneralEvent(map)
    }

    //4,13
    fun clickBackButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_BACK_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  GiftBoxLabel.MAIN_PAGE
        getTracker().sendGeneralEvent(map)
    }

    //5
    fun clickShareButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SHARE_BUTTON

        getTracker().sendGeneralEvent(map)
    }

    //6
    fun clickGiftBox(campaignSlug: String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_GIFT_BOX
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  campaignSlug
        getTracker().sendGeneralEvent(map)
    }

    //7
    fun viewRewards(catalogId: String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  "coupon - $catalogId"

        getTracker().sendGeneralEvent(map)
    }

    //8
    fun viewRewardsPoints(pointsAmount:String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  "points - $pointsAmount"

        getTracker().sendGeneralEvent(map)
    }

    //9
    fun clickClaimButton(buttonTitle:String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_CLAIM_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  buttonTitle

        getTracker().sendGeneralEvent(map)
    }

    //10
    fun clickReminderButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_REMINDER_BUTTON

        getTracker().sendGeneralEvent(map)
    }

    //15
    fun clickExitButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  GiftBoxLabel.CONNECTION_ERROR

        getTracker().sendGeneralEvent(map)
    }

    //16
    fun clickTryAgainButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_TRY_AGAIN_BUTTON

        getTracker().sendGeneralEvent(map)
    }

    //17
    fun clickSettingsButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SETTING_BUTTON

        getTracker().sendGeneralEvent(map)
    }

    //18
    fun clickToaster() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_TOASTER_BUTTON

        getTracker().sendGeneralEvent(map)
    }

}