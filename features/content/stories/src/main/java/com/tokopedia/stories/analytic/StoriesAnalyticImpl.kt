package com.tokopedia.stories.analytic

import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class StoriesAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
): StoriesAnalytic {

    private val userId: String
        get() = userSession.userId.orEmpty()

    private val isLogin: Boolean
        get() = userSession.isLoggedIn

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46042
    override fun sendImpressionStoriesContent(storiesId: String) {
        Tracker.Builder()
            .setEvent("openScreen")
            .setCustomProperty("trackerId", "46042")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty("isLoggedInStatus", isLogin.toString())
            .setCustomProperty("screenName", "/stories-room/$storiesId/$userId")
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46043
    override fun sendViewStoryCircleEvent(
        eventLabel: String,
        promotions: List<String>,
    ) {
        Tracker.Builder()
            .setEvent("view_item")
            .setEventAction("view - story circle")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46043")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty("promotions", promotions)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
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
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46049
    override fun sendClickStoryCircleEvent(
        eventLabel: String,
        promotions: List<String>,
    ) {
        Tracker.Builder()
            .setEvent("select_content")
            .setEventAction("click - story circle")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46049")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty("promotions", promotions)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
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
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty("items", items)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46057
    override fun sendClickTapNextContentEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - tap next content")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46057")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46058
    override fun sendClickTapPreviousContentEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - tap previous content")
            .setEventCategory("stories room")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "46058")
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
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
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

}
