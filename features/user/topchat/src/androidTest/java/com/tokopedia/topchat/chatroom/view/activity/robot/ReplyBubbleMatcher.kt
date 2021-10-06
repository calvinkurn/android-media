package com.tokopedia.topchat.chatroom.view.activity.robot

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.topchat.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object ReplyBubbleMatcher {

    fun matchReplyBoxChildWithId(viewId: Int): Matcher<View> {
        return Matchers.allOf(
            ViewMatchers.withId(viewId),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.reply_box))
        )
    }

}