package com.tokopedia.tokochat.test.robot.general

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers

object GeneralResult {

    fun assertSnackBarWithSubText(msg: String) {
        Espresso.onView(ViewMatchers.withSubstring(msg))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
