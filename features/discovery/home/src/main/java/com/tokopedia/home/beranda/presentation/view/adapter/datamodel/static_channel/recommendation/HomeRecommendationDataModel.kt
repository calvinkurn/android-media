package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

data class HomeRecommendationDataModel(
    val homeRecommendations: List<Visitable<HomeRecommendationTypeFactoryImpl>> = emptyList(),
    val isHasNextPage: Boolean = false
)
