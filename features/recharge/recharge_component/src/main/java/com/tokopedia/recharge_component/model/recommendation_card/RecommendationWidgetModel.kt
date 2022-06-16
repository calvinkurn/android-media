package com.tokopedia.recharge_component.model.recommendation_card

data class RecommendationWidgetModel(
    val title: String = "",
    val recommendations: List<RecommendationCardWidgetModel> = listOf()
)