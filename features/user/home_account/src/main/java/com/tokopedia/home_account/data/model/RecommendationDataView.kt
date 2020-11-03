package com.tokopedia.home_account.data.model

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class RecommendationDataView (
        var title: String = "",
        var items: List<RecommendationItem> = listOf(),
        var isExpanded: Boolean = false,
        var showArrowDown: Boolean = false
)