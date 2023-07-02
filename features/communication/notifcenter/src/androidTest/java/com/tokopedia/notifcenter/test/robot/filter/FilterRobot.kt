package com.tokopedia.notifcenter.test.robot.filter

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.common.smoothScrollTo
import com.tokopedia.notifcenter.stub.common.withRecyclerView

object FilterRobot {
    fun clickFilterAt(position: Int) {
        onView(
            withRecyclerView(R.id.rv_filter)
                .atPositionOnView(position, R.id.chips_filter)
        ).perform(ViewActions.click())
    }

    fun smoothScrollOrderWidgetTo(position: Int) {
        onView(withId(R.id.rv_order_list)).perform(
            smoothScrollTo(position)
        )
    }
}
