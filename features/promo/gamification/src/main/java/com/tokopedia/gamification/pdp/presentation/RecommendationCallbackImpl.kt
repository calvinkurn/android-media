package com.tokopedia.gamification.pdp.presentation

import com.tokopedia.gamification.pdp.data.GamificationAnalytics
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import timber.log.Timber

class RecommendationCallbackImpl(val scratchCardId: () -> String?) : InfiniteRecommendationListener {
    override fun onClickProductCard(recommendationItem: RecommendationItem) {
        GamificationAnalytics.sendClickProductInProductRecommendationSectionEvent("{'direct_reward_id':'${scratchCardId()}', 'product_id':'${recommendationItem.productId}'}")
    }

    override fun onImpressProductCard(recommendationItem: RecommendationItem) {
        GamificationAnalytics.sendImpressProductRecommendationSectionEvent("{'direct_reward_id':'${scratchCardId()}', 'product_id':'${recommendationItem.productId}'}")
    }

    override fun onClickViewAll(recommendationWidget: RecommendationWidget) {
        Timber.d("onClickViewAll")
    }
}
