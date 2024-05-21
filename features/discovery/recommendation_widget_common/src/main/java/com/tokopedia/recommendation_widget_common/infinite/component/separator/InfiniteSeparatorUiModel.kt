package com.tokopedia.recommendation_widget_common.infinite.component.separator

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel

object InfiniteSeparatorUiModel : InfiniteRecommendationUiModel {
    override val isFullSpan: Boolean = true
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun areItemsTheSame(newItem: InfiniteRecommendationUiModel): Boolean {
        return true
    }

    override fun areContentsTheSame(newItem: InfiniteRecommendationUiModel): Boolean {
        return true
    }
}
