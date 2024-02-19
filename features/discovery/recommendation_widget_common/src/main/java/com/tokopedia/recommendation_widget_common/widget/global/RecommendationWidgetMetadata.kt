package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam.Companion.X_SOURCE_DEFAULT_VALUE

data class RecommendationWidgetMetadata(
    val pageSource: String = "",
    val pageType: String = "",
    val pageName: String = "",
    val verticalPosition: Int = 0,
    val productIds: List<String> = listOf(),
    val categoryIds: List<String> = listOf(),
    val keyword: List<String> = listOf(),
    val queryParam: String = "",
    val pageNumber: Int = 1,
    val device: String = "",
    val isTokonow: Boolean = false,
    val criteriaThematicIDs: List<String> = listOf(),
    val xSource: String = X_SOURCE_DEFAULT_VALUE
)
