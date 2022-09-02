package com.tokopedia.topchat.chatroom.view.activity.robot.replybubble

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleMatcher.matchReplyBoxChildWithId
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
        assertReplyBubbleAt(position, R.id.cl_reply_container, isDisplayed())
    }

    fun hasNoVisibleReplyBubbleAt(position: Int) {
        assertReplyBubbleAt(position, R.id.cl_reply_container, not(isDisplayed()))
    }

    fun hasReplyBubbleTitleAt(position: Int, text: String) {
        assertReplyBubbleAt(position, R.id.tp_reply_from, withText(text))
    }

    fun hasVisibleReplyBubbleStickerAt(position: Int) {
        assertReplyBubbleAt(position, R.id.rba_sticker, isDisplayed())
    }

    fun hasVisibleReplyBubbleImageAt(position: Int) {
        assertReplyBubbleAt(position, R.id.rba_image, isDisplayed())
    }

    private fun assertReplyBubbleAt(
        position: Int,
        viewId: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position, viewId
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