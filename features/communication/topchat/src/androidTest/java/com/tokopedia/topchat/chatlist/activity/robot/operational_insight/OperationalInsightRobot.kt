package com.tokopedia.topchat.chatlist.activity.robot.operational_insight

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object OperationalInsightRobot {

    fun clickOperationalInsightTicker() {
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(0, R.id.layout_ticker_chat_performance)
        ).perform(click())
    }
}