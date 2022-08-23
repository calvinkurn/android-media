package com.tokopedia.inbox.view.activity.notifcenter.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.notifcenter.R

object NotificationGeneralRobot {

    fun scrollToProductPosition(position: Int) {
        onView(withId(R.id.recycler_view)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun clickNotification(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view).atPositionOnView(
                position, R.id.notification_container
            )
        ).perform(click())
    }

    fun clickChipFilter(position: Int) {
        onView(
            withRecyclerView(R.id.rv_filter).atPositionOnView(
                position, R.id.chips_filter
            )
        ).perform(click())
    }
}