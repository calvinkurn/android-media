package com.tokopedia.recommendation_widget_common.infinite.component.product

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class InfiniteProductUiModel(
    val recommendationItem: RecommendationItem,
    val appLogAdditionalParam: AppLogAdditionalParam = AppLogAdditionalParam.None
) : InfiniteRecommendationUiModel {

    override val isFullSpan: Boolean = false
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean {
        return newItem is InfiniteProductUiModel && recommendationItem == newItem.recommendationItem
    }

    override fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean {
        return newItem is InfiniteProductUiModel && recommendationItem.productId == newItem.recommendationItem.productId
    }
}
