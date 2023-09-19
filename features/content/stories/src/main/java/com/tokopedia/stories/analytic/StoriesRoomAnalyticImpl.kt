package com.tokopedia.stories.analytic

import android.os.Bundle
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StoriesRoomAnalyticImpl @AssistedInject constructor(
    @Assisted private val authorId: String,
    private val userSession: UserSessionInterface,
) : StoriesRoomAnalytic {

    @AssistedFactory
    interface Factory : StoriesRoomAnalytic.Factory {
        override fun create(authorId: String): StoriesRoomAnalyticImpl
    }

    private val userId: String
        get() = userSession.userId.orEmpty()

    private val isLogin: Boolean
        get() = userSession.isLoggedIn

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46042
    override fun sendImpressionStoriesContent(storiesId: String) {
        Tracker.Builder()
            .setEvent(Event.openScreen)
            .setCustomProperty(Key.trackerId, "46042")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.isLoggedInStatus, isLogin.toString())
            .setCustomProperty(Key.screenName, "/stories-room/$storiesId/$authorId")
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46043
    override fun sendViewStoryCircleEvent(
        entryPoint: String,
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    ) {
        val itemList = promotions.map {
            Bundle().apply {
                putString(Key.creativeName, it.creativeName)
                putString(Key.creativeSlot, it.creativeSlot)
                putString(Key.itemId, it.itemId)
                putString(Key.itemName, it.itemName)
            }
        }

        val eventDataLayer = Bundle().apply {
            putString(Key.event, Event.viewItem)
            putString(Key.eventAction, "view - story circle")
            putString(Key.eventCategory, STORIES_ROOM_CATEGORIES)
            putString(Key.eventLabel, "$entryPoint - $authorId - $currentCircle")
            putString(Key.trackerId, "46043")
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, currentSite)
            putString(Key.sessionIris, sessionIris)
            putString(Key.userId, userId)
            putParcelableArrayList(Key.promotions, ArrayList(itemList))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.viewItem,
            eventDataLayer,
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46044
    override fun sendClickShopNameEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - shop name")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel(eventLabel)
            .setCustomProperty(Key.trackerId, "46044")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46046
    override fun sendClickThreeDotsEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - three dots")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel(eventLabel)
            .setCustomProperty(Key.trackerId, "46046")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46047
    override fun sendClickShoppingBagEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - shopping bag")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel(eventLabel)
            .setCustomProperty(Key.trackerId, "46047")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46049
    override fun sendClickStoryCircleEvent(
        entryPoint: String,
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    ) {
        val itemList = promotions.map {
            Bundle().apply {
                putString(Key.creativeName, it.creativeName)
                putString(Key.creativeSlot, it.creativeSlot)
                putString(Key.itemId, it.itemId)
                putString(Key.itemName, it.itemName)
            }
        }

        val eventDataLayer = Bundle().apply {
            putString(Key.event, Event.selectContent)
            putString(Key.eventAction, "click - story circle")
            putString(Key.eventCategory, STORIES_ROOM_CATEGORIES)
            putString(Key.eventLabel, "$entryPoint - $authorId - $currentCircle")
            putString(Key.trackerId, "46049")
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, currentSite)
            putString(Key.sessionIris, sessionIris)
            putString(Key.userId, userId)
            putParcelableArrayList(Key.promotions, ArrayList(itemList))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.selectContent,
            eventDataLayer,
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46050
    override fun sendClickRemoveStoryEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - remove story")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel(eventLabel)
            .setCustomProperty(Key.trackerId, "46050")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46051
    override fun sendViewProductCardEvent(
        eventLabel: String,
        items: List<String>,
    ) {
        Tracker.Builder()
            .setEvent("view_item_list")
            .setEventAction("view - product card")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel(eventLabel)
            .setCustomProperty(Key.trackerId, "46051")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty("item_list", "/stories-room - product card")
            .setCustomProperty(Key.items, items)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46052
    override fun sendClickProductCardEvent(
        eventLabel: String,
        itemList: String,
        items: List<String>,
    ) {
        Tracker.Builder()
            .setEvent(Event.selectContent)
            .setEventAction("click - product card")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel(eventLabel)
            .setCustomProperty(Key.trackerId, "46052")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty("item_list", itemList)
            .setCustomProperty(Key.items, items)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46054
    override fun sendClickBuyButtonEvent(
        eventLabel: String,
        items: List<String>,
    ) {
        Tracker.Builder()
            .setEvent(Event.add_to_cart)
            .setEventAction("click - buy button")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel(eventLabel)
            .setCustomProperty(Key.trackerId, "46054")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.items, items)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46056
    override fun sendClickAtcButtonEvent(
        eventLabel: String,
        items: List<String>,
    ) {
        Tracker.Builder()
            .setEvent(Event.add_to_cart)
            .setEventAction("click - atc button")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel(eventLabel)
            .setCustomProperty(Key.trackerId, "46056")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.items, items)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46057
    override fun sendClickTapNextContentEvent(
        entryPoint: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - tap next content")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("$entryPoint - $authorId - $storiesId - $creatorType - $contentType - $currentCircle")
            .setCustomProperty(Key.trackerId, "46057")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46058
    override fun sendClickTapPreviousContentEvent(
        entryPoint: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - tap previous content")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("$entryPoint - $authorId - $storiesId - $creatorType - $contentType - $currentCircle")
            .setCustomProperty(Key.trackerId, "46058")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46059
    override fun sendClickMoveToOtherGroup(entryPoint: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - move to other group")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("$entryPoint - $authorId")
            .setCustomProperty(Key.trackerId, "46059")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46062
    override fun sendClickExitStoryRoomEvent(
        entryPoint: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - exit story room")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("$entryPoint - $authorId - $storiesId - $creatorType - $contentType - $currentCircle")
            .setCustomProperty(Key.trackerId, "46062")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

}
