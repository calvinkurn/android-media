package com.tokopedia.topchat.chatlist.activity.robot.operational_insight

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object OperationalInsightResult {

    fun assertOperationalInsightTickerVisible() {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.layout_ticker_chat_performance)
        )
            .check(matches(isDisplayed()))
    }

    fun assertOperationalInsightTickerText(message: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.tv_ticker_chat_performance)
        )
            .check(matches(withText(message)))
    }

    fun assertOperationalInsightTickerNotVisible() {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(0, R.id.layout_ticker_chat_performance)
        )
            .check(doesNotExist())
    }

    fun assertBottomSheetVisible() {
        onView(withId(R.id.layout_bs_operational_insight)).check(matches(isDisplayed()))
    }

    fun assertOperationalInsightBottomSheetText(message: String) {
        onView(withId(R.id.tv_chat_performance_summary)).check(matches(withText(message)))
    }
}
