package com.tokopedia.topchat.chatroom.view.activity.robot.composearea

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withIndex

object ComposeAreaRobot {
    fun setComposedText(msg: String) {
        onView(withIndex(withId(R.id.new_comment), 0))
            .perform(replaceText(msg))
    }
}