package com.tokopedia.play.analytic

import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 27/12/20
 */
@PlayScope
class PlayPiPAnalytic @Inject constructor(
        private val userSession: UserSessionInterface
) {

    private val userId: String
        get() = userSession.userId.orEmpty()

    /*
    {
        "event": "clickGroupChat",
        "eventCategory": "groupchat room",
        "eventAction": "pip screen active",
        "eventLabel": "{channel_id} - {shop_id} - {live/vod}",
        "businessUnit": "{businessUnit}",
        "currentSite": "{currentSite}",
        "userId": "{User ID}"
    }
     */
    /**
     * Duration PIP is shown to user in Tokopedia App.
     * (User activate PiP Screen)
     */
    fun enterPiP(channelId: String, shopId: Long?, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to "clickGroupChat",
                        KEY_EVENT_CATEGORY to "groupchat room",
                        KEY_EVENT_ACTION to "pip screen active",
                        KEY_EVENT_LABEL to "$channelId - ${shopId ?: 0} - ${channelType.value}",
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId
                )
        )
    }

    /*
    {
        "event": "clickGroupChat",
        "eventCategory": "groupchat room",
        "eventAction": "click button pip",
        "eventLabel": "{channel_id} - {shop_id} - {live/vod}",
        "businessUnit": "{businessUnit}",
        "currentSite": "{currentSite}",
        "userId": "{User ID}"
    }
     */
    /**
     * User click PIP icon from inside live room
     * (User press pip icon)
     */
    fun clickPiPIcon(channelId: String, shopId: Long?, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to "clickGroupChat",
                        KEY_EVENT_CATEGORY to "groupchat room",
                        KEY_EVENT_ACTION to "click button pip",
                        KEY_EVENT_LABEL to "$channelId - ${shopId ?: 0} - ${channelType.value}",
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId
                )
        )
    }

    /*
    {
        "event": "clickGroupChat",
        "eventCategory": "groupchat room",
        "eventAction": "pip screen finished",
        "eventLabel": "{channel_id} - {shop_id} - {live/vod} - {duration}",
        "businessUnit": "{businessUnit}",
        "currentSite": "{currentSite}",
        "userId": "{User ID}"
    }
     */
    /**
     * PIP is shown to user in Tokopedia App.
     * (User close pip)
     */
    fun exitPiP(channelId: String, shopId: Long?, channelType: PlayChannelType, durationInSecond: Long) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to "clickGroupChat",
                        KEY_EVENT_CATEGORY to "groupchat room",
                        KEY_EVENT_ACTION to "pip screen finished",
                        KEY_EVENT_LABEL to "$channelId - ${shopId ?: 0} - ${channelType.value} - $durationInSecond",
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId
                )
        )
    }

    companion object {

        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"

        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_USER_ID = "userId"

        private const val KEY_TRACK_BUSINESS_UNIT = "play"
        private const val KEY_TRACK_CURRENT_SITE = "tokopediamarketplace"
    }
}