package com.tokopedia.shareexperience.test.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.stub.common.matcher.waitForLayout

object GeneralRobot {
    fun waitingForLayout() {
        onView(withId(R.id.shareex_rv_bottom_sheet))
            .perform(waitForLayout())
    }
}
