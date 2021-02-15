package com.tokopedia.recommendation_widget_common.presentation.model

import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity

data class RecommendationWidget(
        val recommendationItemList: List<RecommendationItem> = listOf(),
        val title: String = "",
        val foreignTitle: String = "",
        val source: String = "",
        val tid: String = "",
        val widgetUrl: String = "",
        val layoutType: String = "",
        val seeMoreAppLink: String = "",
        val currentPage: Int = 0,
        val nextPage: Int = 0,
        val prevPage: Int = 0,
        val hasNext: Boolean = false,
        val pageName: String = "",
        val recommendationFilterChips: List<RecommendationFilterChipsEntity.RecommendationFilterChip> = listOf()
)