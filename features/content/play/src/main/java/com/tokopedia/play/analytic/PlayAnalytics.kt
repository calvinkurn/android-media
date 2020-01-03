package com.tokopedia.play.analytic

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

    private const val KEY_TRACK_LIVE = "live"
    private const val KEY_TRACK_VOD = "vod"

    private fun getLiveOrVod(isLive: Boolean): String {
        return if(isLive) KEY_TRACK_LIVE else KEY_TRACK_VOD
    }

    fun sendScreen(channelId: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated("/group-chat-room/$channelId")
    }

    fun clickLeaveRoom(channelId: String, duration: Long, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_BACK,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "leave room",
                "$channelId - $duration - ${getLiveOrVod(isLive)}"
        )
    }

    fun clickShop(channelId: String, shopId: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - shop",
                "$shopId - $channelId - ${getLiveOrVod(isLive)}"
        )
    }

    fun clickFollowShop(channelId: String, shopId: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK follow shop",
                "$channelId - $shopId - ${getLiveOrVod(isLive)}"
        )
    }

    fun clickWatchArea(channelId: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK watch area",
                "$channelId - ${getLiveOrVod(isLive)}"
        )
    }

    fun clickPinnedMessage(channelId: String, message: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on admin pinned message",
                "$channelId - $message - ${getLiveOrVod(isLive)}"
        )
    }

    fun clickLike(channelId: String, isLike: Boolean, isLive: Boolean) {
        val action = if(isLike) "like" else "unlike"
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK $action",
                "$channelId - ${getLiveOrVod(isLive)}"
        )
    }

    fun errorState(channelId: String, errorMessage: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "error state",
                "$channelId - $errorMessage - ${getLiveOrVod(isLive)}"
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

    fun clickWatchMode(channelId: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK watch mode option",
                "$channelId - ${getLiveOrVod(isLive)}"
        )
    }
}
