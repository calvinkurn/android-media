package com.tokopedia.stories.analytics

import android.os.Bundle
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StoriesRoomAnalyticImpl @AssistedInject constructor(
    @Assisted private val args: StoriesArgsModel,
    private val userSession: UserSessionInterface
) : StoriesRoomAnalytic {

    @AssistedFactory
    interface Factory : StoriesRoomAnalytic.Factory {
        override fun create(args: StoriesArgsModel): StoriesRoomAnalyticImpl
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
            .setCustomProperty(Key.screenName, "/stories-room/$storiesId/${args.authorId}")
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46043
    override fun sendViewStoryCircleEvent(
        currentCircle: String,
        promotions: List<StoriesEEModel>
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
            putString(Key.eventLabel, "${args.entryPoint} - ${args.authorId} - $currentCircle")
            putString(Key.trackerId, "46043")
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, currentSite)
            putString(Key.sessionIris, sessionIris)
            putString(Key.userId, userId)
            putParcelableArrayList(Key.promotions, ArrayList(itemList))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.viewItem,
            eventDataLayer
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
        currentCircle: String,
        promotions: List<StoriesEEModel>
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
            putString(Key.eventLabel, "${args.entryPoint} - ${args.authorId} - $currentCircle")
            putString(Key.trackerId, "46049")
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, currentSite)
            putString(Key.sessionIris, sessionIris)
            putString(Key.userId, userId)
            putParcelableArrayList(Key.promotions, ArrayList(itemList))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.selectContent,
            eventDataLayer
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
        items: Map<ContentTaggedProductUiModel, Int>
    ) {
        if (items.isEmpty()) return
        val listOfProducts = items.map {
            Bundle().apply {
                putInt(Key.itemIndex, it.value + 1)
                putString(Key.itemId, it.key.id)
                putString(Key.itemName, it.key.title)
                putFloat(Key.price, it.key.price.price.toFloat())
                putString(Key.itemShopId, args.authorId)
                putString(Key.itemShopType, args.authorType)
                putString(Key.dimension40, "/stories-room - product card")
            }
        }
        val eventDataLayer = Bundle().apply {
            putString(Key.event, Event.viewItemList)
            putString(Key.eventAction, "view - product card")
            putString(Key.eventCategory, STORIES_ROOM_CATEGORIES)
            putString(Key.eventLabel, eventLabel)
            putString(Key.trackerId, "46051")
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, currentSite)
            putString(Key.sessionIris, sessionIris)
            putString(Key.userId, userId)
            putString(Key.itemList, "/stories-room - product card")
            putParcelableArrayList(Key.items, ArrayList(listOfProducts))
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.viewItemList,
            eventDataLayer
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46052
    override fun sendClickProductCardEvent(
        eventLabel: String,
        itemList: String,
        items: List<ContentTaggedProductUiModel>,
        position: Int
    ) {
        val listOfProducts = items.map {
            Bundle().apply {
                putInt(Key.itemIndex, position + 1)
                putString(Key.itemId, it.id)
                putString(Key.itemName, it.title)
                putFloat(
                    Key.price,
                    when (val price = it.price) {
                        is ContentTaggedProductUiModel.DiscountedPrice -> price.price.toFloat()
                        is ContentTaggedProductUiModel.CampaignPrice -> price.price.toFloat()
                        is ContentTaggedProductUiModel.NormalPrice -> price.price.toFloat()
                    }
                )
                putString(Key.itemShopId, args.authorId)
                putString(Key.itemShopType, args.authorType)
                putString(Key.dimension40, itemList)
            }
        }
        val eventDataLayer = Bundle().apply {
            putString(Key.event, Event.selectContent)
            putString(Key.eventAction, "click - product card")
            putString(Key.eventCategory, STORIES_ROOM_CATEGORIES)
            putString(Key.eventLabel, eventLabel)
            putString(Key.trackerId, "46052")
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, currentSite)
            putString(Key.sessionIris, sessionIris)
            putString(Key.userId, userId)
            putString(Key.itemList, itemList)
            putParcelableArrayList(Key.items, ArrayList(listOfProducts))
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.selectContent,
            eventDataLayer
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46054
    override fun sendClickBuyButtonEvent(
        eventLabel: String,
        items: List<ContentTaggedProductUiModel>,
        position: Int,
        shopName: String
    ) {
        val itemList = items.map {
            Bundle().apply {
                putInt(Key.itemIndex, position + 1)
                putString(Key.itemId, it.id)
                putString(Key.itemName, it.title)
                putFloat(
                    Key.price,
                    when (val price = it.price) {
                        is ContentTaggedProductUiModel.DiscountedPrice -> price.price.toFloat()
                        is ContentTaggedProductUiModel.CampaignPrice -> price.price.toFloat()
                        is ContentTaggedProductUiModel.NormalPrice -> price.price.toFloat()
                    }
                )
                putString(Key.itemShopId, args.authorId)
                putString(Key.itemShopType, args.authorType)
                putString(Key.itemShopName, shopName)
            }
        }

        val eventDataLayer = Bundle().apply {
            putString(Key.event, Event.add_to_cart)
            putString(Key.eventAction, "click - buy button")
            putString(Key.eventCategory, STORIES_ROOM_CATEGORIES)
            putString(Key.eventLabel, eventLabel)
            putString(Key.trackerId, "46054")
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, currentSite)
            putString(Key.sessionIris, sessionIris)
            putString(Key.userId, userId)
            putParcelableArrayList(Key.items, ArrayList(itemList))
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.add_to_cart,
            eventDataLayer
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46056
    override fun sendClickAtcButtonEvent(
        eventLabel: String,
        items: List<ContentTaggedProductUiModel>,
        position: Int,
        shopName: String
    ) {
        val itemList = items.map {
            Bundle().apply {
                putInt(Key.itemIndex, position + 1)
                putString(Key.itemId, it.id)
                putString(Key.itemName, it.title)
                putFloat(
                    Key.price,
                    when (val price = it.price) {
                        is ContentTaggedProductUiModel.DiscountedPrice -> price.price.toFloat()
                        is ContentTaggedProductUiModel.CampaignPrice -> price.price.toFloat()
                        is ContentTaggedProductUiModel.NormalPrice -> price.price.toFloat()
                    }
                )
                putString(Key.itemShopId, args.authorId)
                putString(Key.itemShopType, args.authorType)
                putString(Key.itemShopName, shopName)
            }
        }

        val eventDataLayer = Bundle().apply {
            putString(Key.event, Event.add_to_cart)
            putString(Key.eventAction, "click - atc button")
            putString(Key.eventCategory, STORIES_ROOM_CATEGORIES)
            putString(Key.eventLabel, eventLabel)
            putString(Key.trackerId, "46056")
            putString(Key.businessUnit, BusinessUnit.content)
            putString(Key.currentSite, currentSite)
            putString(Key.sessionIris, sessionIris)
            putString(Key.userId, userId)
            putParcelableArrayList(Key.items, ArrayList(itemList))
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.add_to_cart,
            eventDataLayer
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46057
    override fun sendClickTapNextContentEvent(
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - tap next content")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("${args.entryPoint} - ${args.authorId} - $storiesId - $creatorType - $contentType - $currentCircle")
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
        storiesId: String,
        creatorType: String,
        contentType: String,
        currentCircle: String
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - tap previous content")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("${args.entryPoint} - ${args.authorId} - $storiesId - $creatorType - $contentType - $currentCircle")
            .setCustomProperty(Key.trackerId, "46058")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46060
    override fun sendClickMoveToOtherGroup() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - move to other category")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("${args.entryPoint} - ${args.authorId}")
            .setCustomProperty(Key.trackerId, "46060")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 46062
    //{source entry point} - {partnerID} - {story_id} - {asgc/organic} - {image/video} - {current circle}
    override fun sendClickExitStoryRoomEvent(
        storiesId: String,
        contentType: StoriesDetailItem.StoriesItemContentType,
        storyType: String,
        currentCircle: String
    ) {
        val authorId = if (storiesId == "0") "0" else args.authorId

        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - exit story room")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("${args.entryPoint} - $authorId - $storiesId - $storyType - ${contentType.value} - $currentCircle")
            .setCustomProperty(Key.trackerId, "46062")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 47950
    override fun sendViewReportReasonList(
        storiesId: String,
        contentType: StoriesDetailItem.StoriesItemContentType,
        storyType: String
    ) {
        val authorId = if (storiesId == "0") "0" else args.authorId

        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("view - list report reason")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("${args.entryPoint} - $authorId - $storiesId - $storyType - ${contentType.value}")
            .setCustomProperty(Key.trackerId, "47950")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setUserId(userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4155
    // Tracker ID: 47951
    override fun sendClickReportReason(
        storiesId: String,
        contentType: StoriesDetailItem.StoriesItemContentType,
        storyType: String,
        reportReason: String
    ) {
        val authorId = if (storiesId == "0") "0" else args.authorId

        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - report reason")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("${args.entryPoint} - $authorId - $storiesId - $storyType - ${contentType.value} - $reportReason")
            .setCustomProperty(Key.trackerId, "47951")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickPerformance(
        storiesId: String,
        contentType: StoriesDetailItem.StoriesItemContentType,
        storyType: String
    ) {
        val authorId = if (storiesId == "0") "0" else args.authorId
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - perform view bottomsheet")
            .setEventCategory(STORIES_ROOM_CATEGORIES)
            .setEventLabel("${args.entryPoint} - $authorId - $storiesId - $storyType - ${contentType.value}")
            .setCustomProperty(Key.trackerId, "50404")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setUserId(userId)
            .build()
            .send()
    }
}
