package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel

interface RechargeRecommendationRepository {
    suspend fun getRecommendations(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        dgOperatorIds: List<Int> = emptyList(),
        isBigRecommendation: Boolean = false
    ): RecommendationWidgetModel
}