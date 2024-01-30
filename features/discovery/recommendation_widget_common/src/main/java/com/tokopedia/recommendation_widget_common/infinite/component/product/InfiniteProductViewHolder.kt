package com.tokopedia.recommendation_widget_common.infinite.component.product

import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteProductBinding
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationCallback
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder

class InfiniteProductViewHolder(
    private val binding: ItemInfiniteProductBinding,
    private val callback: InfiniteRecommendationCallback,
) : InfiniteRecommendationViewHolder<InfiniteProductUiModel>(binding.root) {

    override fun bind(item: InfiniteProductUiModel) = with(binding.infiniteProductCard) {
        val recommendationItem = item.recommendationItem
        setProductModel(recommendationItem.toProductCardModel())
        addOnImpressionListener(item.impressHolder) {
            callback.onImpressProductCard(recommendationItem)
        }
        setOnClickListener {
            callback.onClickProductCard(recommendationItem)
        }
    }
}
