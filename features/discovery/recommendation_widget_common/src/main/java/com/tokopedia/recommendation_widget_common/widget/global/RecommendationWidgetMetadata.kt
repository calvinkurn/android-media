package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.minicart.common.domain.usecase.MiniCartSource

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
    val miniCartSource: MiniCartSource? = null,
    val device: String = "",
    val isTokonow: Boolean = false,
)
