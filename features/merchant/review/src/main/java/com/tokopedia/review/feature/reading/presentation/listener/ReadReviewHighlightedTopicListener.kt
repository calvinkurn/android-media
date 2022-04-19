package com.tokopedia.review.feature.reading.presentation.listener

interface ReadReviewHighlightedTopicListener {
    fun onHighlightedTopicClicked(topicName: String, topicPosition: Int)
    fun onHighlightedTopicImpressed(topicName: String, topicPosition: Int)
}