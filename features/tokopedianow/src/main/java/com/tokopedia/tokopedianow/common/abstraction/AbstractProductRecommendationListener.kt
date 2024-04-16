package com.tokopedia.tokopedianow.common.abstraction

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder

abstract class AbstractProductRecommendationListener: TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener, TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener {
    override fun onImpressedRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) { /* do nothing */ }

    override fun onATCNonVariantRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        recommendationCarouselPosition: Int,
        quantity: Int
    ) { /* do nothing */ }

    override fun onAddVariantRecommendationCarouselItem(
        model: TokoNowProductRecommendationOocUiModel?,
        data: RecommendationCarouselData,
        recomItem: RecommendationItem
    ) { /* do nothing */ }

    override fun onBindRecommendationCarousel(
        model: TokoNowProductRecommendationOocUiModel,
        adapterPosition: Int
    ) { /* do noting */ }

    override fun onMiniCartUpdatedFromRecomWidget(miniCartSimplifiedData: MiniCartSimplifiedData) { /* nothing to do */ }

    override fun onRecomTokonowAtcSuccess(message: String) { /* nothing to do */ }

    override fun onRecomTokonowAtcFailed(throwable: Throwable) { /* nothing to do */ }

    override fun onRecomTokonowAtcNeedToSendTracker(recommendationItem: RecommendationItem) { /* nothing to do */ }

    override fun onRecomTokonowDeleteNeedToSendTracker(recommendationItem: RecommendationItem) { /* nothing to do */ }

    override fun onClickItemNonLoginState() { /* nothing to do */ }

    override fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int) { /* do nothing */ }

    override fun onGetCarouselScrollPosition(adapterPosition: Int): Int = 0
}
