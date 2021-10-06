package com.tokopedia.topchat.chatroom.view.activity.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object ReplyBubbleResult {

    fun hasVisibleReplyCompose() {
        assertReplyCompose(isDisplayed())
        assertReplyComposeIcon(isDisplayed())
        assertReplyComposeTitle(isDisplayed())
        assertReplyComposeMsg(isDisplayed())
        assertReplyComposeCloseBtn(isDisplayed())
    }

    fun assertReplyCompose(matcher: Matcher<in View>) {
        onView(
            matchReplyBoxChildWithId(R.id.trb_container)
        ).check(matches(matcher))
    }

    fun assertReplyComposeIcon(matcher: Matcher<in View>) {
        onView(
            matchReplyBoxChildWithId(R.id.iv_reply_icon)
        ).check(matches(matcher))
    }

    fun assertReplyComposeTitle(matcher: Matcher<in View>) {
        onView(
            matchReplyBoxChildWithId(R.id.tp_reply_from)
        ).check(matches(matcher))
    }

    fun assertReplyComposeMsg(matcher: Matcher<in View>) {
        onView(
            matchReplyBoxChildWithId(R.id.tp_reply_msg)
        ).check(matches(matcher))
    }

    fun assertReplyComposeCloseBtn(matcher: Matcher<in View>) {
        onView(
            matchReplyBoxChildWithId(R.id.iv_rb_close)
        ).check(matches(matcher))
    }

    private fun matchReplyBoxChildWithId(viewId: Int): Matcher<View> {
        return allOf(
            withId(viewId),
            isDescendantOfA(withId(R.id.reply_box))
        )
    }
}