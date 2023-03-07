package com.tokopedia.usercomponents.explicit.cassava

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess

object ExplicitCassava {

    const val VALUE_PAGE_NAME = "discovery page"
    const val VALUE_PAGE_PATH = "discovery path"
    const val VALUE_PAGE_TYPE = "discovery type"
    const val VALUE_TEMPLATE_NAME = "halal_single"
    private const val VALUE_EXPLICIT_WIDGET = "explicit widget"
    private const val VALUE_USER_PLATFORM = "user platform"
    private const val VALUE_TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val VALUE_EVENT_CLICK_ACCOUNT = "clickAccount"
    private const val VALUE_EVENT_CLICK_ACCOUNT_IRIS = "viewAccountIris"
    private const val VALUE_ACTION_TRACKER_ID_31560 = "impression - explicit widget"
    private const val VALUE_ACTION_TRACKER_ID_31561 = "click on yes button - explicit widget"
    private const val VALUE_ACTION_TRACKER_ID_31562 = "click on no button - explicit widget"
    private const val VALUE_ACTION_TRACKER_ID_31563 = "click on close button - explicit widget"
    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_PAGE_PATH = "pagePath"
    private const val KEY_PAGE_TYPE = "pageType"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"

    private fun generateTrackerQuery(
        event: String,
        action: String
    ): Map<String, String> {
        return mapOf(
            KEY_EVENT to event,
            KEY_EVENT_CATEGORY to "$VALUE_PAGE_NAME - $VALUE_EXPLICIT_WIDGET",
            KEY_EVENT_ACTION to action,
            KEY_EVENT_LABEL to "$VALUE_PAGE_NAME - $VALUE_TEMPLATE_NAME",
            KEY_PAGE_PATH to VALUE_PAGE_PATH,
            KEY_PAGE_TYPE to VALUE_PAGE_TYPE,
            KEY_BUSINESS_UNIT to VALUE_USER_PLATFORM,
            KEY_CURRENT_SITE to VALUE_TOKOPEDIA_MARKETPLACE
        )
    }

    fun CassavaTestRule.validateTracker(
        state: ExplicitCassavaState
    ) {
        val query = when (state) {
            ExplicitCassavaState.TRACKER_ID_31560 -> generateTrackerQuery(
                VALUE_EVENT_CLICK_ACCOUNT_IRIS,
                VALUE_ACTION_TRACKER_ID_31560
            )
            ExplicitCassavaState.TRACKER_ID_31561 -> generateTrackerQuery(
                VALUE_EVENT_CLICK_ACCOUNT,
                VALUE_ACTION_TRACKER_ID_31561
            )
            ExplicitCassavaState.TRACKER_ID_31562 -> generateTrackerQuery(
                VALUE_EVENT_CLICK_ACCOUNT,
                VALUE_ACTION_TRACKER_ID_31562
            )
            ExplicitCassavaState.TRACKER_ID_31563 -> generateTrackerQuery(
                VALUE_EVENT_CLICK_ACCOUNT,
                VALUE_ACTION_TRACKER_ID_31563
            )
        }

        val queryMatcher = this.validate(
            listOf(query),
            CassavaTestRule.MODE_SUBSET
        )

        ViewMatchers.assertThat(queryMatcher, hasAllSuccess())
    }

}