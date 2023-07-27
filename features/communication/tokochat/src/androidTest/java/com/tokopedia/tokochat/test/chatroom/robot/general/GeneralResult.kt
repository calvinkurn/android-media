package com.tokopedia.tokochat.test.chatroom.robot.general

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withSubstring

object GeneralResult {

    fun assertSnackBarWithSubText(msg: String) {
        onView(withSubstring(msg))
            .check(matches(isDisplayed()))
    }
}
