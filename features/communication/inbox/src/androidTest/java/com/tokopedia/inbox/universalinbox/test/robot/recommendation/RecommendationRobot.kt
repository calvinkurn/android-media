package com.tokopedia.inbox.universalinbox.test.robot.recommendation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.withRecyclerView

object RecommendationRobot {
    fun clickProductOnPosition(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_product_recommendation)
        ).perform(click())
    }

    fun clickPrePurchaseProductOnPosition(position: Int) {
        onView(
            withRecyclerView(com.tokopedia.recommendation_widget_common.R.id.carouselProductCardRecyclerView)
                .atPositionOnView(position, com.tokopedia.recommendation_widget_common.R.id.carouselProductCardItem)
        ).perform(click())
    }
}
