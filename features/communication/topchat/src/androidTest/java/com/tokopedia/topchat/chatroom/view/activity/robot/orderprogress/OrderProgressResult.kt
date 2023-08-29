package com.tokopedia.topchat.chatroom.view.activity.robot.orderprogress

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import org.hamcrest.CoreMatchers

object OrderProgressResult {

    fun assertOrderProgressTitle(text: String) {
        onView(withId(R.id.tp_order_name)).check(
            matches(withText(text))
        )
    }

    fun assertOrderProgressGone() {
        onView(withId(R.id.tp_order_name)).check(
            matches(CoreMatchers.not(ViewMatchers.isDisplayed()))
        )
    }
}
