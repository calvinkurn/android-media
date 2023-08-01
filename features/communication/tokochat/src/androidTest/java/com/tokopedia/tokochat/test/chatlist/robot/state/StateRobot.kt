package com.tokopedia.tokochat.test.chatlist.robot.state

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.tokochat_common.R

object StateRobot {
    fun clickGoToHome() {
        onView(
            withId(R.id.globalerrors_action)
        ).perform(click())
    }
}
