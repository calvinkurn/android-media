package com.tokopedia.creation.common.upload.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.Value
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 13, 2022
 */

/**
 * Mynakama Tracker : https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
 */
class PlayShortsUploadAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayShortsUploadAnalytic {

    /**
     * Row 66
     */
    override fun clickRedirectToChannelRoom(
        authorId: String,
        authorType: String,
        channelId: String
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    Key.event to Event.clickContent,
                    Key.eventAction to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "lihat video uploaded"
                    ),
                    Key.eventCategory to EventCategory.playBroadcastShort,
                    Key.eventLabel to "$authorId - ${getAnalyticAuthorType(authorType)} - $channelId",
                    Key.businessUnit to BusinessUnit.play,
                    Key.currentSite to currentSite,
                    Key.trackerId to getTrackerIdBySite("37589", "37671"),
                    Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                    Key.userId to userSession.userId
                )
            )
    }

    /**
     * Row 68
     */
    override fun clickRetryUpload(authorId: String, authorType: String, channelId: String) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    Key.event to Event.clickContent,
                    Key.eventAction to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "coba lagi coachmark"
                    ),
                    Key.eventCategory to EventCategory.playBroadcastShort,
                    Key.eventLabel to "$authorId - ${getAnalyticAuthorType(authorType)}",
                    Key.businessUnit to BusinessUnit.play,
                    Key.currentSite to currentSite,
                    Key.trackerId to getTrackerIdBySite("37591", "37673"),
                    Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                    Key.userId to userSession.userId
                )
            )
    }

    private fun getAnalyticAuthorType(authorType: String): String {
        return when(authorType) {
            CONTENT_TYPE_USER -> Value.user
            CONTENT_TYPE_SHOP -> Value.seller
            else -> ""
        }
    }

    private val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) CurrentSite.tokopediaSeller
        else CurrentSite.tokopediaMarketplace

    private fun getTrackerIdBySite(mainAppTrackerId: String, sellerAppTrackerId: String): String {
        return if (GlobalConfig.isSellerApp()) sellerAppTrackerId
        else mainAppTrackerId
    }

    companion object {
        private const val EVENT_ACTION_CLICK_FORMAT = "click - %s"

        private const val CONTENT_TYPE_USER = "content-user"
        private const val CONTENT_TYPE_SHOP = "content-shop"
    }
}
