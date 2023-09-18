package com.tokopedia.recommendation_widget_common.infinite.main.base

interface InfiniteRecommendationUiModel {
    val isFullSpan: Boolean
    fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean
    fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean
}
