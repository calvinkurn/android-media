package com.tokopedia.recommendation_widget_common.infinite.component.product

import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.databinding.ItemInfiniteProductBinding
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationCallback
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class InfiniteProductViewHolder(
    private val binding: ItemInfiniteProductBinding,
    private val callback: InfiniteRecommendationCallback,
) : InfiniteRecommendationViewHolder<InfiniteProductUiModel>(binding.root) {

    override fun bind(item: InfiniteProductUiModel) = with(binding.infiniteProductCard) {
        val recommendationItem = item.recommendationItem
        setProductModel(recommendationItem.toProductCardModel())
        addOnImpressionListener(item.impressHolder) {
            if (recommendationItem.isTopAds) hitTopAdsImpression(recommendationItem)
            callback.onImpressProductCard(recommendationItem)
        }
        setOnClickListener {
            if (recommendationItem.isTopAds) hitTopAdsClick(recommendationItem)
            callback.onClickProductCard(recommendationItem)
        }
    }

    private fun hitTopAdsImpression(recommendationItem: RecommendationItem) {
        TopAdsUrlHitter(itemView.context).hitImpressionUrl(
            this@InfiniteProductViewHolder::class.java.simpleName,
            recommendationItem.trackerImageUrl,
            recommendationItem.productId.toString(),
            recommendationItem.name,
            recommendationItem.imageUrl
        )
    }

    private fun hitTopAdsClick(recommendationItem: RecommendationItem) {
        TopAdsUrlHitter(itemView.context).hitClickUrl(
            this@InfiniteProductViewHolder::class.java.simpleName,
            recommendationItem.clickUrl,
            recommendationItem.productId.toString(),
            recommendationItem.name,
            recommendationItem.imageUrl
        )
    }
}
