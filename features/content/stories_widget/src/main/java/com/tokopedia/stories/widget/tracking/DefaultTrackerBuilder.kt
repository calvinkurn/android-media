package com.tokopedia.stories.widget.tracking

import android.os.Bundle
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.stories.widget.StoriesStatus
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by kenny.hadisaputra on 03/10/23
 */
class DefaultTrackerBuilder(
    private val entryPoint: StoriesEntryPoint,
    private val userSession: UserSessionInterface
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

    override fun onImpressedEntryPoint(state: StoriesWidgetState): StoriesWidgetTracker.Data {
        if (!isStoriesValid(state)) return StoriesWidgetTracker.Data.Empty

        val eventName = Event.viewItem
        val eventAction = "view - story entry point"

        val trackerBundle = Bundle().apply {
            putString(Key.event, eventName)
            putString(Key.eventCategory, eventCategory)
            putString(Key.eventAction, eventAction)
            putString(Key.eventLabel, "${entryPoint.trackerName} - ${state.shopId} - story - shop")
            putParcelableArrayList(
                Key.promotions,
                arrayListOf(
                    Bundle().apply {
                        putString(Key.itemId, state.shopId)
                        putString(Key.itemName, "/ - ${state.shopId} - stories entry point")
                        putString(Key.creativeSlot, "")
                        putString(Key.creativeName, "")
                    }
                )
            )
            putString(Key.userId, userSession.userId)
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, CurrentSite.tokopediaMarketplace)
            putString(Key.trackerId, impressionTrackerId)
        }

        return StoriesWidgetTracker.Data(
            state.shopId,
            eventName,
            eventAction,
            true,
            trackerBundle
        )
    }

    override fun onClickedEntryPoint(state: StoriesWidgetState): StoriesWidgetTracker.Data {
        if (!isStoriesValid(state)) return StoriesWidgetTracker.Data.Empty

        val eventName = Event.selectContent
        val eventAction = "click - story entry point"

        val trackerBundle = Bundle().apply {
            putString(Key.event, eventName)
            putString(Key.eventCategory, eventCategory)
            putString(Key.eventAction, eventAction)
            putString(Key.eventLabel, "${entryPoint.trackerName} - ${state.shopId} - story - shop")
            putParcelableArrayList(
                Key.promotions,
                arrayListOf(
                    Bundle().apply {
                        putString(Key.itemId, state.shopId)
                        putString(Key.itemName, "/ - ${state.shopId} - stories entry point")
                        putString(Key.creativeSlot, "")
                        putString(Key.creativeName, "")
                    }
                )
            )
            putString(Key.userId, userSession.userId)
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, CurrentSite.tokopediaMarketplace)
            putString(Key.trackerId, clickTrackerId)
        }

        return StoriesWidgetTracker.Data(
            state.shopId,
            eventName,
            eventAction,
            false,
            trackerBundle
        )
    }

    private fun isStoriesValid(state: StoriesWidgetState): Boolean {
        return state.status != StoriesStatus.NoStories && state.shopId.isNotBlank()
    }
}
