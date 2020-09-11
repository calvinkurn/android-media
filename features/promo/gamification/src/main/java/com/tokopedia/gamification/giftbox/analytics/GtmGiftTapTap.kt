package com.tokopedia.gamification.giftbox.analytics

object GtmGiftTapTap {

    //3
    fun impressionGiftBox(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.LOGIN
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //4
    fun clickMainBackButton(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_BACK_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.MAIN_PAGE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //5
    fun clickShareButton(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SHARE_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //6
    fun clickGiftBox(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_GIFT_BOX
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //7
    fun viewRewards(rewardName: String,userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS
        map[GiftBoxTrackerConstants.EVENT_LABEL] = rewardName
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //8
    fun clickContinueButton(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_CONTINUE_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //9
    fun clickExitButton(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.EARLY_END
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //10
    fun viewNoInternetError(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_ERROR
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.INTERNET_CONN_ERROR
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //11
    fun viewToastError(userId:String?, errorMessage:String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_ERROR
        map[GiftBoxTrackerConstants.EVENT_LABEL] = errorMessage
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //12
    fun clickTryAgain(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_TRY_AGAIN_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //13
    fun clickSettingsButton(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SETTING_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //14
    fun viewRewardSummary(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS_SUMMARY
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //15
    fun clickUseCoupon(catalogId: String, userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_USE_COUPON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = catalogId
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //16
    fun clickCheckRewards(userId:String?) {

        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_CHECK_REWARDS
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //17
    fun clickExitButtonReward(userId:String?) {

        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.REWARDS_SUMMARY
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //24
    fun campaignOver(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.CAMPAIGN_OVER
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //25
    fun clickHomePageButton(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_HOMEPAGE_BUTTON
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //26
    fun viewNoRewardsPage(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_NO_REWARDS_PAGE
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //27
    fun clickExitNoReward(userId:String?) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.NO_REWARD
        userId?.let {
            map[GiftBoxTrackerConstants.USER_ID] = userId
        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }
}