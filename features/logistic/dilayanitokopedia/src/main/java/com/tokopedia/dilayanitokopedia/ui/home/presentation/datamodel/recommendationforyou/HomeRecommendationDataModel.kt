package com.tokopedia.dilayanitokopedia.ui.home.presentation.datamodel.recommendationforyou

import com.tokopedia.dilayanitokopedia.ui.home.presentation.uimodel.HomeRecommendationVisitable

data class HomeRecommendationDataModel(
    val homeRecommendations: List<HomeRecommendationVisitable> = listOf(),
    val isHasNextPage: Boolean = false
)
