package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withSubstring

object ProductCardResult {

    fun hasFailedToasterWithMsg(msg: String) {
        onView(withSubstring(msg))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

}