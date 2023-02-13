package com.tokopedia.topchat.chatlist.activity.robot.operational_insight

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withIndex
import com.tokopedia.topchat.matchers.withRecyclerView

object OperationalInsightRobot {

    fun clickOperationalInsightTicker() {
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(0, R.id.layout_ticker_chat_performance)
        ).perform(click())
    }

    fun clickOnTextBottomSheet(text: String) {
        onView(withIndex(withSubstring(text), 0)).perform(click())
    }

    fun clickOnCTABottomSheet() {
        onView(withId(R.id.btn_visit_operational_insight)).perform(click())
    }
}