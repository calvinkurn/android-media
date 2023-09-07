package com.tokopedia.topchat.chatroom.view.activity.robot.broadcast

import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot

object BroadcastRobot {

    fun clickCtaBroadcast(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.ll_cta_container, click())
        }
    }

    fun clickFollowShop(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.btn_follow_shop, click())
        }
    }
}
