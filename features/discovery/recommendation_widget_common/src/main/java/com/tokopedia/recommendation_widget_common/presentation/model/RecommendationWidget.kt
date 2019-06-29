package com.tokopedia.recommendation_widget_common.presentation.model

class RecommendationWidget(
        val recommendationItemList: List<RecommendationItem>,
        val title: String,
        val foreignTitle: String,
        val source: String,
        val tid: String,
        val widgetUrl: String,
        val layoutType: String,
        val currentPage: Int,
        val nextPage: Int,
        val prevPage: Int,
        val hasNext: Boolean,
        val pageName: String) {
}