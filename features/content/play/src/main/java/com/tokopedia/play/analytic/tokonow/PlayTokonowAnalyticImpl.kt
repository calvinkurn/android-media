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
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_USER_ID to userId,
            )
        )
    }

    /**
     * Button Ganti Alamat
     */
    override fun impressChooseAddress(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - ganti alamat",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun clickChooseAddress(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - ganti alamat",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    /**
     * Click cek jangkauan
     */
    override fun clickInfoAddressWidget(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - now cek jangkauan",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_USER_ID to userId,
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
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun clickInfoNow(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - now info bottomsheet",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun impressNowToaster(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - now toaster",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun clickLihatNowToaster(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - lihat now toaster",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun clickGlobalToaster(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - lihat keranjang",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun impressGlobalToaster(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - lihat keranjang",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }
}