package com.tokopedia.tokochat.test.chatroom.robot.message_bubble

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView
import com.tokopedia.tokochat_common.R as tokochat_commonR

object MessageBubbleResult {

    fun assertImageAttachmentVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(isDisplayed())
        } else {
            ViewAssertions.doesNotExist()
        }
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_layout_image_bubble)
        ).check(matcher)
    }

    fun assertImageAttachmentRetryDownloadVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(isDisplayed())
        } else {
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))
        }
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_tv_image_bubble_error_download)
        ).check(matcher)
    }

    fun assertImageAttachmentRetryUploadVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(isDisplayed())
        } else {
            matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))
        }
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_icon_image_bubble_error_upload)
        ).check(matcher)
    }

    fun assertMessageBubbleVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(isDisplayed())
        } else {
            ViewAssertions.doesNotExist()
        }
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_layout_item_msg_bubble)
        ).check(matcher)
    }

    fun assertMessageBubbleText(position: Int, text: String) {
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_tv_msg)
        ).check(matches(withText(text)))
    }

    fun assertMessageBubbleCheckMark(position: Int) {
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_iv_msg_check_mark)
        ).check(matches(isDisplayed()))
    }

    fun assertMessageBubbleReadMoreText(position: Int) {
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_tv_msg_read_more)
        ).check(matches(isDisplayed()))
    }

    fun assertMessageBubbleBottomSheet() {
        onView(
            withId(tokochat_commonR.id.tokochat_tv_long_message)
        ).check(matches(isDisplayed()))
    }

    fun assertMessageBubbleCensoredVisibility(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(isDisplayed())
        } else {
            ViewAssertions.doesNotExist()
        }
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_layout_item_msg_censor)
        ).check(matcher)
    }

    fun assertMessageBubbleCensoredText(position: Int, text: String) {
        onView(
            withRecyclerView(tokochat_commonR.id.tokochat_chatroom_rv)
                .atPositionOnView(position, tokochat_commonR.id.tokochat_tv_msg_censor)
        ).check(matches(withText(text)))
    }

    fun assertGuideChatBottomSheet() {
        onView(
            withId(tokochat_commonR.id.tokochat_tv_subtitle_guide_chat)
        ).check(matches(isDisplayed()))
    }
}
