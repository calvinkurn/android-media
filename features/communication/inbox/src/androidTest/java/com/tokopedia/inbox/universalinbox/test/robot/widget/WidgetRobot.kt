package com.tokopedia.inbox.universalinbox.test.robot.widget

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.withRecyclerView
import com.tokopedia.unifycomponents.R as unifycomponentsR

object WidgetRobot {
    fun clickWidgetOnPosition(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv_widget_meta)
                .atPositionOnView(position, R.id.inbox_card_widget)
        ).perform(click())
    }

    fun clickIndividualLocalLoad(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv_widget_meta)
                .atPositionOnView(position, unifycomponentsR.id.refreshID)
        ).perform(click())
    }
}
