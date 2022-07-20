package com.tokopedia.topchat.chatroom.view.activity.robot.composearea

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R

object ComposeAreaRobot {
    fun setComposedText(msg: String) {
        onView(withId(R.id.new_comment))
            .perform(replaceText(msg))
    }
}