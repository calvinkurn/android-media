package com.tokopedia.stories.widget.tracking

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.stories.widget.StoriesStatus
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kenny.hadisaputra on 23/08/23
 */
class DefaultTrackingManager(
    private val entryPoint: StoriesEntryPoint,
    lifecycleOwner: LifecycleOwner,
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface,
) : TrackingManager {

    private val eventCategory: String = when (entryPoint) {
        StoriesEntryPoint.ShopPage, StoriesEntryPoint.ShopPageReimagined -> "shop page - buyer"
        StoriesEntryPoint.ProductDetail -> "product detail page"
        StoriesEntryPoint.TopChatList -> "inbox-chat"
        StoriesEntryPoint.TopChatRoom -> "message room"
    }

    private val impressionTrackerId = when (entryPoint) {
        StoriesEntryPoint.ShopPage, StoriesEntryPoint.ShopPageReimagined -> "45997"
        StoriesEntryPoint.ProductDetail -> "45999"
        StoriesEntryPoint.TopChatList -> "46001"
        StoriesEntryPoint.TopChatRoom -> "46003"
    }

    private val clickTrackerId = when (entryPoint) {
        StoriesEntryPoint.ShopPage, StoriesEntryPoint.ShopPageReimagined -> "45998"
        StoriesEntryPoint.ProductDetail -> "46000"
        StoriesEntryPoint.TopChatList -> "46002"
        StoriesEntryPoint.TopChatRoom -> "46004"
    }

    private val impressedShopIds = mutableSetOf<String>()

    private val sessionIris get() = TrackApp.getInstance().gtm.irisSessionId

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> sendAll()
                    else -> {}
                }
            }
        })
    }

    override fun impressEntryPoints(state: StoriesWidgetState) = requireHasStories(state) {
        if (impressedShopIds.contains(state.shopId)) return@requireHasStories
        impressedShopIds.add(state.shopId)

        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.promoView,
            eventCategory = eventCategory,
            eventAction = "view - story entry point",
            eventLabel = "${entryPoint.trackerName} - ${state.shopId} - story - shop",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = state.shopId,
                    name = "/ - ${state.shopId} - stories entry point",
                    creative = "",
                    position = "",
                )
            )
        ).appendUserId(userSession.userId)
            .appendBusinessUnit(BusinessUnit.content)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendCustomKeyValue(Key.sessionIris, sessionIris)
            .appendCustomKeyValue(Key.trackerId, impressionTrackerId)
            .build()

        if (map is HashMap<String, Any>) trackingQueue.putEETracking(map)
    }

    override fun clickEntryPoints(state: StoriesWidgetState) = requireHasStories(state) {
        val map = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoClick,
            eventCategory = eventCategory,
            eventAction = "click - story entry point",
            eventLabel = "${entryPoint.trackerName} - ${state.shopId} - story - shop",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = state.shopId,
                    name = "/ - ${state.shopId} - stories entry point",
                    creative = "",
                    position = "",
                )
            )
        ).appendUserId(userSession.userId)
            .appendBusinessUnit(BusinessUnit.content)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendCustomKeyValue(Key.sessionIris, sessionIris)
            .appendCustomKeyValue(Key.trackerId, clickTrackerId)
            .build()

        if (map is HashMap<String, Any>) trackingQueue.putEETracking(map)
    }

    fun sendAll() {
        trackingQueue.sendAll()
    }

    fun resetImpression() {
        impressedShopIds.clear()
    }

    private fun requireHasStories(state: StoriesWidgetState, onHasStories: () -> Unit) {
        if (state.status == StoriesStatus.NoStories) return
        if (state.shopId.isBlank()) return
        onHasStories()
    }
}
