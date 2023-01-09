package com.tokopedia.play.analytic.explorewidget

import android.os.Bundle
import com.tokopedia.play.analytic.*
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker
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
    private val userSession: UserSessionInterface,
) : PlayExploreWidgetAnalytic {

    @AssistedFactory
    interface Factory : PlayExploreWidgetAnalytic.Factory {
        override fun create(
            channelInfo: PlayChannelInfoUiModel,
        ): PlayExploreWidgetAnalyticImpl
    }

    private val channelId: String
        get() = channelInfo.id

    private val channelType: String
        get() = channelInfo.channelType.value

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else "0"

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    override fun impressExploreIcon() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39856")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickExploreIcon() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39857")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressExploreTab(categoryName: String, channels: List<ChipWidgetUiModel>, position: Int) {
        val items = arrayListOf<Bundle>().apply {
            channels.forEach {
                add(itemToBundle(it, position))
            }
        }

        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, "promoView")
            putString(KEY_EVENT_CATEGORY, KEY_TRACK_GROUP_CHAT_ROOM)
            putString(KEY_EVENT_ACTION, "impression - category tab")
            putString(KEY_EVENT_LABEL, "$categoryName - $channelId $channelType")
            putString(KEY_CURRENT_SITE, KEY_TRACK_CURRENT_SITE)
            putString(KEY_SESSION_IRIS, sessionIris)
            putString(KEY_USER_ID, userId)
            putString(KEY_BUSINESS_UNIT, KEY_TRACK_BUSINESS_UNIT)
            putParcelableArrayList("items", items)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            "promotions", dataLayer
        )
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
        categoryName: String,
        isAutoplay: Boolean
    ) {
        /**
         * {channel_id live room} - {live/vod live room} - {channel_id clicked} - {card_type} - {position} -
         * {is_autoplay} - {category name} - {promo/no promo} - {recommendation_type}
         */
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - channel card")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${selectedChannel.channelId} - ${selectedChannel.channelType.value} - $position - $isAutoplay - $categoryName - ${selectedChannel.hasPromo} - ${selectedChannel.recommendationType}")
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

    override fun scrollExplore() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("scroll - explore widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
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

    private fun itemToBundle(
        chip: ChipWidgetUiModel,
        position: Int,
    ): Bundle =
        Bundle().apply {
            putString("creative_name", chip.text)
            putInt("creative_slot", position)
            putString("item_id", chip.sourceId)
            putString("item_name", chip.text)
        }
}
