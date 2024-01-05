package com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.unifycomponents.R as unifycomponentsR

object TickerReminderRobot {

    fun clickTickerCloseButtonAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(
                position = position,
                viewId = unifycomponentsR.id.ticker_close_icon,
                action = click()
            )
        }
    }

    fun clickTickerLabel(label: String) {
        onView(withSubstring(label)).perform(click())
    }
}
