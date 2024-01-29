package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationVisitable

data class HomeRecommendationDataModel(
    val homeRecommendations: List<ForYouRecommendationVisitable> = emptyList(),
    val isHasNextPage: Boolean = false
)
