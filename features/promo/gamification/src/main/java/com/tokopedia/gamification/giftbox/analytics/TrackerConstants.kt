package com.tokopedia.gamification.giftbox.analytics

object GiftBoxTrackerConstants {
    const val EVENT = "event"
    const val EVENT_CATEGORY = "eventCategory"
    const val EVENT_ACTION = "eventAction"
    const val EVENT_LABEL = "eventLabel"
    const val USER_ID = "userId"
}

object GiftBoxEvent {
    const val CLICK_PRESENT = "clickPresent"
    const val VIEW_PRESENT_IRIS = "viewPresentIris"
}

object GiftBoxCategory {
    const val GIFT_BOX_DAILY = "gift box daily"
    const val GIFT_BOX_60 = "gift box 60"
}

object GiftBoxAction {
    const val VIEW_GIFT_BOX_PAGE = "view gift box page"
    const val VIEW_NO_REWARDS_PAGE = "view no rewards page"
    const val CLICK_BACK_BUTTON = "click back button"
    const val CLICK_SHARE_BUTTON = "click share button"
    const val CLICK_GIFT_BOX = "click gift box"
    const val VIEW_REWARDS = "view rewards"
    const val VIEW_ERROR = "view error"
    const val VIEW_REWARDS_SUMMARY = "view rewards summary"
    const val CLICK_CLAIM_BUTTON = "click claim button"
    const val CLICK_REMINDER_BUTTON = "click reminder button"
    const val CLICK_TOASTER_BUTTON = "click toaster button"
    const val CLICK_EXIT_BUTTON = "click exit button"
    const val CLICK_TRY_AGAIN_BUTTON = "click try again button"
    const val CLICK_CONTINUE_BUTTON = "click continue button"
    const val CLICK_SETTING_BUTTON = "click setting button"
    const val CLICK_USE_COUPON = "click use coupon"
    const val CLICK_CHECK_REWARDS = "click check rewards"
    const val CLICK_HOMEPAGE_BUTTON = "click homepage button"
}

object GiftBoxLabel {
    const val MAIN_PAGE = "main page"
    const val CONNECTION_ERROR = "connection error"
    const val ALREADY_OPENED = "already opened"
    const val LOGIN = "login"
    const val EARLY_END = "early end"
    const val REWARDS_SUMMARY = "rewards summary"
    const val CAMPAIGN_OVER = "campaign over"
    const val NO_REWARD = "no reward"
}