package com.tokopedia.recommendation_widget_common.presentation.model

class RecommendationModel(
        val recommendationItemList: List<RecommendationItem>,
        val title: String,
        val foreignTitle: String,
        val source: String,
        val tid: String,
        val widgetUrl: String) {
}