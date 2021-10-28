package com.tokopedia.topchat.chatroom.view.activity.robot.replybubble

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object ReplyBubbleMatcher {

    fun matchReplyBoxChildWithId(viewId: Int): Matcher<View> {
        return allOf(
            withId(viewId),
            isDescendantOfA(withId(R.id.reply_box))
        )
    }

}