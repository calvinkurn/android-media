package com.tokopedia.topchat.chatroom.view.activity.robot.msgbubble

import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot

object MsgBubbleRobot {

    fun clickCtaHeaderMsgBubbleAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.tp_header_cta, click())
        }
    }

    fun clickReadMoreAutoReplyMsgBubbleAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.topchat_tv_auto_reply_read_more, click())
        }
    }
}
