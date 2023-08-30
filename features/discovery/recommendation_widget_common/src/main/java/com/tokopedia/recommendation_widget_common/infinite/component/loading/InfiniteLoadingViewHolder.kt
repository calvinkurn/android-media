package com.tokopedia.recommendation_widget_common.infinite.component.loading

import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteLoadingBinding
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder

class InfiniteLoadingViewHolder(
    binding: ItemInfiniteLoadingBinding
) : InfiniteRecommendationViewHolder<InfiniteLoadingUiModel>(binding.root) {
    override fun bind(item: InfiniteLoadingUiModel) {}
}
