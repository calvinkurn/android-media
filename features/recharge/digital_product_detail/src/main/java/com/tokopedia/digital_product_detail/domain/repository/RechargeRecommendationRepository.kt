package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

interface RechargeRecommendationRepository {
    suspend fun getRecommendations(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        isBigRecommendation: Boolean = false
    ): List<RecommendationCardWidgetModel>
}