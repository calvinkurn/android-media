package com.tokopedia.notifcenter.test.robot.detail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.common.withRecyclerView

object DetailRobot {

    fun clickLoadMoreAt(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.btn_loading)
        ).perform(click())
    }

    fun clickFilterAt(position: Int) {
        onView(
            withRecyclerView(R.id.rv_filter)
                .atPositionOnView(position, R.id.chips_filter)
        ).perform(click())
    }
}
