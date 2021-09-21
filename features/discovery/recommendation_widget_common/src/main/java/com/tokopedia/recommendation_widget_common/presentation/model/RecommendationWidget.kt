package com.tokopedia.recommendation_widget_common.presentation.model

import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity

data class RecommendationWidget(
        var recommendationItemList: List<RecommendationItem> = listOf(),
        val title: String = "",
        val subtitle: String = "",
        val foreignTitle: String = "",
        val source: String = "",
        val tid: String = "",
        val widgetUrl: String = "",
        var layoutType: String = "",
        val seeMoreAppLink: String = "",
        val currentPage: Int = 0,
        val nextPage: Int = 0,
        val prevPage: Int = 0,
        val hasNext: Boolean = false,
        val pageName: String = "",
        val recommendationFilterChips: List<RecommendationFilterChipsEntity.RecommendationFilterChip> = listOf(),
        val titleColor: String = "",
        val subtitleColor: String = "",
        val expiredTime: String = "",
        val serverTimeUnix: Long = 0,
        val headerBackImage: String = "",
        val headerBackColor: String = "",
        val recommendationConfig: RecommendationConfig = RecommendationConfig(),
        var recommendationBanner: RecommendationBanner? = null,
        //for recom PDP since there is possibility gql return empty page name and recom list
        var recomUiPageName: String = ""
) {
    fun copyRecomItemList(): List<RecommendationItem> {
        val newList = mutableListOf<RecommendationItem>()
        recommendationItemList.forEach { newList.add(it.copy()) }
        return newList
    }
}