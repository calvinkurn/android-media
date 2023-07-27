package com.tokopedia.tokochat.test.chatroom.robot.consent

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokochat_common.R

object ConsentRobot {

    fun clickSubmitConsent() {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_btn_consent)
        ).perform(ViewActions.click())
    }

    fun clickCheckBoxConsent() {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.usercomponents.R.id.checkboxPurposes)
        ).perform(ViewActions.click())
    }
}
