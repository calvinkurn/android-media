package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel

interface SearchCategoryRecommendationCarouselListener {

    fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int)

    fun onGetCarouselScrollPosition(adapterPosition: Int): Int

    fun onBindRecommendationCarousel(
            element: TokoNowRecommendationCarouselUiModel,
            adapterPosition: Int,
    )

    fun onImpressedRecommendationCarouselItem(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int,
    )

    fun onClickRecommendationCarouselItem(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
    )

    fun onATCNonVariantRecommendationCarouselItem(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            recommendationCarouselPosition: Int,
            quantity: Int,
    )

    fun onAddVariantRecommendationCarouselItem(
            recommendationCarouselDataView: TokoNowRecommendationCarouselUiModel?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
    )
}