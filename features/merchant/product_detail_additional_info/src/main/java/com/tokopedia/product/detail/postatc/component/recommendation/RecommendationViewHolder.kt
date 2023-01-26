package com.tokopedia.product.detail.postatc.component.recommendation

import com.tokopedia.product.detail.databinding.ItemRecommendationBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener

class RecommendationViewHolder(
    private val binding: ItemRecommendationBinding,
    private val listener: PostAtcListener
) : PostAtcViewHolder<RecommendationUiModel>(binding.root), RecomCarouselWidgetBasicListener, RecommendationCarouselTokonowListener {

    override fun bind(element: RecommendationUiModel) {
        binding.apply {
            val widget = element.widget

            if (widget == null) {
                postAtcRecommCarousel.bind(
                    basicListener = this@RecommendationViewHolder,
                    tokonowListener = this@RecommendationViewHolder
                )
                listener.fetchRecommendation(element.name)
            } else {
                postAtcRecommCarousel.bind(
                    carouselData = RecommendationCarouselData(
                        recommendationData = widget,
                        state = RecommendationCarouselData.STATE_READY
                    ),
                    adapterPosition = 0,
                    basicListener = this@RecommendationViewHolder,
                    tokonowListener = this@RecommendationViewHolder
                )
            }
        }
    }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) {
    }

    override fun onSeeAllBannerClicked(data: RecommendationCarouselData, applink: String) {
        listener.goToAppLink(applink)
    }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) {
    }

    override fun onRecomProductCardImpressed(data: RecommendationCarouselData, recomItem: RecommendationItem, itemPosition: Int, adapterPosition: Int) {
    }

    override fun onRecomProductCardClicked(data: RecommendationCarouselData, recomItem: RecommendationItem, applink: String, itemPosition: Int, adapterPosition: Int) {
        listener.goToProduct(recomItem.productId.toString())
    }

    override fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int) {
    }

    override fun onRecomBannerClicked(data: RecommendationCarouselData, applink: String, adapterPosition: Int) {
    }

    override fun onChannelWidgetEmpty() {
    }

    override fun onWidgetFail(pageName: String, e: Throwable) {
    }

    override fun onShowError(pageName: String, e: Throwable) {
    }

    override fun onRecomProductCardAddToCartNonVariant(data: RecommendationCarouselData, recomItem: RecommendationItem, adapterPosition: Int, quantity: Int) {
    }

    override fun onRecomProductCardAddVariantClick(data: RecommendationCarouselData, recomItem: RecommendationItem, adapterPosition: Int) {
    }
}
