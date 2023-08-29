package com.tokopedia.topchat.chatroom.view.activity.robot.composearea

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.ClickChildViewWithIdAction
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder

object ComposeAreaRobot {

    fun clickComposeArea() {
        onView(withId(R.id.new_comment))
            .perform(ViewActions.click())
    }
    fun typeMessageComposeArea(msg: String) {
        onView(withId(R.id.new_comment))
            .perform(replaceText(msg))
    }

    fun clickSendBtn() {
        onView(withId(R.id.send_but))
            .perform(ViewActions.click())
    }

    fun clickPlusIconMenu() {
        onView(withId(R.id.topchat_icon_chat_menu))
            .perform(ViewActions.click())
    }

    fun clickStickerIconMenu() {
        onView(withId(R.id.iv_chat_sticker))
            .perform(ViewActions.click())
    }

    fun clickAttachProductMenu() {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<AttachmentItemViewHolder>(
                0,
                ViewActions.click()
            )
        onView(withId(R.id.rv_topchat_attachment_menu))
            .perform(viewAction)
    }

    fun clickAttachImageMenu() {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<AttachmentItemViewHolder>(
                1,
                ViewActions.click()
            )
        onView(withId(R.id.rv_topchat_attachment_menu))
            .perform(viewAction)
    }

    fun clickAttachInvoiceMenu() {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<AttachmentItemViewHolder>(
                2,
                ViewActions.click()
            )
        onView(withId(R.id.rv_topchat_attachment_menu))
            .perform(viewAction)
    }

    fun clickAttachVoucherMenu() {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<AttachmentItemViewHolder>(
                3,
                ViewActions.click()
            )
        onView(withId(R.id.rv_topchat_attachment_menu))
            .perform(viewAction)
    }

    fun clickStickerAtPosition(position: Int) {
        Thread.sleep(300) // delay for sticker load
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(
                position,
                ClickChildViewWithIdAction()
                    .clickChildViewWithId(R.id.iv_sticker)
            )
        onView(withId(R.id.rv_sticker)).perform(viewAction)
    }

    fun clickTemplateChatAt(position: Int) {
        val viewAction = RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            position,
            ViewActions.click()
        )
        onView(withId(R.id.list_template)).perform(viewAction)
    }
}
