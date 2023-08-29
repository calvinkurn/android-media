package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot.doActionOnListItemAt

object BannedProductRobot {

    fun clickBannedProductBuyButtonAt(position: Int) {
        doActionOnListItemAt(position, R.id.btn_buy, click())
    }
}
