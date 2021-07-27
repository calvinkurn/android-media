package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView

interface SearchCategoryRecommendationCarouselListener {

    fun onBindRecommendationCarousel(
            element: RecommendationCarouselDataView,
            adapterPosition: Int,
    )

    fun onImpressedRecommendationCarouselItem(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
    )

    fun onClickRecommendationCarouselItem(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
    )

    fun onATCNonVariantRecommendationCarouselItem(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            recommendationCarouselPosition: Int,
            quantity: Int,
    )

    fun onAddVariantRecommendationCarouselItem(
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
    )
}