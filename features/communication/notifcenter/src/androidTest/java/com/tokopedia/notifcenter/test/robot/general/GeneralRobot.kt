package com.tokopedia.notifcenter.test.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.common.smoothScrollTo

object GeneralRobot {
    fun smoothScrollNotificationTo(position: Int) {
        onView(withId(R.id.recycler_view)).perform(
            smoothScrollTo(position)
        )
    }
}
