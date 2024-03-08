package com.tokopedia.recommendation_widget_common.infinite.component.title

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class InfiniteTitleUiModel(
    val recommendationWidget: RecommendationWidget,
) : InfiniteRecommendationUiModel {
    override val isFullSpan: Boolean = true
    override val impressHolder: ImpressHolder = ImpressHolder()
    override fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean = true
    override fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean = true
}
