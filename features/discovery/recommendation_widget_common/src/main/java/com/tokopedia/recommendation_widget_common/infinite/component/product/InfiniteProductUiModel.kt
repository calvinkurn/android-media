package com.tokopedia.recommendation_widget_common.infinite.component.product

import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class InfiniteProductUiModel(
    val recommendationItem: RecommendationItem
) : InfiniteRecommendationUiModel() {
    override fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean {
        return newItem is InfiniteProductUiModel && recommendationItem == newItem.recommendationItem
    }

    override fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean {
        return newItem is InfiniteProductUiModel && recommendationItem.productId == newItem.recommendationItem.productId
    }
}
