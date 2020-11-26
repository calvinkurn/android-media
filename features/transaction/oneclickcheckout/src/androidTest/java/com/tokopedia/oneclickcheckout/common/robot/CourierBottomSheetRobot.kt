package com.tokopedia.oneclickcheckout.common.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText

class CourierBottomSheetRobot {

    fun chooseCourierWithText(text: String) {
        onView(withText(text)).perform(click())
    }
}