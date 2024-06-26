package com.tokopedia.tokochat.test.chatroom.robot.message_bubble

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokochat_common.R

object MessageBubbleRobot {

    fun clickReadMore() {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tv_msg_read_more)
        ).perform(ViewActions.click())
    }

    fun clickCheckGuide() {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tv_msg_censor_guide)
        ).perform(ViewActions.click())
    }
}
