package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

data class HomeRecommendationDataModel(
    val homeRecommendations: List<BaseHomeRecommendationVisitable> = emptyList(),
    val isHasNextPage: Boolean = false
)
