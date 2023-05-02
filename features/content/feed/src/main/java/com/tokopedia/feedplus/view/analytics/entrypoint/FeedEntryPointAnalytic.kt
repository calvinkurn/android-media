package com.tokopedia.feedplus.view.analytics.entrypoint

import com.tokopedia.feedplus.view.analytics.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 30/09/21
 */
class FeedEntryPointAnalytic @Inject constructor(
    private val userSession: UserSessionInterface
) {

    private val shopId: String
        get() = userSession.shopId

    private val userId: String
        get() = userSession.userId

    /**
     * {
        "event":"clickFeed",
        "eventAction":"click - plus button",
        "eventCategory":"content feed timeline",
        "eventLabel":"{user_id} - {shop_id}",
        "businessUnit":"content",
        "currentSite":"tokopediamarketplace",
        "sessionIris":"{session_Iris}"
        }
     */
    fun clickMainEntryPoint() {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    EVENT_NAME to EVENT_CLICK_FEED,
                    EVENT_ACTION to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "plus button"
                    ),
                    EVENT_CATEGORY to CONTENT_FEED_TIMELINE,
                    EVENT_LABEL to String.format(
                        FORMAT_TWO_PARAM,
                        userId,
                        shopId
                    ),
                    EVENT_BUSINESSUNIT to CONTENT,
                    EVENT_CURRENTSITE to MARKETPLACE
                )
            )
    }

    /**
     * {
        "event":"clickFeed",
        "eventAction":"click - buat post",
        "eventCategory":"content feed timeline",
        "eventLabel":"{user_id} - {shop_id}",
        "businessUnit":"content",
        "currentSite":"tokopediamarketplace",
        "sessionIris":"{session_Iris}"
        }
     */
    fun clickCreatePostEntryPoint() {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    EVENT_NAME to EVENT_CLICK_FEED,
                    EVENT_ACTION to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "buat post"
                    ),
                    EVENT_CATEGORY to CONTENT_FEED_TIMELINE,
                    EVENT_LABEL to String.format(
                        FORMAT_TWO_PARAM,
                        userId,
                        shopId
                    ),
                    EVENT_BUSINESSUNIT to CONTENT,
                    EVENT_CURRENTSITE to MARKETPLACE
                )
            )
    }

    /**
     * {
        "event":"clickFeed",
        "eventAction":"click - buat live",
        "eventCategory":"content feed timeline",
        "eventLabel":"{user_id} - {shop_id}",
        "businessUnit":"content",
        "currentSite":"tokopediamarketplace",
        "sessionIris":"{session_Iris}"
        }
     */
    fun clickCreateLiveEntryPoint() {
        TrackApp.getInstance().gtm
            .sendGeneralEvent(
                mapOf(
                    EVENT_NAME to EVENT_CLICK_FEED,
                    EVENT_ACTION to String.format(
                        EVENT_ACTION_CLICK_FORMAT,
                        "buat live"
                    ),
                    EVENT_CATEGORY to CONTENT_FEED_TIMELINE,
                    EVENT_LABEL to String.format(
                        FORMAT_TWO_PARAM,
                        userId,
                        shopId
                    ),
                    EVENT_BUSINESSUNIT to CONTENT,
                    EVENT_CURRENTSITE to MARKETPLACE
                )
            )
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

        private const val EVENT_CLICK_FEED = "clickFeed"
        private const val EVENT_CLICK_CONTENT = "clickContent"
        private const val EVENT_VIEW_CONTENT = "viewContentIris"
        private const val EVENT_ACTION_CLICK_FORMAT = "click - %s"
        private const val EVENT_ACTION_VIEW_FORMAT = "view - %s"
    }
}
