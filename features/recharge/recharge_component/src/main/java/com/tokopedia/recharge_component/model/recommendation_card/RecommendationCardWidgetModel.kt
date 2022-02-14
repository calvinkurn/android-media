package com.tokopedia.recharge_component.model.recommendation_card

import com.tokopedia.kotlin.model.ImpressHolder

data class RecommendationCardWidgetModel(
    val layoutType: RecommendationCardEnum = RecommendationCardEnum.BIG,
    val imageUrl: String = "",
    val title: String = "",
    val price: String = "",
    val appUrl: String = "",
    val productType: String = "",
    val productExpired: String = "",
    val id: String = "",
    val clientNumber: String = "",
    val categoryId: String = "",
    val operatorId: String = "",
    val productId: String = ""
): ImpressHolder()