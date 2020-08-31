package com.tokopedia.gamification.giftbox.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object GtmEvents {

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    //3
    fun viewGiftBoxPage(campaignSlug:String, userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] = campaignSlug
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    fun emptyBoxImpression(userId: String?){
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  GiftBoxLabel.ALREADY_OPENED
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //5,14
    fun clickBackButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_BACK_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  GiftBoxLabel.MAIN_PAGE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //6
    fun clickShareButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SHARE_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //7
    fun clickGiftBox(campaignSlug: String,userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_GIFT_BOX
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  campaignSlug
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //8
    fun viewRewards(catalogId: String,userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  "coupon - $catalogId"
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //9
    fun viewRewardsPoints(pointsAmount:String,userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  "points - $pointsAmount"
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //10
    fun clickClaimButton(buttonTitle:String,userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_CLAIM_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  buttonTitle
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //11
    fun clickReminderButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_REMINDER_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //16
    fun clickExitButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] =  GiftBoxLabel.CONNECTION_ERROR
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //17
    fun clickTryAgainButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_TRY_AGAIN_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //18
    fun clickSettingsButton(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SETTING_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

    //19
    fun clickToaster(userId: String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_DAILY
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_TOASTER_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        getTracker().sendGeneralEvent(map)
    }

}