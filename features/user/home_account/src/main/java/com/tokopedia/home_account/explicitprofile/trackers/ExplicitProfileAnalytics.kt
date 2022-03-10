package com.tokopedia.home_account.explicitprofile.trackers

import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.Action.CLICK_CATEGORY_TABS
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.Action.CLICK_INFORMATION_ICON
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.Action.CLICK_OPTION_VALUE
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.Action.CLICK_RESET_PREFERENCE
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.BUSINESS_UNIT_USER_PLATFORM
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.Category.EXPLICIT_PROFILE_FORM_PAGE
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.EVENT_BUSINESS_UNIT
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.EVENT_CURRENT_SITE
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.Event.CLICK_EXPLICIT_PROFILE
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.Label.FAIL
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.Label.SUCCESS
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalyticConstants.TOKOPEDIA_MARKETPLACE_SITE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class ExplicitProfileAnalytics {

    private fun sendTracker(action: String, label: String) {
        val trackerParam = TrackAppUtils.gtmData(
            CLICK_EXPLICIT_PROFILE,
            EXPLICIT_PROFILE_FORM_PAGE,
            action,
            label
        )

        trackerParam[EVENT_BUSINESS_UNIT] = BUSINESS_UNIT_USER_PLATFORM
        trackerParam[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE

        TrackApp.getInstance().gtm.sendGeneralEvent(trackerParam)
    }

    fun clickOnResetPreference() {
        sendTracker(
            action = CLICK_RESET_PREFERENCE,
            label = ""
        )
    }

    fun clickOnTabMenu(selectedTab: String) {
        sendTracker(
            action = CLICK_CATEGORY_TABS,
            label = selectedTab
        )
    }

    fun clickOnInfoIcon() {
        sendTracker(
            action = CLICK_INFORMATION_ICON,
            label = ""
        )
    }

    fun clickOnAnswers(selectedAnswer: String) {
        sendTracker(
            action = CLICK_OPTION_VALUE,
            label = selectedAnswer
        )
    }

    fun onSavePreference(isSuccess: Boolean, message: String = "") {
        sendTracker(
            action = CLICK_OPTION_VALUE,
            label = if (isSuccess) SUCCESS else String.format("%s - %s", FAIL, message)
        )
    }
}