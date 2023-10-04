package com.tokopedia.stories.widget.tracking

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
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kenny.hadisaputra on 03/10/23
 */
class DefaultTrackerBuilder(
    private val entryPoint: StoriesEntryPoint,
    private val userSession: UserSessionInterface,
) : StoriesWidgetTracker.Builder {

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

    private val sessionIris get() = TrackApp.getInstance().gtm.irisSessionId

    override fun onImpressedEntryPoint(state: StoriesWidgetState): StoriesWidgetTracker.Data {
        if (!isStoriesValid(state)) return StoriesWidgetTracker.Data.Empty

        val eventAction = "view - story entry point"
        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.promoView,
            eventCategory = eventCategory,
            eventAction = eventAction,
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

        return StoriesWidgetTracker.Data(state.shopId, eventAction, true, map)
    }

    override fun onClickedEntryPoint(state: StoriesWidgetState): StoriesWidgetTracker.Data  {
        if (!isStoriesValid(state)) return StoriesWidgetTracker.Data.Empty

        val eventAction = "click - story entry point"
        val map = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.promoClick,
            eventCategory = eventCategory,
            eventAction = eventAction,
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

        return StoriesWidgetTracker.Data(state.shopId, eventAction, false, map)
    }

    private fun isStoriesValid(state: StoriesWidgetState): Boolean {
        return state.status != StoriesStatus.NoStories && state.shopId.isNotBlank()
    }
}
