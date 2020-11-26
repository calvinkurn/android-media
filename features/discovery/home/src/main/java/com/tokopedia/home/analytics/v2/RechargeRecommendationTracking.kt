package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object RechargeRecommendationTracking : BaseTracking() {
    private const val RECHARGE_RECOMMENDATION_NAME = "digital bills"
    private const val RECHARGE_RECOMMENDATION_EVENT_CATEGORY = "homepage-digital"
    private const val RECHARGE_RECOMMENDATION_CLICK_EVENT = "clickHomepage"
    private const val CLICK_CLOSE_ON = "click close on %s"

    fun homeRechargeRecommendationImpressionTracker(
            trackingQueue: TrackingQueue,
            recommendation: RechargeRecommendationData
    ) {
        val productName = recommendation.applink
        trackingQueue.putEETracking(getBasicPromotionView(
                Event.PROMO_VIEW,
                RECHARGE_RECOMMENDATION_EVENT_CATEGORY,
                Action.IMPRESSION_ON.format(RECHARGE_RECOMMENDATION_NAME),
                productName,
                listOf(mapToPromotionTracker(recommendation))
        ) as? HashMap<String, Any>)
    }

    fun homeRechargeRecommendationOnClickTracker(
            trackingQueue: TrackingQueue,
            recommendation: RechargeRecommendationData
    ) {
        val productName = recommendation.applink
        val promotionClickData = DataLayer.mapOf(
                Event.KEY, Event.PROMO_CLICK,
                Category.KEY, RECHARGE_RECOMMENDATION_EVENT_CATEGORY,
                Action.KEY, Action.CLICK_ON.format(RECHARGE_RECOMMENDATION_NAME),
                Label.KEY, productName,
                Ecommerce.KEY, Ecommerce.getEcommercePromoClick(listOf(mapToPromotionTracker(recommendation)))
        )
        trackingQueue.putEETracking(promotionClickData as? HashMap<String, Any>)
    }

    fun homeRechargeRecommendationOnCloseTracker(recommendation: RechargeRecommendationData) {
        val productName = recommendation.applink
        getTracker().sendGeneralEvent(
                RECHARGE_RECOMMENDATION_CLICK_EVENT,
                RECHARGE_RECOMMENDATION_EVENT_CATEGORY,
                CLICK_CLOSE_ON.format(RECHARGE_RECOMMENDATION_NAME),
                productName
        )
    }

    private fun mapToPromotionTracker(model: RechargeRecommendationData): Promotion {
        val productName = model.applink
        return Promotion(
                id = model.contentID,
                creative = model.iconURL,
                name = "/ - p1 - $RECHARGE_RECOMMENDATION_NAME - $productName",
                position = "none/other")
    }
}