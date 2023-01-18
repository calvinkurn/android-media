package com.tokopedia.feedplus.view.analytics.shoprecom

import com.tokopedia.config.GlobalConfig
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * created by fachrizalmrsln on 17/10/22
 **/
class FeedShopRecomWidgetAnalytic @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) {

    fun sendImpressionShopRecommendationEvent(
        eventLabel: String,
        shops: ShopRecomUiModelItem,
        postPosition: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_NAME_PROMO_VIEW,
                action = EVENT_ACTION_IMPRESSION_SHOP_RECOMMENDATION,
                category = EVENT_CATEGORY_CONTENT_FEED_TIMELINE,
                label = eventLabel
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    EVENT_NAME_PROMO_VIEW to hashMapOf(
                        KEY_PROMOTIONS to listOf(
                            convertToPromotion(
                                creativeName = shops.logoImageURL,
                                creativeSlot = postPosition,
                                itemId = shops.id.toString(),
                                itemName = ITEM_NAME_SHOP_RECOMMENDATIONS_CAROUSEL
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                KEY_BUSINESS_UNIT to CONTENT,
                KEY_CURRENT_SITE to currentSite,
                KEY_SESSION_IRIS to sessionIris,
                KEY_USER_ID to userSession.userId,
                KEY_TRACKER_ID to "31272"
            )
        )
    }

    fun sendClickShopRecommendationEvent(
        eventLabel: String,
        shopId: String,
        shopsImageUrl: String,
        postPosition: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_NAME_PROMO_CLICK,
                category = EVENT_CATEGORY_CONTENT_FEED_TIMELINE,
                action = EVENT_ACTION_CLICK_SHOP_RECOMMENDATION,
                label = eventLabel
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    EVENT_NAME_PROMO_CLICK to hashMapOf(
                        KEY_PROMOTIONS to listOf(
                            convertToPromotion(
                                creativeName = shopsImageUrl,
                                creativeSlot = postPosition,
                                itemId = shopId,
                                itemName = ITEM_NAME_SHOP_RECOMMENDATIONS_CAROUSEL
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                KEY_CURRENT_SITE to currentSite,
                KEY_SESSION_IRIS to sessionIris,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to CONTENT,
                KEY_TRACKER_ID to "31273"
            )
        )
        trackingQueue.sendAll()
    }

    fun sendClickFollowShopRecommendationEvent(eventLabel: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_PG,
            KEY_EVENT_ACTION to EVENT_ACTION_CLICK_FOLLOW_SHOP_RECOMMENDATION,
            KEY_EVENT_CATEGORY to EVENT_CATEGORY_CONTENT_FEED_TIMELINE,
            KEY_EVENT_LABEL to eventLabel,
            KEY_CURRENT_SITE to currentSite,
            KEY_SESSION_IRIS to sessionIris,
            KEY_USER_ID to userSession.userId,
            KEY_BUSINESS_UNIT to CONTENT,
            KEY_TRACKER_ID to "31274"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun sendClickXShopRecommendationEvent(eventLabel: String) {
        val map = mapOf(
            KEY_EVENT to CLICK_PG,
            KEY_EVENT_ACTION to EVENT_ACTION_CLICK_X_SHOP_RECOMMENDATION,
            KEY_EVENT_CATEGORY to EVENT_CATEGORY_CONTENT_FEED_TIMELINE,
            KEY_EVENT_LABEL to eventLabel,
            KEY_CURRENT_SITE to currentSite,
            KEY_SESSION_IRIS to sessionIris,
            KEY_USER_ID to userSession.userId,
            KEY_BUSINESS_UNIT to CONTENT,
            KEY_TRACKER_ID to "31275"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun convertToPromotion(
        creativeName: String,
        creativeSlot: Int,
        itemId: String,
        itemName: String
    ): HashMap<String, Any> {
        return hashMapOf(
            KEY_CREATIVE_NAME to creativeName,
            KEY_CREATIVE_SLOT to creativeSlot,
            KEY_ITEM_ID to itemId,
            KEY_ITEM_NAME to itemName
        )
    }

    fun sendPendingAnalytics() {
        trackingQueue.sendAll()
    }

    companion object {

        private const val EVENT_NAME_PROMO_CLICK = "promoClick"
        private const val EVENT_NAME_PROMO_VIEW = "promoView"

        private const val EVENT_ACTION_CLICK_FOLLOW_SHOP_RECOMMENDATION =
            "click - follow profile recommendations"
        private const val EVENT_ACTION_CLICK_X_SHOP_RECOMMENDATION =
            "click - x - profile recommendation"
        private const val EVENT_ACTION_CLICK_SHOP_RECOMMENDATION = "click - profile recommendations icon"
        private const val EVENT_ACTION_IMPRESSION_SHOP_RECOMMENDATION =
            "impression - profile recommendations carousel"

        private const val EVENT_CATEGORY_CONTENT_FEED_TIMELINE = "content feed timeline"

        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_SESSION_IRIS = "sessionIris"
        private const val KEY_CREATIVE_NAME = "creative_name"
        private const val KEY_CREATIVE_SLOT = "creative_slot"
        private const val KEY_ITEM_ID = "item_id"
        private const val KEY_ITEM_NAME = "item_name"
        private const val KEY_USER_ID = "userId"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_PROMOTIONS = "promotions"
        private const val KEY_ECOMMERCE = "ecommerce"
        private const val KEY_EVENT = "event"
        const val KEY_EVENT_ACTION = "eventAction"
        const val KEY_EVENT_CATEGORY = "eventCategory"
        const val KEY_EVENT_LABEL = "eventLabel"

        private const val ITEM_NAME_SHOP_RECOMMENDATIONS_CAROUSEL =
            "/feed update tab - shop recommendations carousel"

        private const val CONTENT = "content"
        private const val CLICK_PG = "clickPG"
        private const val TOKOPEDIA_SELLER = "tokopediaseller"
        private const val TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"

        private val sessionIris = TrackApp.getInstance().gtm.irisSessionId
        private val currentSite: String
            get() = if (GlobalConfig.isSellerApp()) {
                TOKOPEDIA_SELLER
            } else {
                TOKOPEDIA_MARKET_PLACE
            }
    }
}
