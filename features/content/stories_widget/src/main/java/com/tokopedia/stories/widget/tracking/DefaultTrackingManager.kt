package com.tokopedia.stories.widget.tracking

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kenny.hadisaputra on 23/08/23
 */
class DefaultTrackingManager(
    private val entryPoint: StoriesEntryPoint,
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) : TrackingManager {

    private val eventCategory: String = when (entryPoint) {
        StoriesEntryPoint.ShopPage -> "shop page - buyer"
        StoriesEntryPoint.ProductDetail -> "product detail page"
        StoriesEntryPoint.TopChatList -> "inbox-chat"
        StoriesEntryPoint.TopChatRoom -> "message room"
    }

    override fun impressEntryPoints(key: StoriesEntryPoint) {
        //TODO() not yet implemented
        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.promoView,
            eventCategory = eventCategory,
            eventAction = "view - story entry point",
            eventLabel = "story",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = "",
                    name = "",
                    creative = "",
                    position = "",
                )
            )
        )
            .appendUserId(userSession.userId)
            .appendBusinessUnit(BusinessUnit.content)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace) //TODO() if available seller app
            .build()

        if (map is HashMap<String, Any>) trackingQueue.putEETracking(map)
    }

    override fun clickEntryPoints(key: StoriesEntryPoint) {
        val map = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoClick,
            eventCategory = eventCategory,
            eventAction = "click - story entry point",
            eventLabel = "${key.sourceName} - ${key.key} - story - shop", //TODO() shopId
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = "",
                    name = "",
                    creative = "",
                    position = "",
                )
            )
        )
            .appendUserId(userSession.userId)
            .appendBusinessUnit(BusinessUnit.content)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace) //TODO() if available seller app
            .build()
        if (map is HashMap<String, Any>) trackingQueue.putEETracking(map)
    }
}
