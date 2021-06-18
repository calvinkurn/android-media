package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.review.feature.reading.data.ProductTopic

interface ReadReviewFilterChipsListener {
    fun onFilterWithAttachmentClicked(isActive: Boolean)
    fun onFilterWithTopicClicked(topics: List<ProductTopic>, index: Int)
    fun onFilterWithRatingClicked(index: Int)
    fun onSortClicked(chipTitle: String)
}