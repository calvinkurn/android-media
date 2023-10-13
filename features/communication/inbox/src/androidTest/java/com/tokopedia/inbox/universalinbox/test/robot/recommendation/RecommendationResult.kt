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
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationWidgetUiModel
import com.tokopedia.carouselproductcard.R as carouselproductcardR
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

object RecommendationResult {
    fun assertProductRecommendation(position: Int) {
        onView(withId(R.id.inbox_rv)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = UniversalInboxRecommendationUiModel::class.java
            )
        )
    }

    fun assertWidgetRecommendation(position: Int) {
        onView(withId(R.id.inbox_rv)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = UniversalInboxRecommendationWidgetUiModel::class.java
            )
        )
    }

    fun assertWidgetRecommendationGone(position: Int) {
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
                .atPositionOnView(position, productcardR.id.textViewProductName)
        ).check(matches(withText(name)))
    }

    fun assertProductWidgetPrePurchaseRecommendationName(position: Int, name: String) {
        onView(
            withRecyclerView(carouselproductcardR.id.carouselProductCardRecyclerView)
                .atPositionOnView(position, productcardR.id.textViewProductName)
        ).check(matches(withText(name)))
    }

    fun assertProductWidgetPostPurchaseRecommendationName(position: Int, name: String) {
        onView(
            withRecyclerView(recommendation_widget_commonR.id.rv_recommendation_vertical)
                .atPositionOnView(position, productcardR.id.textViewProductName)
        ).check(matches(withText(name)))
    }
}
