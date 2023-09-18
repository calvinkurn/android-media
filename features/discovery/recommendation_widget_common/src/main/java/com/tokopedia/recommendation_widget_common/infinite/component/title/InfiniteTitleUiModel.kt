package com.tokopedia.recommendation_widget_common.infinite.component.title

import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel

data class InfiniteTitleUiModel(
    val title: String,
    override val isFullSpan: Boolean = true
) : InfiniteRecommendationUiModel {
    override fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean = true
    override fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean = true
}
