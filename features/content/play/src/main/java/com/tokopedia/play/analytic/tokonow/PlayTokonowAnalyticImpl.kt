package com.tokopedia.play.analytic.tokonow

import com.tokopedia.play.analytic.*
import com.tokopedia.play.analytic.KEY_TRACK_GROUP_CHAT_ROOM
import com.tokopedia.play.analytic.KEY_TRACK_VIEW_CONTENT_IRIS
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 17/06/22
 */
class PlayTokonowAnalyticImpl @Inject constructor(private val userSession: UserSessionInterface): PlayTokonowAnalytic {
    private val userId: String
        get() = userSession.userId

    override fun impressAddressWidget(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - out of now coverage",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_TRACKER_ID to "32235"
            )
        )
    }

    override fun impressChooseAddress(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - ganti alamat",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_TRACKER_ID to "32237"
            )
        )
    }

    override fun clickChooseAddress(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - ganti alamat",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_TRACKER_ID to "32239"
            )
        )
    }

    override fun clickInfoAddressWidget(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - now info selengkapnya",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_TRACKER_ID to "32238"
            )
        )
    }

    override fun impressInfoNow(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - now info bottomsheet",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_TRACKER_ID to "32252"
            )
        )
    }

    override fun clickInfoNow(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - now info bottomsheet\"",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_TRACKER_ID to "32253"
            )
        )
    }
}