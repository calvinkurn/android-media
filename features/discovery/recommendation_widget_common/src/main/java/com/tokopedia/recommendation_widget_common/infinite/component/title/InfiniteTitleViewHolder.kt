package com.tokopedia.recommendation_widget_common.infinite.component.title

import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteTitleBinding
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder

class InfiniteTitleViewHolder(
    private val binding: ItemInfiniteTitleBinding
) : InfiniteRecommendationViewHolder<InfiniteTitleUiModel>(binding.root) {
    override fun bind(item: InfiniteTitleUiModel) {
        binding.infiniteTitle.text = item.title
    }
}
