package com.tokopedia.inbox.universalinbox.test.robot.recommendation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.atPositionCheckInstanceOf
import com.tokopedia.inbox.universalinbox.stub.common.withRecyclerView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel

object RecommendationResult {
    fun assertProductRecommendation(position: Int) {
        onView(withId(R.id.inbox_rv)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = RecommendationItem::class.java
            )
        )
    }

    fun assertPrePurchaseRecommendation(position: Int) {
        onView(withId(R.id.inbox_rv)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = RecommendationWidgetModel::class.java
            )
        )
    }

    fun assertPrePurchaseRecommendationGone(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_recommendation_widget)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertApplinkPDP() {
        Intents.intended(
            IntentMatchers.hasData(
                "tokopedia-android-internal://marketplace/product-detail/2455862417/"
            )
        )
    }

    fun assertProductRecommendationName(position: Int, name: String) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, com.tokopedia.productcard.R.id.textViewProductName)
        ).check(matches(withText(name)))
    }

    fun assertProductWidgetRecommendationName(position: Int, name: String) {
        onView(
            withRecyclerView(com.tokopedia.recommendation_widget_common.R.id.carouselProductCardRecyclerView)
                .atPositionOnView(position, com.tokopedia.productcard.R.id.textViewProductName)
        ).check(matches(withText(name)))
    }
}
