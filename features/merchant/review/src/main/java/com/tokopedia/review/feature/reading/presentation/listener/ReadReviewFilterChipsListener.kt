package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.review.feature.reading.data.ProductTopic

interface ReadReviewFilterChipsListener {
    fun onFilterWithAttachmentClicked(isActive: Boolean)
    fun onFilterWithTopicClicked(topics: List<ProductTopic>, index: Int, isActive: Boolean)
    fun onFilterWithRatingClicked(index: Int, isActive: Boolean)
    fun onSortClicked(chipTitle: String)
    fun onClearFiltersClicked()
}