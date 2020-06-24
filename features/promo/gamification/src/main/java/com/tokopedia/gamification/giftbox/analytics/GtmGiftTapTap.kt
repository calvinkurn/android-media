package com.tokopedia.gamification.giftbox.analytics

object GtmGiftTapTap {

    //3
    fun impressionGiftBox() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.LOGIN

        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //4
    fun clickMainBackButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_BACK_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.MAIN_PAGE

        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //5
    fun clickShareButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SHARE_BUTTON
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //6
    fun clickGiftBox() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_GIFT_BOX
//        userId?.let {
//            map[GiftBoxTrackerConstants.USER_ID] = userId
//        }
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //7 todo Add label
    fun viewRewards(rewardName: String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS
        map[GiftBoxTrackerConstants.EVENT_LABEL] = rewardName
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //8
    fun clickContinueButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_CONTINUE_BUTTON
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //9
    fun clickExitButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.EARLY_END
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //10
    fun viewError() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_ERROR
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //11
    fun clickTryAgain() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_TRY_AGAIN_BUTTON
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //12
    fun clickSettingsButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_SETTING_BUTTON
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //13
    fun viewRewardSummary() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_REWARDS_SUMMARY
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //14
    fun clickUseCoupon(catalogId: String) {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_USE_COUPON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = catalogId
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //15
    fun clickCheckRewards() {

        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_CHECK_REWARDS
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //16
    fun clickExitButtonReward() {

        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.REWARDS_SUMMARY
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //23
    fun campaignOver() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_GIFT_BOX_PAGE
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.CAMPAIGN_OVER
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //24
    fun clickHomePageButton() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_HOMEPAGE_BUTTON
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //25
    fun viewNoRewardsPage() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.VIEW_PRESENT_IRIS
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.VIEW_NO_REWARDS_PAGE
        GtmEvents.getTracker().sendGeneralEvent(map)
    }

    //26
    fun clickExitNoReward() {
        val map = mutableMapOf<String, Any>()
        map[GiftBoxTrackerConstants.EVENT] = GiftBoxEvent.CLICK_PRESENT
        map[GiftBoxTrackerConstants.EVENT_CATEGORY] = GiftBoxCategory.GIFT_BOX_60
        map[GiftBoxTrackerConstants.EVENT_ACTION] = GiftBoxAction.CLICK_EXIT_BUTTON
        map[GiftBoxTrackerConstants.EVENT_LABEL] = GiftBoxLabel.NO_REWARD
        GtmEvents.getTracker().sendGeneralEvent(map)
    }
}