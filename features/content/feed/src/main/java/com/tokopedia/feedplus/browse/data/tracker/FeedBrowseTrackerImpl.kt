package com.tokopedia.feedplus.browse.data.tracker

import android.os.Bundle
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.play.widget.analytic.const.toTrackingType
import com.tokopedia.play.widget.analytic.const.trackerMultiFields
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by meyta.taliti on 01/09/23.
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4134
 */
private typealias EnhanceEcommerce = Pair<String, Bundle>
internal class FeedBrowseTrackerImpl @AssistedInject constructor(
    @Assisted val prefix: String,
    private val impressionManager: FeedBrowseImpressionManager,
    private val userSession: UserSessionInterface
) : FeedBrowseTracker {

    @AssistedFactory
    interface Factory {
        fun create(prefix: String): FeedBrowseTrackerImpl
    }

    override fun viewChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        slotInfo: SlotInfo,
        channelPositionInList: Int
    ) = impressionManager.impress(slotInfo.id, item) {
        val widgetPosition = slotInfo.position + 1
        val channelPosition = channelPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.viewItem,
            eventAction = "view - channel card",
            eventLabel = trackerMultiFields(
                prefix,
                /** prefix **/
                item.channelType.toTrackingType(),
                /** videoType **/
                item.partner.id,
                /** partnerID **/
                item.channelId,
                /** channelID **/
                channelPositionInList,
                /** position **/
                0,
                /** businessPosition **/
                "null",
                /** isAutoPlay **/
                config.maxAutoPlayCellularDuration,
                /** duration **/
                item.recommendationType,
                /** recommendationType **/
            ),
            trackerId = "45741",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, channelPosition.toString())
                putString(Key.itemId, "${item.channelId} - ${slotInfo.title}")
                putString(Key.itemName, "/ - $prefix - $widgetPosition - channel card - ${slotInfo.title}")
            }
        )
    }

    override fun viewChipsWidget(
        item: WidgetMenuModel,
        slotInfo: SlotInfo,
        chipPositionInList: Int
    ) = impressionManager.impress(slotInfo.id, item) {
        val widgetPosition = slotInfo.position + 1
        val channelPosition = chipPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.viewItem,
            eventAction = "view - chips recom widget",
            eventLabel = trackerMultiFields(
                prefix,
                item.label,
                slotInfo.title
            ),
            trackerId = "45742",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, channelPosition.toString())
                putString(Key.itemId, "${item.id} - ${slotInfo.title}")
                putString(Key.itemName, item.label)
            }
        )
    }

    override fun clickChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        slotInfo: SlotInfo,
        channelPositionInList: Int
    ) {
        val widgetPosition = slotInfo.position + 1
        val channelPosition = channelPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.selectContent,
            eventAction = "click - channel card",
            eventLabel = trackerMultiFields(
                prefix,
                /** prefix **/
                item.channelType.toTrackingType(),
                /** videoType **/
                item.partner.id,
                /** partnerID **/
                item.channelId,
                /** channelID **/
                channelPositionInList,
                /** position **/
                config.businessWidgetPosition,
                /** businessPosition **/
                "null",
                /** isAutoPlay **/
                config.maxAutoPlayCellularDuration,
                /** duration **/
                item.recommendationType,
                /** recommendationType **/
            ),
            trackerId = "45743",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, channelPosition.toString())
                putString(Key.itemId, "${item.channelId} - ${slotInfo.title}")
                putString(Key.itemName, "/ - $prefix - $widgetPosition - channel card - ${slotInfo.title}")
            }
        )
    }

    override fun clickChipsWidget(
        item: WidgetMenuModel,
        slotInfo: SlotInfo,
        chipPositionInList: Int
    ) {
        val widgetPosition = slotInfo.position + 1
        val channelPosition = chipPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.selectContent,
            eventAction = "click - chips recom widget",
            eventLabel = trackerMultiFields(
                prefix,
                item.label,
                slotInfo.title
            ),
            trackerId = "45744",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, channelPosition.toString())
                putString(Key.itemId, "${item.id} - ${slotInfo.title}")
                putString(Key.itemName, item.label)
            }
        )
    }

    override fun clickBackExitBrowsePage() {
        Tracker.Builder()
            .setEvent(Event.clickHomepage)
            .setEventAction("click - back exit browse")
            .setEventCategory("feed browse page")
            .setEventLabel(prefix)
            .setCustomProperty(Key.trackerId, "45745")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    override fun viewInspirationBanner(
        item: BannerWidgetModel,
        slotInfo: SlotInfo,
        bannerPositionInList: Int
    ) = impressionManager.impress(slotInfo.id, item) {
        val widgetPosition = slotInfo.position + 1
        val bannerPosition = bannerPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.viewItem,
            eventAction = "view - category inspiration",
            eventLabel = trackerMultiFields(
                prefix,
                item.title,
                slotInfo.title,
                bannerPosition
            ),
            trackerId = "45748",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, bannerPosition.toString())
                putString(Key.itemId, "${item.title} - ${slotInfo.title}")
                putString(Key.itemName, "/ - $prefix - $widgetPosition - category inspiration - ${slotInfo.title}")
            }
        )
    }

    override fun clickInspirationBanner(
        item: BannerWidgetModel,
        slotInfo: SlotInfo,
        bannerPositionInList: Int
    ) {
        val widgetPosition = slotInfo.position + 1
        val bannerPosition = bannerPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.selectContent,
            eventAction = "click - category inspiration",
            eventLabel = trackerMultiFields(
                prefix,
                item.title,
                slotInfo.title,
                bannerPosition
            ),
            trackerId = "45749",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, bannerPosition.toString())
                putString(Key.itemId, "${item.title} - ${slotInfo.title}")
                putString(Key.itemName, "/ - $prefix - $widgetPosition - category inspiration - ${slotInfo.title}")
            }
        )
    }

    override fun viewAuthorWidget(
        item: AuthorWidgetModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    ) = impressionManager.impress(slotInfo.id, item) {
        val widgetPosition = slotInfo.position + 1
        val channelPosition = widgetPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.viewItem,
            eventAction = "view - channel card",
            eventLabel = trackerMultiFields(
                prefix,
                /** prefix **/
                item.channelType,
                /** videoType **/
                item.id,
                /** partnerID **/
                item.contentId,
                /** channelID **/
                channelPosition,
                /** position **/
                0,
                /** businessPosition **/
                "null",
                /** isAutoPlay **/
                0,
                /** duration **/
                "null",
                /** recommendationType **/
            ),
            trackerId = "45741",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, channelPosition.toString())
                putString(Key.itemId, "${item.contentId} - ${slotInfo.title}")
                putString(Key.itemName, "/ - $prefix - $widgetPosition - channel card - ${slotInfo.title}")
            }
        )
    }

    override fun clickAuthorChannelCard(
        item: AuthorWidgetModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    ) {
        val widgetPosition = slotInfo.position + 1
        val channelPosition = widgetPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.selectContent,
            eventAction = "click - channel card",
            eventLabel = trackerMultiFields(
                prefix,
                /** prefix **/
                item.channelType,
                /** videoType **/
                item.id,
                /** partnerID **/
                item.contentId,
                /** channelID **/
                channelPosition,
                /** position **/
                0,
                /** businessPosition **/
                "null",
                /** isAutoPlay **/
                0,
                /** duration **/
                "null",
                /** recommendationType **/
            ),
            trackerId = "45743",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, channelPosition.toString())
                putString(Key.itemId, "${item.contentId} - ${slotInfo.title}")
                putString(Key.itemName, "/ - $prefix - $widgetPosition - channel card - ${slotInfo.title}")
            }
        )
    }

    override fun clickAuthorName(
        item: AuthorWidgetModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    ) {
        val widgetVerticalPosition = slotInfo.position + 1
        val widgetHorizontalPosition = widgetPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.selectContent,
            eventAction = "click - creator name",
            eventLabel = trackerMultiFields(
                prefix,
                item.id,
                widgetHorizontalPosition,
                slotInfo.title
            ),
            trackerId = "45754",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetVerticalPosition.toString())
                putString(Key.creativeSlot, widgetHorizontalPosition.toString())
                putString(Key.itemId, "${item.contentId} - ${slotInfo.title}")
                putString(Key.itemName, "/ - $prefix - $widgetVerticalPosition - creator name - ${slotInfo.title}")
            }
        )
    }

    override fun clickBackExit() {
        Tracker.Builder()
            .setEvent(Event.clickHomepage)
            .setEventAction("click - back exit page")
            .setEventCategory("feed browse page")
            .setEventLabel(prefix)
            .setCustomProperty(Key.trackerId, "45755")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    override fun openScreenBrowseFeedPage() {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            "/feed browse page",
            mapOf(
                Key.trackerId to "49229",
                Key.businessUnit to BusinessUnit.content,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.isLoggedInStatus to userSession.isLoggedIn.toString(),
                Key.userId to userSession.userId
            )
        )
    }

    override fun viewStoryWidget(
        item: StoryNodeModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    ) = impressionManager.impress(slotInfo.id, item) {
        val widgetPosition = slotInfo.position + 1
        val storiesPosition = widgetPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.viewItem,
            eventAction = "view - stories entry point",
            eventLabel = trackerMultiFields(
                prefix,
                item.id,
                storiesPosition
            ),
            trackerId = "45746",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, storiesPosition.toString())
                putString(Key.itemId, "${item.id} - ${item.authorType.trackerValue}")
                putString(Key.itemName, "/ - $prefix - $widgetPosition - stories")
            }
        )
    }

    override fun clickStoryWidget(
        item: StoryNodeModel,
        slotInfo: SlotInfo,
        widgetPositionInList: Int
    ) {
        val widgetPosition = slotInfo.position + 1
        val storiesPosition = widgetPositionInList + 1

        sendEnhanceEcommerceEvent(
            eventName = Event.selectContent,
            eventAction = "click - stories entry point",
            eventLabel = trackerMultiFields(
                prefix,
                item.id,
                storiesPosition
            ),
            trackerId = "45747",
            enhanceEcommerce = Key.promotions to Bundle().apply {
                putString(Key.creativeName, widgetPosition.toString())
                putString(Key.creativeSlot, storiesPosition.toString())
                putString(Key.itemId, "${item.id} - ${item.authorType.trackerValue}")
                putString(Key.itemName, "/ - $prefix - $widgetPosition - stories")
            }
        )
    }

    override fun clickSearchbar() {
        Tracker.Builder()
            .setEvent(Event.clickHomepage)
            .setEventAction("click - search bar")
            .setEventCategory("feed browse page")
            .setEventLabel(prefix)
            .setCustomProperty(Key.trackerId, "50385")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    private fun sendEnhanceEcommerceEvent(
        eventName: String,
        eventAction: String,
        eventLabel: String,
        trackerId: String,
        enhanceEcommerce: EnhanceEcommerce
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            eventName,
            Bundle().apply {
                putString(Key.event, eventName)
                putString(Key.eventAction, eventAction)
                putString(Key.eventCategory, EventCategory.browseFeed)
                putString(Key.eventLabel, eventLabel)
                putString(Key.trackerId, trackerId)
                putString(Key.businessUnit, BusinessUnit.content)
                putString(Key.currentSite, CurrentSite.tokopediaMarketplace)
                putParcelableArrayList(
                    enhanceEcommerce.first,
                    arrayListOf(
                        enhanceEcommerce.second
                    )
                )
                putString(Key.userId, userSession.userId)
            }
        )
    }

    private val AuthorType.trackerValue get() = when (this) {
        AuthorType.User -> "user"
        AuthorType.Shop -> "shop"
        else -> ""
    }

    companion object {
        const val PREFIX_BROWSE_PAGE = "BROWSE_PAGE_FEED"
        const val PREFIX_CATEGORY_INSPIRATION_PAGE = "CAT_INSPIRATION_PAGE"
        const val PREFIX_LOCAL_SEARCH_PAGE = "BROWSE_PAGE_SEARCH"
    }
}
