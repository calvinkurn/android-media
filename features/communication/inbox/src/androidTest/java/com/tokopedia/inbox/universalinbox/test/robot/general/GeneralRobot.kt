package com.tokopedia.inbox.universalinbox.test.robot.general

import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.smoothScrollTo

object GeneralRobot {
    fun scrollToPosition(position: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.inbox_rv)).perform(
            smoothScrollTo(position)
        )
    }
}
