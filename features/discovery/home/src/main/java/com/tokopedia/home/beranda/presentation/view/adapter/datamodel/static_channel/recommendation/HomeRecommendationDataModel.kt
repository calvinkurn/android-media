package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable

data class HomeRecommendationDataModel(
    val homeRecommendations: List<HomeRecommendationVisitable> = emptyList(),
    val isHasNextPage: Boolean = false
)
