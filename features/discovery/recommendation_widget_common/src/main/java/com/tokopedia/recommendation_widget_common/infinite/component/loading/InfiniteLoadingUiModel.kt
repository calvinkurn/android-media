package com.tokopedia.recommendation_widget_common.infinite.component.loading

import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel

object InfiniteLoadingUiModel : InfiniteRecommendationUiModel() {
    override fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean {
        return true
    }

    override fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean {
        return true
    }
}
