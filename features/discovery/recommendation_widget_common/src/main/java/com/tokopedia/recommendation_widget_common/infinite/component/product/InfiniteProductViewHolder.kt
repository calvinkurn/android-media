package com.tokopedia.recommendation_widget_common.infinite.component.product

import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteProductBinding
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder

class InfiniteProductViewHolder(
    private val binding: ItemInfiniteProductBinding
) : InfiniteRecommendationViewHolder<InfiniteProductUiModel>(binding.root) {

    override fun bind(item: InfiniteProductUiModel) {
        binding.infiniteProductCard.apply {
            setProductModel(item.recommendationItem.toProductCardModel())
        }
    }

}
