package com.tokopedia.inbox.universalinbox.test.robot.recommendation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView

object RecommendationRobot {
    fun clickProductOnPosition(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_product_recommendation)
        ).perform(ViewActions.click())
    }
}
