package com.tokopedia.inbox.universalinbox.test.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.smoothScrollTo

object GeneralRobot {
    fun scrollToPosition(position: Int) {
        onView(withId(R.id.inbox_rv)).perform(
            smoothScrollTo(position)
        )
    }

    fun clickOnBell() {
        onView(withId(R.id.inbox_icon_notif)).perform(click())
    }
}
