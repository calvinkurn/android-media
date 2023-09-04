package com.tokopedia.inbox.universalinbox.test.robot.recommendation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.withRecyclerView
import com.tokopedia.carouselproductcard.R as carouselproductcardR

object RecommendationRobot {
    fun clickProductOnPosition(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_product_recommendation)
        ).perform(click())
    }

    fun clickPrePurchaseProductOnPosition(position: Int) {
        onView(
            withRecyclerView(carouselproductcardR.id.carouselProductCardRecyclerView)
                .atPositionOnView(position, carouselproductcardR.id.carouselProductCardItem)
        ).perform(click())
    }
}
