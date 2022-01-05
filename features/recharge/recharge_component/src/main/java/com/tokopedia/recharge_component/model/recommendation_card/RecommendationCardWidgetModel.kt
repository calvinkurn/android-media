package com.tokopedia.recharge_component.model.recommendation_card

data class RecommendationCardWidgetModel(
    val layoutType: RecommendationCardEnum = RecommendationCardEnum.BIG,
    val imageUrl: String = "",
    val title: String = "",
    val price: String = "",
    val appUrl: String = "",
    val dueDate: String = "",
    val productType: String = "",
    val productExpired: String = ""
)