package com.tokopedia.recommendation_widget_common.infinite.main.base

import com.tokopedia.kotlin.model.ImpressHolder

interface InfiniteRecommendationUiModel {
    val isFullSpan: Boolean
    val impressHolder: ImpressHolder
    fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean
    fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean
}
