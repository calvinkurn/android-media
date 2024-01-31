package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.foryou.utils.RecomTemporary

data class HomeRecommendationDataModel(
    val homeRecommendations: List<BaseHomeRecommendationVisitable> = emptyList(),
    val isHasNextPage: Boolean = false
)

@RecomTemporary
data class HomeGlobalRecommendationDataModel(
    val homeRecommendations: List<ForYouRecommendationVisitable> = emptyList(),
    val isHasNextPage: Boolean = false
)
