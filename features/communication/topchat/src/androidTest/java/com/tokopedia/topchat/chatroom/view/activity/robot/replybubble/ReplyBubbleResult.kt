package com.tokopedia.topchat.chatroom.view.activity.robot.replybubble

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleMatcher.matchReplyBoxChildWithId
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

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
        generalResult {
            assertViewInRecyclerViewAt(position, viewId, matcher)
        }
    }

    fun assertReplyCompose(matcher: Matcher<View>) {
        onView(
            matchReplyBoxChildWithId(R.id.trb_container)
        ).check(matches(matcher))
    }

    fun assertReplyComposeIcon(matcher: Matcher<View>) {
        onView(
            matchReplyBoxChildWithId(R.id.iv_reply_icon)
        ).check(matches(matcher))
    }

    fun assertReplyComposeTitle(matcher: Matcher<View>) {
        onView(
            matchReplyBoxChildWithId(R.id.tp_reply_from)
        ).check(matches(matcher))
    }

    fun assertReplyComposeMsg(matcher: Matcher<View>) {
        onView(
            matchReplyBoxChildWithId(R.id.tp_reply_msg)
        ).check(matches(matcher))
    }

    fun assertReplyComposeCloseBtn(matcher: Matcher<View>) {
        onView(
            matchReplyBoxChildWithId(R.id.iv_rb_close)
        ).check(matches(matcher))
    }

    fun assertLongClickMenu() {
        onView(ViewMatchers.withId(R.id.rv_menu)).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    fun assertMsgBubbleAt(position: Int, matcher: Matcher<View>) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.tvMessage, matcher)
        }
    }
}
