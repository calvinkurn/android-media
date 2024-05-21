package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog

data class HomeGlobalRecommendationDataModel(
    val homeRecommendations: List<ForYouRecommendationVisitable> = emptyList(),
    val isHasNextPage: Boolean = false,
    val appLog: RecommendationAppLog = RecommendationAppLog(),
)
