package com.tokopedia.stories.analytic

import android.os.Bundle
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class StoriesAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : StoriesAnalytic {

    private val userId: String
        get() = userSession.userId.orEmpty()

    private val isLogin: Boolean
        get() = userSession.isLoggedIn

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46042
    override fun sendImpressionStoriesContent(storiesId: String, authorId: String) {
        Tracker.Builder()
            .setEvent("openScreen")
            .setCustomProperty("trackerId", "46042")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("isLoggedInStatus", isLogin.toString())
            .setCustomProperty("screenName", "/stories-room/$storiesId/$authorId")
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46043
    override fun sendViewStoryCircleEvent(
        entryPoint: String,
        partnerId: String,
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    ) {
        val itemList = promotions.map {
            Bundle().apply {
                putString(CREATIVE_NAME, it.creativeName)
                putString(CREATIVE_SLOT, it.creativeSlot)
                putString(ITEM_ID, it.itemId)
                putString(ITEM_NAME, it.itemName)
            }
        }

        val eventDataLayer = Bundle().apply {
            putString(EVENT, "view_item")
            putString(EVENT_ACTION, "view - story circle")
            putString(EVENT_CATEGORY, "stories room")
            putString(
                EVENT_LABEL,
                "$entryPoint - $partnerId - $currentCircle"
            )
            putString(TRACKER_ID, "46043")
            putString(BUSINESS_UNIT, "content")
            putString(CURRENT_SITE, currentSite)
            putString(SESSION_IRIS, sessionIris)
            putString(USER_ID, userId)
            putParcelableArrayList(PROMOTIONS, ArrayList(itemList))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            "view_item",
            eventDataLayer,
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46044
    override fun sendClickShopNameEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - shop name")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46044")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46046
    override fun sendClickThreeDotsEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - three dots")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46046")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46047
    override fun sendClickShoppingBagEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - shopping bag")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46047")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46049
    override fun sendClickStoryCircleEvent(
        entryPoint: String,
        partnerId: String,
        currentCircle: String,
        promotions: List<StoriesEEModel>,
    ) {
        val itemList = promotions.map {
            Bundle().apply {
                putString(CREATIVE_NAME, it.creativeName)
                putString(CREATIVE_SLOT, it.creativeSlot)
                putString(ITEM_ID, it.itemId)
                putString(ITEM_NAME, it.itemName)
            }
        }

        val eventDataLayer = Bundle().apply {
            putString(EVENT, "select_content")
            putString(EVENT_ACTION, "click - story circle")
            putString(EVENT_CATEGORY, "stories room")
            putString(
                EVENT_LABEL,
                "$entryPoint - $partnerId - $currentCircle"
            )
            putString(TRACKER_ID, "46049")
            putString(BUSINESS_UNIT, "content")
            putString(CURRENT_SITE, currentSite)
            putString(SESSION_IRIS, sessionIris)
            putString(USER_ID, userId)
            putParcelableArrayList(PROMOTIONS, ArrayList(itemList))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            "select_content",
            eventDataLayer,
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46050
    override fun sendClickRemoveStoryEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - remove story")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46050")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
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
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46051")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("item_list", "/stories-room - product card")
            .setCustomProperty("items", items)
            .setCustomProperty("sessionIris", sessionIris)
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
            .setEvent("select_content")
            .setEventAction("click - product card")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46052")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("item_list", itemList)
            .setCustomProperty("items", items)
            .setCustomProperty("sessionIris", sessionIris)
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
            .setEvent("add_to_cart")
            .setEventAction("click - buy button")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46054")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("items", items)
            .setCustomProperty("sessionIris", sessionIris)
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
            .setEvent("add_to_cart")
            .setEventAction("click - atc button")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46056")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("items", items)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46057
    override fun sendClickTapNextContentEvent(
        entryPoint: String,
        partnerId: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    ) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - tap next content")
            .setEventCategory("stories room")
            .setEventLabel("$entryPoint - $partnerId - $storiesId - $creatorType - $contentType - $currentCircle")
            .setCustomProperty("trackerId", "46057")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46058
    override fun sendClickTapPreviousContentEvent(
        entryPoint: String,
        partnerId: String,
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String,
    ) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - tap previous content")
            .setEventCategory("stories room")
            .setEventLabel("$entryPoint - $partnerId - $storiesId - $creatorType - $contentType - $currentCircle")
            .setCustomProperty("trackerId", "46058")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46059
    override fun sendClickSwipeNextContentEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - swipe next content")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46059")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46060
    override fun sendClickSwipePreviousContentEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - swipe previous content")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46060")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46062
    override fun sendClickExitStoryRoomEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - exit story room")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46062")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

}
