package com.tokopedia.search.result.product.samesessionrecommendation

import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback.FeedbackItem

interface SameSessionRecommendationListener {
    fun onSameSessionRecommendationImpressed(recommendationDataView: SameSessionRecommendationDataView)
    fun onSameSessionRecommendationFeedbackItemImpressed(feedbackItem: FeedbackItem)
    fun onSameSessionRecommendationFeedbackItemClicked(
        feedback: Feedback,
        feedbackItem: FeedbackItem
    )
}
