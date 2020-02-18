package com.tokopedia.play.analytic

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp


/**
 * Created by mzennis on 2020-01-02.
 */

object PlayAnalytics {

    private const val KEY_TRACK_CLICK_BACK = "clickBack"
    private const val KEY_TRACK_CLICK_GROUP_CHAT = "clickGroupChat"
    private const val KEY_TRACK_VIEW_GROUP_CHAT = "viewGroupChat"

    private const val KEY_TRACK_CLICK = "click"
    private const val KEY_TRACK_GROUP_CHAT_ROOM = "groupchat room"

    fun sendScreen(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated("/group-chat-room/$channelId/${channelType.value}")
    }

    fun clickLeaveRoom(channelId: String, duration: Long, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_BACK,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "leave room",
                "$channelId - $duration - ${channelType.value}"
        )
    }

    fun clickShop(channelId: String, shopId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - shop",
                "$shopId - $channelId - ${channelType.value}"
        )
    }

    fun clickFollowShop(channelId: String, shopId: String, action: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK $action shop",
                "$channelId - $shopId - ${channelType.value}"
        )
    }

    fun clickWatchArea(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK watch area",
                "$channelId - ${channelType.value}"
        )
    }

    fun clickPinnedMessage(channelId: String, message: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on admin pinned message",
                "$channelId - $message - ${channelType.value}"
        )
    }

    fun clickLike(channelId: String, isLike: Boolean, channelType: PlayChannelType) {
        val action = if(isLike) "like" else "unlike"
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK $action",
                "$channelId - ${channelType.value}"
        )
    }

    fun errorState(channelId: String, errorMessage: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "error state",
                "$channelId - $errorMessage - ${channelType.value}"
        )
    }

    fun clickQuickReply(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on quick reply component",
                channelId
        )
    }

    fun clickSendChat(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on button send",
                channelId
        )
    }

    fun clickWatchMode(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK watch mode option",
                "$channelId - ${channelType.value}"
        )
    }

    fun clickPlayVideo(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on play button video",
                "$channelId - ${channelType.value}"
        )
    }
}
