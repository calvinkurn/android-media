package com.tokopedia.play.analytic.explorewidget

import com.tokopedia.play.analytic.*
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.ExploreWidgetType
import com.tokopedia.play.view.uimodel.recom.ExploreWidgetConfig
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
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

    private fun getCategoryLabel(widgetInfo: ExploreWidgetConfig, type: ExploreWidgetType) : Triple<String, String, String> {
        return if (type == ExploreWidgetType.Category) Triple(widgetInfo.categoryName, widgetInfo.categoryLevel.toString(), widgetInfo.categoryId)
        else Triple(widgetInfo.categoryName, "0", "0")
    }

    override fun impressExploreIcon(widgetInfo: ExploreWidgetConfig, type: ExploreWidgetType) {
        val label = getCategoryLabel(widgetInfo, type)
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${label.first} - ${label.second} - ${label.third}")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39856")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickExploreIcon(widgetInfo: ExploreWidgetConfig, type: ExploreWidgetType) {
        val label = getCategoryLabel(widgetInfo, type)

        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${label.first} - ${label.second} - ${label.third}")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39857")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
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
            event = KEY_TRACK_PROMO_VIEW,
            eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
            eventAction = "impression - category tab",
            eventLabel = "$categoryName - $channelId - $channelType",
            promotions = promotions
        )
            .appendUserId(userId)
            .appendBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .appendCurrentSite(KEY_TRACK_CURRENT_SITE)
            .appendCustomKeyValue(KEY_TRACK_TRACKER_ID, "39858")
            .build()

        trackingQueue.putEETracking(map as? HashMap<String, Any>)
    }

    override fun clickExploreTab(categoryName: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - category tab")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$categoryName - $channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39859")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickContentCard(
        selectedChannel: PlayWidgetChannelUiModel,
        position: Int,
        widgetInfo: ExploreWidgetConfig,
        config: PlayWidgetConfigUiModel,
        type: ExploreWidgetType,
    ) {
        /**
         * {channel_id live room} - {live/vod live room} - {channel_id clicked} - {card_type} - {position} -
         * {is_autoplay} - {category name} - {promo/no promo} - {recommendation_type} - {category level} - {category id}
         */
        val label = getCategoryLabel(widgetInfo, type)
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - channel card")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${selectedChannel.channelId} - ${selectedChannel.channelType.value} - ${position + 1} - ${config.autoPlay} - ${widgetInfo.categoryName} - ${selectedChannel.hasPromo.promoToString} - ${selectedChannel.recommendationType} - ${label.second} - ${label.third}")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39860")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCloseExplore() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - close explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39861")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun scrollExplore(widgetInfo: ExploreWidgetConfig, type: ExploreWidgetType) {
        val label = getCategoryLabel(widgetInfo, type)
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("scroll - explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${label.first} - ${label.second} - ${label.third}")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39862")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickRemind(selectedChannelId: String) {
        /**
         * {channel_id live room} - {live/vod live room} - {channel_id remind}
         */
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - remind me explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - $selectedChannelId")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39863")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun swipeRefresh() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("scroll - refresh explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39864")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressToasterGlobalError() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - toaster global error")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39875")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickRetryToaster() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - coba lagi toaster")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39876")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        widgetInfo: ExploreWidgetConfig,
        position: Int,
        type: ExploreWidgetType
    ) {
        val label = getCategoryLabel(widgetInfo, type)
        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = KEY_TRACK_PROMO_VIEW,
            eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
            eventAction = "impression - channel card",
            eventLabel = "$channelId - $channelType - ${item.channelType.value.lowercase()} - ${position + 1} - ${config.autoPlay} - ${widgetInfo.categoryName} - ${item.hasPromo.promoToString} - ${item.recommendationType} - ${label.second} - ${label.third}",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = item.channelId,
                    name = PLAY_EXPLORE_WIDGET_PATH,
                    creative = "${item.channelId} - ${item.channelType.value.lowercase()} - ${position + 1} - ${config.autoPlay} - ${widgetInfo.categoryName} - ${item.hasPromo.promoToString} - ${item.recommendationType}",
                    position = (position + 1).toString()
                )
            )
        )
            .appendUserId(userId)
            .appendBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .appendCurrentSite(KEY_TRACK_CURRENT_SITE)
            .appendCustomKeyValue(KEY_TRACK_TRACKER_ID, "40969")
            .build()

        trackingQueue.putEETracking(map as? HashMap<String, Any>)
    }

    companion object {
        private const val PLAY_EXPLORE_WIDGET_PATH = "/play/explorewidget"
    }
}
