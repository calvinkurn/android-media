package com.tokopedia.inbox.universalinbox.test.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.smoothScrollTo
import com.tokopedia.inbox.universalinbox.stub.common.waitForLayout
import com.tokopedia.unifycomponents.toPx

object GeneralRobot {
    fun scrollToPosition(position: Int) {
        onView(withId(R.id.inbox_rv))
            .perform(waitForLayout())
            .perform(smoothScrollTo(position))
    }

    fun scrollToPosition(position: Int, offset: Int = 0) {
        onView(withId(R.id.inbox_rv))
            .perform(waitForLayout())
            .perform(smoothScrollTo(position, offset.toPx()))
    }

    fun clickOnBell() {
        onView(withId(R.id.inbox_icon_notif)).perform(click())
    }
}
