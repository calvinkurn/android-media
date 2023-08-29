package com.tokopedia.tokochat.test.chatroom.robot.reply_area

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokochat.stub.common.matcher.withRecyclerView
import com.tokopedia.tokochat_common.R

object ReplyAreaRobot {

    fun typeInReplyArea(text: String) {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tf_new_comment)
        ).perform(typeText(text))
    }

    fun replaceTextInReplyArea(text: String) {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tf_new_comment)
        ).perform(replaceText(text))
    }

    fun clearReplyArea() {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_tf_new_comment)
        ).perform(clearText())
    }

    fun clickReplyButton() {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_ic_send_btn)
        ).perform(click())
    }

    fun clickAttachmentPlusButton() {
        Espresso.onView(
            ViewMatchers.withId(R.id.tokochat_icon_chat_menu)
        ).perform(click())
    }

    fun clickAttachmentMenuButton(position: Int) {
        Espresso.onView(
            withRecyclerView(R.id.tokochat_rv_attachment_menu)
                .atPositionOnView(position, R.id.tokochat_icon_attachment_menu)
        ).perform(click())
    }
}
