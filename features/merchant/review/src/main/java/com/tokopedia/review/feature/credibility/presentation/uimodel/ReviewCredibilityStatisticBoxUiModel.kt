package com.tokopedia.review.feature.credibility.presentation.uimodel

data class ReviewCredibilityStatisticBoxUiModel(
    val title: String, val statistics: List<ReviewCredibilityStatisticUiModel>
) {
    data class ReviewCredibilityStatisticUiModel(
        val icon: String, val title: String, val count: String
    )
}