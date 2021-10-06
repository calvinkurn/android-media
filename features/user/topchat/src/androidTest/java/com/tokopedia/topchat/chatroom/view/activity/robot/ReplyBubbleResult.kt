package com.tokopedia.topchat.chatroom.view.activity.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.ReplyBubbleMatcher.matchReplyBoxChildWithId
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not

object ReplyBubbleResult {

    fun hasVisibleReplyCompose() {
        assertReplyCompose(isDisplayed())
        assertReplyComposeIcon(isDisplayed())
        assertReplyComposeTitle(isDisplayed())
        assertReplyComposeMsg(isDisplayed())
        assertReplyComposeCloseBtn(isDisplayed())
    }

    fun hasNoVisibleReplyCompose() {
        assertReplyCompose(not(isDisplayed()))
        assertReplyComposeIcon(not(isDisplayed()))
        assertReplyComposeTitle(not(isDisplayed()))
        assertReplyComposeMsg(not(isDisplayed()))
        assertReplyComposeCloseBtn(not(isDisplayed()))
    }

    fun hasVisibleReplyBubbleAt(position: Int) {
        assertReplyBubbleAt(position, isDisplayed())
    }

    fun hasNoVisibleReplyBubbleAt(position: Int) {
        assertReplyBubbleAt(position, not(isDisplayed()))
    }

    private fun assertReplyBubbleAt(position: Int, matcher: Matcher<View>) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position, R.id.cl_reply_container
            )
        ).check(matches(matcher))
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

}