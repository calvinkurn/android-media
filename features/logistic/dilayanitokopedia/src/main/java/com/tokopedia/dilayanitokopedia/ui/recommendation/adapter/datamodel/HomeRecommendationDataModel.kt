package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel

data class HomeRecommendationDataModel(
    val homeRecommendations: List<HomeRecommendationVisitable> = listOf(),
    val isHasNextPage: Boolean = false
)
