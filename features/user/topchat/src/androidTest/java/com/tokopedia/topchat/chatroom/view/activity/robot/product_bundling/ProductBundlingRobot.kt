package com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductBundlingRobot {

    fun clickCtaProductBundling(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.button_open_package)
        ).perform(click())
    }
}