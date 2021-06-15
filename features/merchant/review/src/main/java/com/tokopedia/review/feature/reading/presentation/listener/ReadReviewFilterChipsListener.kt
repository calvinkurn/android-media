package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.review.feature.reading.data.ProductTopic

interface ReadReviewFilterChipsListener {
    fun onFilterWithAttachmentClicked(isActive: Boolean)
    fun onFilterWithTopicClicked(topics: List<ProductTopic>, isActive: Boolean)
    fun onFilterWithRatingClicked(isActive: Boolean)
    fun onSortClicked()
}