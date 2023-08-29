package com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble

import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot.doActionOnListItemAt

object MsgBubbleRobot {

    fun clickCtaHeaderMsgAtBubblePosition(position: Int) {
        doActionOnListItemAt(position, R.id.tp_header_cta, click())
    }
}
