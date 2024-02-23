package com.tokopedia.topchat.chatroom.view.activity.robot.ordercancellation

import androidx.test.espresso.action.ViewActions
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot

object OrderCancellationRobot {
    fun clickOrderCancellationWidgetAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(
                position,
                R.id.topchat_chatroom_layout_order_cancellation,
                ViewActions.click()
            )
        }
    }
}
