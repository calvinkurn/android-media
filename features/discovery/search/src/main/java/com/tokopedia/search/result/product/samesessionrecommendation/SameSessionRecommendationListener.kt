package com.tokopedia.search.result.product.samesessionrecommendation

interface SameSessionRecommendationListener {
    fun onSameSessionRecommendationImpressed(recommendationDataView: SameSessionRecommendationDataView)
    fun onSameSessionRecommendationFeedbackItemImpressed(feedbackItem: SameSessionRecommendationDataView.Feedback.FeedbackItem)
    fun onSameSessionRecommendationFeedbackItemClicked(feedbackItem: SameSessionRecommendationDataView.Feedback.FeedbackItem)
}
