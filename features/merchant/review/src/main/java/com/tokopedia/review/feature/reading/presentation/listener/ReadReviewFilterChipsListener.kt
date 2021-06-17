package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.review.feature.reading.data.ProductTopic

interface ReadReviewFilterChipsListener {
    fun onFilterWithAttachmentClicked()
    fun onFilterWithTopicClicked(topics: List<ProductTopic>)
    fun onFilterWithRatingClicked()
    fun onSortClicked(chipTitle: String)
}