package com.tokopedia.topchat.chatroom.view.activity.robot.broadcast

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.topchat.R

object BroadcastRobot {

    fun clickCtaBroadcast() {
        Espresso.onView(ViewMatchers.withId(R.id.ll_cta_container)).perform(ViewActions.click())
    }
}
