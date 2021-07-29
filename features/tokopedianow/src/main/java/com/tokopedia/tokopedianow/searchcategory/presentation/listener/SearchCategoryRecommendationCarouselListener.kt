package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView

interface SearchCategoryRecommendationCarouselListener {

    fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int)

    fun onGetCarouselScrollPosition(adapterPosition: Int): Int

    fun onBindRecommendationCarousel(
            element: RecommendationCarouselDataView,
            adapterPosition: Int,
    )

    fun onImpressedRecommendationCarouselItem(
            recommendationCarouselDataView: RecommendationCarouselDataView?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
    )

    fun onClickRecommendationCarouselItem(
            recommendationCarouselDataView: RecommendationCarouselDataView?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            itemPosition: Int,
            adapterPosition: Int
    )

    fun onATCNonVariantRecommendationCarouselItem(
            recommendationCarouselDataView: RecommendationCarouselDataView?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
            recommendationCarouselPosition: Int,
            quantity: Int,
    )

    fun onAddVariantRecommendationCarouselItem(
            recommendationCarouselDataView: RecommendationCarouselDataView?,
            data: RecommendationCarouselData,
            recomItem: RecommendationItem,
    )
}