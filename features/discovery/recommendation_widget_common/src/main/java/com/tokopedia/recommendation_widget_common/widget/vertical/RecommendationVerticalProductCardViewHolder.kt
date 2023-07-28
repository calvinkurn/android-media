package com.tokopedia.recommendation_widget_common.widget.vertical

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomVerticalProductcardBinding
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTracking
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationVerticalProductCardViewHolder(
    itemView: View,
    private val trackingQueue: TrackingQueue
) : AbstractViewHolder<RecommendationVerticalProductCardModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_recom_vertical_productcard
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalProductCardViewHolder"
    }

    private val binding = ItemRecomVerticalProductcardBinding.bind(itemView)

    override fun bind(element: RecommendationVerticalProductCardModel) {
        setupProductCardLayoutData(element.productModel)
        setupProductCardListener(element)
    }

    override fun bind(element: RecommendationVerticalProductCardModel, payloads: MutableList<Any>) {
        val payload = payloads.firstOrNull().takeIf { it is Map<*, *> } as? Map<*, *>
        if (payload.isNullOrEmpty()) {
            bind(element)
        } else {
            if (payload.shouldUpdateProductCard()) {
                setupProductCardLayoutData(element.productModel)
            }
            if (payload.shouldUpdateListeners()) {
                setupProductCardListener(element)
            }
        }
    }

    override fun onViewRecycled() {
        binding.productCardView.recycle()
    }

    private fun setupProductCardLayoutData(productModel: ProductCardModel) {
        binding.productCardView.setProductModel(productModel)
    }

    private fun setupProductCardListener(element: RecommendationVerticalProductCardModel) {
        with(binding.productCardView) {
            addOnImpressionListener(element.recomItem) { onProductCardImpressed(element) }
            setOnClickListener { onProductClicked(element) }
        }
    }

    private fun onProductCardImpressed(element: RecommendationVerticalProductCardModel) {
        if (element.recomItem.isTopAds) {
            TopAdsUrlHitter(binding.productCardView.context).hitImpressionUrl(
                CLASS_NAME,
                element.recomItem.trackerImageUrl,
                element.recomItem.productId.toString(),
                element.recomItem.name,
                element.recomItem.imageUrl,
                element.componentName
            )
        }

        RecommendationCarouselTracking.sendEventItemImpression(
            trackingQueue = trackingQueue,
            widget = element.recomWidget,
            item = element.recomItem,
            trackingModel = element.trackingModel
        )
    }

    private fun onProductClicked(element: RecommendationVerticalProductCardModel) {
        val productRecommendation = element
            .recomWidget
            .recommendationItemList
            .getOrNull(bindingAdapterPosition) ?: return

        if (element.recomItem.isTopAds) {
            TopAdsUrlHitter(binding.productCardView.context).hitClickUrl(
                CLASS_NAME,
                productRecommendation.clickUrl,
                productRecommendation.productId.toString(),
                productRecommendation.name,
                productRecommendation.imageUrl
            )
        }

        RecommendationCarouselTracking.sendEventItemClick(
            widget = element.recomWidget,
            item = element.recomItem,
            trackingModel = element.trackingModel
        )

        RouteManager.route(binding.productCardView.context, productRecommendation.appUrl)
    }

    private fun Map<*, *>.shouldUpdateProductCard(): Boolean {
        return containsKey(RecommendationVerticalProductCardModel.PAYLOAD_FLAG_SHOULD_UPDATE_PRODUCT_CARD)
    }

    private fun Map<*, *>.shouldUpdateListeners(): Boolean {
        return containsKey(RecommendationVerticalProductCardModel.PAYLOAD_FLAG_SHOULD_UPDATE_LISTENERS)
    }
}
