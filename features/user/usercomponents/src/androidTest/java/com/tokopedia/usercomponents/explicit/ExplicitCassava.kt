package com.tokopedia.usercomponents.explicit

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess

object ExplicitCassava {

    private const val PAGE_NAME = "discovery page"
    private const val PAGE_PATH = "discovery path"
    private const val PAGE_TYPE = "discovery type"
    private const val TEMPLATE_NAME = "halal_single"

    private fun generateTrackerQuery(
        event: String,
        action: String
    ): Map<String, String> {
        return mapOf(
            "event" to event,
            "eventCategory" to "$PAGE_NAME - explicit widget",
            "eventAction" to action,
            "eventLabel" to "$PAGE_NAME - $TEMPLATE_NAME",
            "pagePath" to PAGE_PATH,
            "pageType" to PAGE_TYPE,
            "businessUnit" to "user platform",
            "currentSite" to "tokopediamarketplace"
        )
    }

    fun CassavaTestRule.validateTracker(
        state: ExplicitCassavaState
    ) {
        val query = when (state) {
            ExplicitCassavaState.TRACKER_ID_31560 -> generateTrackerQuery("viewAccountIris", "impression - explicit widget")
            ExplicitCassavaState.TRACKER_ID_31561 -> generateTrackerQuery("clickAccount", "click on yes button - explicit widget")
            ExplicitCassavaState.TRACKER_ID_31562 -> generateTrackerQuery("clickAccount", "click on no button - explicit widget")
            ExplicitCassavaState.TRACKER_ID_31563 -> generateTrackerQuery("clickAccount", "click on close button - explicit widget")
        }

        val queryMatcher = this.validate(
            listOf(query),
            CassavaTestRule.MODE_SUBSET
        )

        ViewMatchers.assertThat(queryMatcher, hasAllSuccess())
    }

}