package com.tokopedia.home.analytics.v2

import android.net.Uri
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object RechargeRecommendationTracking : BaseTracking() {
    private const val RECHARGE_RECOMMENDATION_NAME = "digital bills"
    private const val RECHARGE_RECOMMENDATION_EVENT_CATEGORY = "homepage-digital"
    private const val RECHARGE_RECOMMENDATION_CLICK_EVENT = "clickHomepage"
    private const val CLICK_CLOSE_ON = "click close on %s"
    private const val LABEL_FORMAT = "%s - %s - %s"
    private const val PRODUCT_ID = "product_id"
    private const val CATEGORY_ID = "category_id"

    fun homeRechargeRecommendationImpressionTracker(
            trackingQueue: TrackingQueue,
            recommendation: RechargeRecommendationData
    ) {
        val label = getLabel(recommendation.applink, recommendation.iconURL)
        trackingQueue.putEETracking(getBasicPromotionView(
                Event.PROMO_VIEW,
                RECHARGE_RECOMMENDATION_EVENT_CATEGORY,
                Action.IMPRESSION_ON.format(RECHARGE_RECOMMENDATION_NAME),
                label,
                listOf(mapToPromotionTracker(recommendation))
        ) as? HashMap<String, Any>)
    }

    fun homeRechargeRecommendationOnClickTracker(
            trackingQueue: TrackingQueue,
            recommendation: RechargeRecommendationData
    ) {
        val label = getLabel(recommendation.applink, recommendation.iconURL)
        val promotionClickData = DataLayer.mapOf(
                Event.KEY, Event.PROMO_CLICK,
                Category.KEY, RECHARGE_RECOMMENDATION_EVENT_CATEGORY,
                Action.KEY, Action.CLICK_ON.format(RECHARGE_RECOMMENDATION_NAME),
                Label.KEY, label,
                Ecommerce.KEY, Ecommerce.getEcommercePromoClick(listOf(mapToPromotionTracker(recommendation)))
        )
        trackingQueue.putEETracking(promotionClickData as? HashMap<String, Any>)
    }

    fun homeRechargeRecommendationOnCloseTracker(recommendation: RechargeRecommendationData) {
        val label = getLabel(recommendation.applink, recommendation.iconURL)
        getTracker().sendGeneralEvent(
                RECHARGE_RECOMMENDATION_CLICK_EVENT,
                RECHARGE_RECOMMENDATION_EVENT_CATEGORY,
                CLICK_CLOSE_ON.format(RECHARGE_RECOMMENDATION_NAME),
                label
        )
    }

    private fun mapToPromotionTracker(model: RechargeRecommendationData): Promotion {
        val productName = model.iconURL
        return Promotion(
                id = model.contentID,
                creative = model.iconURL,
                name = "/ - p1 - $RECHARGE_RECOMMENDATION_NAME - $productName",
                position = "none/other")
    }

    private fun getIdsMap(url: String): Pair<String, String> {
        val uri = Uri.parse(url)
        val categoryId = uri.getQueryParameter(CATEGORY_ID) ?: ""
        val productId = uri.getQueryParameter(PRODUCT_ID) ?: ""

        return Pair(categoryId, productId)
    }

    private fun getLabel(url: String, iconUrl: String): String {
        val ids = getIdsMap(url)
        return LABEL_FORMAT.format(ids.first, ids.second, iconUrl)
    }
}
