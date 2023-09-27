package com.tokopedia.stories.widget.tracking

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.stories.widget.domain.StoriesEntrySource
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kenny.hadisaputra on 23/08/23
 */
class DefaultTrackingManager(
    entryPoint: StoriesEntrySource,
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) : TrackingManager {

    private val eventCategory: String = when (entryPoint) {
        is StoriesEntrySource.ShopPage -> "shop page - buyer"
        is StoriesEntrySource.ProductDetail -> "product detail page"
        is StoriesEntrySource.TopChatList -> "inbox-chat"
        is StoriesEntrySource.TopChatRoom -> "message room"
    }

    override fun impressEntryPoints(key: StoriesEntrySource) {
        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.promoView,
            eventCategory = eventCategory,
            eventAction = "view - story entry point",
            eventLabel = "${key.sourceName} - ${key.id} - story - shop",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = key.id,
                    name = "/ - ${key.id} - stories entry point",
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

    override fun clickEntryPoints(key: StoriesEntrySource) {
        val map = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoClick,
            eventCategory = eventCategory,
            eventAction = "click - story entry point",
            eventLabel = "${key.sourceName} - ${key.id} - story - shop",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = key.id,
                    name = "/ - ${key.id} - stories entry point",
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
