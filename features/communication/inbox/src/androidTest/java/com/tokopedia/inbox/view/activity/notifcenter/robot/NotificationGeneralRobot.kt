package com.tokopedia.inbox.view.activity.notifcenter.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.notifcenter.R

object NotificationGeneralRobot {

    fun scrollToProductPosition(position: Int) {
        onView(withId(R.id.recycler_view)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }
}