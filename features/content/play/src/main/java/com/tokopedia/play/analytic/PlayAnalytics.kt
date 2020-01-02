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
    }

    fun clickLeaveRoom(channelId: String, duration: Long, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_BACK,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun clickShop(channelId: String, shopId: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun clickFollowShop(channelId: String, shopId: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun clickWatchArea(channelId: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun clickPinnedMessage(channelId: String, message: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun clickLike(channelId: String, isLike: Boolean, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun errorState(channelId: String, errorMessage: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun clickQuickReply(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun clickSendChat(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }

    fun clickWatchMode(channelId: String, isLive: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "",
                ""
        )
    }
}
