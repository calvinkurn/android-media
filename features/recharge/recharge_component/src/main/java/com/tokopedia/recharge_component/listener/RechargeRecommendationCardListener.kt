package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel

interface RechargeRecommendationCardListener{
    fun onProductRecommendationCardClicked(recommendationCardWidgetModel: RecommendationCardWidgetModel, position: Int)
    fun onProductRecommendationCardImpression(recommendationCardWidgetModel: RecommendationCardWidgetModel, position: Int)
}