package com.tokopedia.tokochat.test.chatlist.robot.state

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.globalerror.R as globalerrorR

object StateRobot {
    fun clickGoToHome() {
        onView(
            withId(globalerrorR.id.globalerrors_action)
        ).perform(click())
    }
}
