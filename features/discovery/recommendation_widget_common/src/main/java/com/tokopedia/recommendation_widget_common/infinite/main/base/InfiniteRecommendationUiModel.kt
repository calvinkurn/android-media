package com.tokopedia.recommendation_widget_common.infinite.main.base

abstract class InfiniteRecommendationUiModel {
    abstract val isFullSpan: Boolean
    abstract fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean
    abstract fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean
}
