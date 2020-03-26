package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object RechargeRecommendationTracking : BaseTracking() {
    private const val RECHARGE_RECOMMENDATION_NAME = "digital bills"
    private const val RECHARGE_RECOMMENDATION_EVENT_CATEGORY = "homepage-digital"
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
        trackingQueue.putEETracking(getBasicPromotionClick(
                Event.PROMO_CLICK,
                RECHARGE_RECOMMENDATION_EVENT_CATEGORY,
                Action.CLICK_ON.format(RECHARGE_RECOMMENDATION_NAME),
                productName,
                "", "", "", "", "",
                listOf(mapToPromotionTracker(recommendation))
        ) as? HashMap<String, Any>)
    }

    fun homeRechargeRecommendationOnCloseTracker(recommendation: RechargeRecommendationData) {
        val productName = recommendation.applink
        getTracker().sendGeneralEvent(
                "clickHomepage",
                RECHARGE_RECOMMENDATION_EVENT_CATEGORY,
                CLICK_CLOSE_ON.format(RECHARGE_RECOMMENDATION_NAME),
                productName
        )
    }

    override fun getBasicPromotionClick(event: String, eventCategory: String, eventAction: String, eventLabel: String, channelId: String, affinity: String, attribution: String, categoryId: String, shopId: String, promotions: List<Promotion>): Map<String, Any> {
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommercePromoClick(promotions)
        )
    }

    private fun mapToPromotionTracker(model: RechargeRecommendationData) = Promotion(
            id = model.contentID,
            creative = "",
            name = "$RECHARGE_RECOMMENDATION_NAME - ${model.applink}",
            creativeUrl = model.iconURL,
            position = "0")
}