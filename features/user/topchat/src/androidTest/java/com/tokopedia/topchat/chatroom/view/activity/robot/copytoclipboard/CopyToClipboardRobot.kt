package com.tokopedia.topchat.chatroom.view.activity.robot.copytoclipboard

import com.tokopedia.topchat.chatroom.view.activity.robot.longclickbubblemenu.LongClickBubbleMenuRobot.clickLongClickMenuItemAt

object CopyToClipboardRobot {

    const val CTC_ITEM_MENU_POSITION = 1

    fun clickCtcItemMenu(position: Int? = null) {
        clickLongClickMenuItemAt(
            position ?: CTC_ITEM_MENU_POSITION
        )
    }
}