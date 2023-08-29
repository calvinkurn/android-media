package com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble

import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot

object MsgBubbleRobot {

    fun clickCtaHeaderMsgAtBubblePosition(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.tp_header_cta, click())
        }
    }
}
