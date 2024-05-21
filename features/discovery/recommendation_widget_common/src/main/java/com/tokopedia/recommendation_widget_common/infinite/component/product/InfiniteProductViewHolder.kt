package com.tokopedia.recommendation_widget_common.infinite.component.product

import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
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
) : InfiniteRecommendationViewHolder<InfiniteProductUiModel>(binding.root), AppLogRecTriggerInterface {

    private var recTriggerObject = RecommendationTriggerObject()

    override fun bind(item: InfiniteProductUiModel) = with(binding.infiniteProductCard) {
        val recommendationItem = item.recommendationItem
        setProductModel(recommendationItem.toProductCardModel())
        updateRecTriggerObject(item)
        addOnImpressionListener(item.impressHolder) {
            if (recommendationItem.isTopAds) hitTopAdsImpression(recommendationItem)
            callback.onImpressProductCard(
                recommendationItem,
                item.appLogAdditionalParam
            )
        }
        setOnClickListener {
            if (recommendationItem.isTopAds) hitTopAdsClick(recommendationItem)
            callback.onClickProductCard(
                recommendationItem,
                item.appLogAdditionalParam
            )
        }
    }

    private fun updateRecTriggerObject(model: InfiniteProductUiModel) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.recommendationItem.appLog.sessionId,
            requestId = model.recommendationItem.appLog.requestId,
            moduleName = model.recommendationItem.pageName,
            additionalParam = model.appLogAdditionalParam,
        )
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

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject? {
        return recTriggerObject
    }
}
