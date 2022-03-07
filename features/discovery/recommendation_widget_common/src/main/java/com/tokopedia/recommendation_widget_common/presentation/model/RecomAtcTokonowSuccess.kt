package com.tokopedia.recommendation_widget_common.presentation.model

/**
 * Created by yfsx on 06/10/21.
 */
data class RecomAtcTokonowResponse(
    val recomItem: RecommendationItem = RecommendationItem(),
    val message: String = "",
    val error: Throwable? = null
)