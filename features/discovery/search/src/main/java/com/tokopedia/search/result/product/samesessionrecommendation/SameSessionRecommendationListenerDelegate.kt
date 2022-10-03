package com.tokopedia.search.result.product.samesessionrecommendation

import com.tokopedia.iris.Iris
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback.FeedbackItem
import com.tokopedia.track.TrackApp

class SameSessionRecommendationListenerDelegate(
    private val sameSessionRecommendationPresenterDelegate: SameSessionRecommendationPresenterDelegate,
    private val iris: Iris,
) : SameSessionRecommendationListener {

    override fun onSameSessionRecommendationImpressed(
        recommendationDataView: SameSessionRecommendationDataView
    ) {
        recommendationDataView.impress(iris)
    }

    override fun onSameSessionRecommendationFeedbackItemImpressed(feedbackItem: FeedbackItem) {
        feedbackItem.impress(iris)
    }

    override fun onSameSessionRecommendationFeedbackItemClicked(
        feedback: Feedback,
        feedbackItem: FeedbackItem
    ) {
        feedbackItem.click(TrackApp.getInstance().gtm)
        sameSessionRecommendationPresenterDelegate.handleFeedbackItemClick(feedback, feedbackItem)
    }
}
