package com.tokopedia.review.feature.reading.presentation.uimodel

sealed class SortFilterType {
    object RatingFilter: SortFilterType()
    object TopicFilter: SortFilterType()
    object Sort: SortFilterType()
}