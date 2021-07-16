package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView

interface SearchCategoryRecommendationCarouselListener {

    fun onBindRecommendationCarousel(element: RecommendationCarouselDataView, adapterPosition: Int)

    fun onImpressed()

    fun onClicked()

    fun onATCNonVariant()

    fun onAddVariant()
}