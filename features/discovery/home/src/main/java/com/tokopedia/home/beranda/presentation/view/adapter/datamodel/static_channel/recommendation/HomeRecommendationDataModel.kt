package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable

data class HomeRecommendationDataModel(
    val homeRecommendations: List<Visitable<HomeRecommendationVisitable>> = emptyList(),
    val isHasNextPage: Boolean = false
)
