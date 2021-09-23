package com.tokopedia.review.feature.reading.presentation.uimodel

sealed class SortFilterBottomSheetType {
    object RatingFilterBottomSheet: SortFilterBottomSheetType()
    object TopicFilterBottomSheet: SortFilterBottomSheetType()
    object SortBottomSheet: SortFilterBottomSheetType()
}