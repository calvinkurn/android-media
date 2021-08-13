package com.tokopedia.home_recom.model.entity

import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity

/**
 * Created by Lukas on 12/6/20.
 */
data class FilterSortChip (
        val filterAndSort: RecommendationFilterChipsEntity.FilterAndSort = RecommendationFilterChipsEntity.FilterAndSort(),
        val quickFilterList: List<RecommendationFilterChipsEntity.RecommendationFilterChip> = listOf()
)