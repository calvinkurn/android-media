package com.tokopedia.topchat.chatroom.view.activity.robot.copytoclipboard

import com.tokopedia.topchat.chatroom.view.activity.robot.replybubble.ReplyBubbleRobot

object CopyToClipboardRobot {

    const val CTC_ITEM_MENU_POSITION = 1

    fun clickCtcItemMenu(position: Int? = null) {
        ReplyBubbleRobot.clickLongClickMenuItemAt(
            position ?: CTC_ITEM_MENU_POSITION
        )
    }
}