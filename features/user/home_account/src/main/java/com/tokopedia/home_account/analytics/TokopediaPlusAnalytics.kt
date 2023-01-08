package com.tokopedia.home_account.analytics

import com.tokopedia.home_account.AccountConstants.Analytics.BusinessUnit.USER_PLATFORM_UNIT
import com.tokopedia.home_account.AccountConstants.Analytics.Category.CATEGORY_ACCOUNT_BUYER
import com.tokopedia.home_account.AccountConstants.Analytics.CurrentSite.TOKOPEDIA_MARKETPLACE_SITE
import com.tokopedia.home_account.AccountConstants.Analytics.Event.EVENT_CLICK_ACCOUNT
import com.tokopedia.track.builder.Tracker

class TokopediaPlusAnalytics {

    fun sendClickOnTokopediaPlusButtonEvent(isSubscriber: Boolean) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_TOKOPEDIA_PLUS)
            .setEventCategory(CATEGORY_ACCOUNT_BUYER)
            .setEventLabel(if (isSubscriber) "subscriber" else "not subscriber")
            .setCustomProperty(KEY_TRACKED_ID, TRACKED_ID_TOKOPEDIA_PLUS)
            .setBusinessUnit(USER_PLATFORM_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE_SITE)
            .build()
            .send()
    }

    companion object {
        private const val ACTION_CLICK_TOKOPEDIA_PLUS = "click on tokopedia plus button"
        private const val KEY_TRACKED_ID = "trackerId"
        private const val TRACKED_ID_TOKOPEDIA_PLUS = "33874"
    }
}