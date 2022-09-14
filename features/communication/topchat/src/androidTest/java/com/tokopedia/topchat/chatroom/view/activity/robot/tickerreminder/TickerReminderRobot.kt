package com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot.doActionOnListItemAt

object TickerReminderRobot {

    fun clickTickerCloseButtonAt(position: Int) {
        doActionOnListItemAt(
            position = position,
            viewId = com.tokopedia.unifycomponents.R.id.ticker_close_icon,
            action = click()
        )
    }

    fun clickTickerLabel(label: String) {
        onView(withSubstring(label)).perform(click())
    }

}