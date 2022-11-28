package com.tokopedia.feedplus.view.analytics.shorts

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.feedplus.view.analytics.CONTENT_FEED_TIMELINE
import com.tokopedia.feedplus.view.analytics.FORMAT_TWO_PARAM
import com.tokopedia.feedplus.view.analytics.MARKETPLACE
import com.tokopedia.feedplus.view.analytics.PLAY
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
class PlayShortsInFeedAnalytic @Inject constructor(
    private val userSession: UserSessionInterface
) {

    /**
     * Mynakama Tracker
     * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
     *
     * Row 1 : eventLabel is {partnerId} - {seller / user} but we can't identified the user
     * as seller / user bcs it can be both
     */
    fun clickCreateShortsEntryPoint() {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    EVENT_NAME to EVENT_CLICK_CONTENT,
                    EVENT_ACTION to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "buat video"
                    ),
                    EVENT_CATEGORY to CONTENT_FEED_TIMELINE,
                    EVENT_LABEL to String.format(
                        /** TODO: need to determine {partnerId} - {seller / user} */
                        FORMAT_TWO_PARAM,
                        "",
                        ""
                    ),
                    EVENT_BUSINESSUNIT to PLAY,
                    EVENT_CURRENTSITE to MARKETPLACE,
                    EVENT_TRACKER_ID to "37524",
                    EVENT_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId
                )
            )
    }

    /**
     * Row 64
     */
    fun clickRetryUploadShorts(
        authorId: String,
        authorType: String
    ) {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    EVENT_NAME to EVENT_CLICK_CONTENT,
                    EVENT_ACTION to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "coba lagi short video"
                    ),
                    EVENT_CATEGORY to EVENT_CATEGORY_SHORTS,
                    EVENT_LABEL to "$authorId - ${getAnalyticAuthorType(authorType)}",
                    EVENT_BUSINESSUNIT to PLAY,
                    EVENT_CURRENTSITE to MARKETPLACE,
                    EVENT_TRACKER_ID to "37587",
                    EVENT_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId
                )
            )
    }

    /**
     * Row 66
     */
    fun clickRedirectToChannelRoom(
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
                        "liat video uploaded"
                    ),
                    EVENT_CATEGORY to EVENT_CATEGORY_SHORTS,
                    EVENT_LABEL to "$authorId - ${getAnalyticAuthorType(authorType)} - $channelId",
                    EVENT_BUSINESSUNIT to PLAY,
                    EVENT_CURRENTSITE to MARKETPLACE,
                    EVENT_TRACKER_ID to "37589",
                    EVENT_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId
                )
            )
    }

    /**
     * Row 79 : eventLabel is {partnerId} - {seller / user} but we can't identified the user
     * as seller / user bcs it can be both
     */
    fun viewShortsEntryPoint() {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    EVENT_NAME to EVENT_VIEW_CONTENT,
                    EVENT_ACTION to String.format(
                        EVENT_ACTION_VIEW_FORMAT,
                        "buat video"
                    ),
                    EVENT_CATEGORY to CONTENT_FEED_TIMELINE,
                    EVENT_LABEL to String.format(
                        /** TODO: need to determine {partnerId} - {seller / user} */
                        FORMAT_TWO_PARAM,
                        "",
                        ""
                    ),
                    EVENT_BUSINESSUNIT to PLAY,
                    EVENT_CURRENTSITE to MARKETPLACE,
                    EVENT_TRACKER_ID to "37602",
                    EVENT_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId
                )
            )
    }

    private fun getAnalyticAuthorType(authorType: String): String {
        return when(authorType) {
            ContentCommonUserType.TYPE_USER -> AUTHOR_TYPE_USER
            ContentCommonUserType.TYPE_SHOP -> AUTHOR_TYPE_SELLER
            else -> ""
        }
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

        private const val EVENT_CATEGORY_SHORTS = "play broadcast short"
        private const val EVENT_CLICK_CONTENT = "clickContent"
        private const val EVENT_VIEW_CONTENT = "viewContentIris"
        private const val EVENT_ACTION_CLICK_FORMAT = "click - %s"
        private const val EVENT_ACTION_VIEW_FORMAT = "view - %s"

        private const val AUTHOR_TYPE_USER = "user"
        private const val AUTHOR_TYPE_SELLER = "seller"
    }
}
