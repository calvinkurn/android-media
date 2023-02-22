package com.tokopedia.tokochat.test.robot.message_bubble

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView
import com.tokopedia.tokochat_common.R

object MessageBubbleResult {

    fun assertImageAttachmentVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(ViewMatchers.isDisplayed())
        } else {
            ViewAssertions.doesNotExist()
        }
        Espresso.onView(
            withRecyclerView(R.id.tokochat_chatroom_rv)
                .atPositionOnView(position, R.id.tokochat_layout_image_bubble)
        ).check(matcher)
    }

    fun assertMessageBubbleVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(ViewMatchers.isDisplayed())
        } else {
            ViewAssertions.doesNotExist()
        }
        Espresso.onView(
            withRecyclerView(R.id.tokochat_chatroom_rv)
                .atPositionOnView(position, R.id.tokochat_layout_item_msg_bubble)
        ).check(matcher)
    }

    fun assertMessageBubbleText(position: Int, text: String) {
        Espresso.onView(
            withRecyclerView(R.id.tokochat_chatroom_rv)
                .atPositionOnView(position, R.id.tokochat_tv_msg)
        ).check(matches(withText(text)))
    }

    fun assertMessageBubbleCheckMark(position: Int) {
        Espresso.onView(
            withRecyclerView(R.id.tokochat_chatroom_rv)
                .atPositionOnView(position, R.id.tokochat_iv_msg_check_mark)
        ).check(matches(isDisplayed()))
    }

    fun assertMessageBubbleReadMoreText(position: Int) {
        Espresso.onView(
            withRecyclerView(R.id.tokochat_chatroom_rv)
                .atPositionOnView(position, R.id.tokochat_tv_msg_read_more)
        ).check(matches(isDisplayed()))
    }

    fun assertMessageBubbleBottomSheet() {
        Espresso.onView(
            withId(R.id.tokochat_tv_long_message)
        ).check(matches(isDisplayed()))
    }
}
