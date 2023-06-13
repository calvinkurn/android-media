package com.tokopedia.play_common.shortsuploader.analytic

import com.tokopedia.config.GlobalConfig
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
                    EVENT_NAME to EVENT_CLICK_CONTENT,
                    EVENT_ACTION to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "lihat video uploaded"
                    ),
                    EVENT_CATEGORY to EVENT_CATEGORY_SHORTS,
                    EVENT_LABEL to "$authorId - ${getAnalyticAuthorType(authorType)} - $channelId",
                    EVENT_BUSINESSUNIT to EVENT_BUSINESS_UNIT_PLAY,
                    EVENT_CURRENTSITE to currentSite,
                    EVENT_TRACKER_ID to getTrackerIdBySite("37589", "37671"),
                    EVENT_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                    EVENT_USER_ID to userSession.userId
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
                    EVENT_NAME to EVENT_CLICK_CONTENT,
                    EVENT_ACTION to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "coba lagi coachmark"
                    ),
                    EVENT_CATEGORY to EVENT_CATEGORY_SHORTS,
                    EVENT_LABEL to "$authorId - ${getAnalyticAuthorType(authorType)}",
                    EVENT_BUSINESSUNIT to EVENT_BUSINESS_UNIT_PLAY,
                    EVENT_CURRENTSITE to currentSite,
                    EVENT_TRACKER_ID to getTrackerIdBySite("37591", "37673"),
                    EVENT_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                    EVENT_USER_ID to userSession.userId
                )
            )
    }

    private fun getAnalyticAuthorType(authorType: String): String {
        return when(authorType) {
            CONTENT_TYPE_USER -> AUTHOR_TYPE_USER
            CONTENT_TYPE_SHOP -> AUTHOR_TYPE_SELLER
            else -> ""
        }
    }

    private val currentSite: String
        get() = if(GlobalConfig.isSellerApp()) EVENT_CURRENT_SITE_SA
                else EVENT_CURRENT_SITE_MA

    private fun getTrackerIdBySite(mainAppTrackerId: String, sellerAppTrackerId: String): String {
        return if(GlobalConfig.isSellerApp()) sellerAppTrackerId
        else mainAppTrackerId
    }

    companion object {
        private const val EVENT_NAME = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val EVENT_BUSINESSUNIT = "businessUnit"
        private const val EVENT_CURRENTSITE = "currentSite"
        private const val EVENT_TRACKER_ID = "trackerId"
        private const val EVENT_SESSION_IRIS = "sessionIris"
        private const val EVENT_USER_ID = "userId"

        private const val EVENT_CATEGORY_SHORTS = "play broadcast short"
        private const val EVENT_CLICK_CONTENT = "clickContent"
        private const val EVENT_VIEW_CONTENT = "viewContentIris"
        private const val EVENT_ACTION_CLICK_FORMAT = "click - %s"
        private const val EVENT_ACTION_VIEW_FORMAT = "view - %s"
        private const val EVENT_BUSINESS_UNIT_PLAY = "play"
        private const val EVENT_CURRENT_SITE_MA = "tokopediamarketplace"
        private const val EVENT_CURRENT_SITE_SA = "tokopediaseller"

        private const val CONTENT_TYPE_USER = "content-user"
        private const val CONTENT_TYPE_SHOP = "content-shop"

        private const val AUTHOR_TYPE_USER = "user"
        private const val AUTHOR_TYPE_SELLER = "seller"
    }
}
