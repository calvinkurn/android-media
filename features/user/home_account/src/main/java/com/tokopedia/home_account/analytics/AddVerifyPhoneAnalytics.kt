package com.tokopedia.home_account.analytics

import com.tokopedia.home_account.AccountConstants
import com.tokopedia.track.builder.Tracker

class AddVerifyPhoneAnalytics {

    private fun generateTracker(): Tracker.Builder {
        return Tracker.Builder()
            .setEvent(AccountConstants.Analytics.Event.EVENT_CLICK_ACCOUNT)
            .setBusinessUnit(AccountConstants.Analytics.BusinessUnit.USER_PLATFORM_UNIT)
            .setCurrentSite(AccountConstants.Analytics.CurrentSite.TOKOPEDIA_MARKETPLACE_SITE)
    }

    fun sendClickAddPhoneNumberEvent() {
        generateTracker()
            .setEventAction("click add phone number")
            .setEventCategory("account page setting - phone number")
            .setEventLabel("")
            .setCustomProperty("trackerId", "38507")
            .build()
            .send()
    }

    fun sendClickVerifiedPhoneNumberEvent() {
        generateTracker()
            .setEventAction("click verified phone number")
            .setEventCategory("account page setting - verified pn")
            .setEventLabel("")
            .setCustomProperty("trackerId", "38508")
            .build()
            .send()
    }
}
