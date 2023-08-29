package com.tokopedia.topchat.chatroom.view.activity.robot.composearea

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.topchat.R

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
}
