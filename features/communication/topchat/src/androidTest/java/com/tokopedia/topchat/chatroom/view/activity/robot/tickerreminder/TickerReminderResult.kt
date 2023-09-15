package com.tokopedia.topchat.chatroom.view.activity.robot.tickerreminder

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsNotInstanceOf
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertChatRecyclerview
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertViewInRecyclerViewAt
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ReminderTickerViewHolder
import org.hamcrest.CoreMatchers.not
import com.tokopedia.unifycomponents.R as unifycomponentsR

object TickerReminderResult {

    fun assertReminderTickerVisibleAtPosition(position: Int) {
        assertChatRecyclerview(
            hasViewHolderItemAtPosition(
                position,
                ReminderTickerViewHolder::class.java
            )
        )
    }

    fun assertReminderTickerVisibleWithText(position: Int, text: String) {
        assertViewInRecyclerViewAt(
            position,
            unifycomponentsR.id.ticker_description,
            withSubstring(text)
        )
    }

    fun assertReminderTickerNotVisible() {
        assertChatRecyclerview(
            not(hasViewHolderOf(ReminderTickerViewHolder::class.java))
        )
    }

    fun assertReminderTickerIsNotAtPosition(position: Int) {
        onView(ViewMatchers.withId(R.id.recycler_view_chatroom)).check(
            atPositionIsNotInstanceOf(position, ReminderTickerViewHolder::class.java)
        )
    }
}
