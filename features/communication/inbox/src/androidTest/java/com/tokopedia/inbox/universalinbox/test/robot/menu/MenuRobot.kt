package com.tokopedia.inbox.universalinbox.test.robot.menu

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.withCustomConstraints
import com.tokopedia.inbox.universalinbox.stub.common.withRecyclerView

object MenuRobot {
    fun swipeDown() {
        onView(withId(R.id.inbox_layout_swipe_refresh)).perform(
            withCustomConstraints(
                ViewActions.swipeDown(),
                ViewMatchers.isDisplayingAtLeast(100)
            )
        )
    }

    fun clickMenuOnPosition(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_layout_menu)
        ).perform(click())
    }
}
