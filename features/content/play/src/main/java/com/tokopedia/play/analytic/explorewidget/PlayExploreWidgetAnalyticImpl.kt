package com.tokopedia.play.analytic.explorewidget

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.ExploreWidgetType
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelRecommendationConfig
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 03/01/23
 * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3621
 */
class PlayExploreWidgetAnalyticImpl @AssistedInject constructor(
    @Assisted private val channelInfo: PlayChannelInfoUiModel,
    @Assisted private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) : PlayExploreWidgetAnalytic {

    @AssistedFactory
    interface Factory : PlayExploreWidgetAnalytic.Factory {
        override fun create(
            channelInfo: PlayChannelInfoUiModel,
            trackingQueue: TrackingQueue
        ): PlayExploreWidgetAnalyticImpl
    }

    private val channelId: String
        get() = channelInfo.id

    private val channelType: String
        get() = channelInfo.channelType.value.lowercase()

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else "0"

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    private val Boolean.promoToString: String
        get() {
            return if (this) "promo" else "no promo"
        }

    private fun getCategoryLabel(widgetInfo: PlayChannelRecommendationConfig, type: ExploreWidgetType) : Triple<String, String, String> {
        return if (type == ExploreWidgetType.Category) Triple(widgetInfo.categoryWidgetConfig.categoryName, widgetInfo.categoryWidgetConfig.categoryLevel.toString(), widgetInfo.categoryWidgetConfig.categoryId)
        else Triple(widgetInfo.exploreWidgetConfig.categoryName, "0", "0")
    }

    override fun impressExploreIcon(widgetInfo: PlayChannelRecommendationConfig, type: ExploreWidgetType) {
        val label = getCategoryLabel(widgetInfo, type)
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("impression - explore widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - ${label.first} - ${label.second} - ${label.third}")
            .setCustomProperty(Key.trackerId, "39856")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickExploreIcon(widgetInfo: PlayChannelRecommendationConfig, type: ExploreWidgetType) {
        val label = getCategoryLabel(widgetInfo, type)

        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - explore widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - ${label.first} - ${label.second} - ${label.third}")
            .setCustomProperty(Key.trackerId, "39857")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressExploreTab(categoryName: String, chips: Map<ChipWidgetUiModel, Int>) {
        val promotions = chips.map {
            BaseTrackerConst.Promotion(
                id = channelId,
                name = "/play/explorewidget",
                creative = it.key.text,
                position = (it.value + 1).toString()
            )
        }

        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.promoView,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "impression - category tab",
            eventLabel = "$categoryName - $channelId - $channelType",
            promotions = promotions
        )
            .appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.play)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendCustomKeyValue(Key.trackerId, "39858")
            .build()

        trackingQueue.putEETracking(map as? HashMap<String, Any>)
    }

    override fun clickExploreTab(categoryName: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - category tab")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$categoryName - $channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39859")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickContentCard(
        selectedChannel: PlayWidgetChannelUiModel,
        position: Int,
        widgetInfo: PlayChannelRecommendationConfig,
        config: PlayWidgetConfigUiModel,
        type: ExploreWidgetType,
    ) {
        /**
         * {channel_id live room} - {live/vod live room} - {channel_id clicked} - {card_type} - {position} -
         * {is_autoplay} - {category name} - {promo/no promo} - {recommendation_type} - {category level} - {category id}
         */
        val label = getCategoryLabel(widgetInfo, type)
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - channel card")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - ${selectedChannel.channelId} - ${selectedChannel.channelType.value} - ${position + 1} - ${config.autoPlay} - ${label.first} - ${selectedChannel.hasPromo.promoToString} - ${selectedChannel.recommendationType} - ${label.second} - ${label.third}")
            .setCustomProperty(Key.trackerId, "39860")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCloseExplore() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - close explore widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39861")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun scrollExplore(widgetInfo: PlayChannelRecommendationConfig, type: ExploreWidgetType) {
        val label = getCategoryLabel(widgetInfo, type)
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("scroll - explore widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - ${label.first} - ${label.second} - ${label.third}")
            .setCustomProperty(Key.trackerId, "39862")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickRemind(selectedChannelId: String) {
        /**
         * {channel_id live room} - {live/vod live room} - {channel_id remind}
         */
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - remind me explore widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - $selectedChannelId")
            .setCustomProperty(Key.trackerId, "39863")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun swipeRefresh() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("scroll - refresh explore widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39864")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressToasterGlobalError() {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("impression - toaster global error")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39875")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickRetryToaster() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - coba lagi toaster")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39876")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        widgetInfo: PlayChannelRecommendationConfig,
        position: Int,
        type: ExploreWidgetType
    ) {
        val label = getCategoryLabel(widgetInfo, type)
        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.promoView,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "impression - channel card",
            eventLabel = "$channelId - $channelType - ${item.channelType.value.lowercase()} - ${position + 1} - ${config.autoPlay} - ${label.first} - ${item.hasPromo.promoToString} - ${item.recommendationType} - ${label.second} - ${label.third}",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = item.channelId,
                    name = PLAY_EXPLORE_WIDGET_PATH,
                    creative = "${item.channelId} - ${item.channelType.value.lowercase()} - ${position + 1} - ${config.autoPlay} - ${label.first} - ${item.hasPromo.promoToString} - ${item.recommendationType}",
                    position = (position + 1).toString()
                )
            )
        )
            .appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.play)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendCustomKeyValue(Key.trackerId, "40969")
            .build()

        trackingQueue.putEETracking(map as? HashMap<String, Any>)
    }

    companion object {
        private const val PLAY_EXPLORE_WIDGET_PATH = "/play/explorewidget"
    }
}
