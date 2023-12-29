package com.tokopedia.recommendation_widget_common.infinite.utils

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel

object InfiniteDiffItemCallback : DiffUtil.ItemCallback<InfiniteRecommendationUiModel>() {
    override fun areItemsTheSame(
        oldItem: InfiniteRecommendationUiModel,
        newItem: InfiniteRecommendationUiModel
    ): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(
        oldItem: InfiniteRecommendationUiModel,
        newItem: InfiniteRecommendationUiModel
    ): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }
}
