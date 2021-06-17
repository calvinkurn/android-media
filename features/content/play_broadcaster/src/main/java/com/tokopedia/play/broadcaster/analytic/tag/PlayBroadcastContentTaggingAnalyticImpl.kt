package com.tokopedia.play.broadcaster.analytic.tag

import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 28/04/21
 */
class PlayBroadcastContentTaggingAnalyticImpl @Inject constructor(
        private val userSession: UserSessionInterface
) : PlayBroadcastContentTaggingAnalytic {

    private val shopId: String
        get() = userSession.shopId

    override fun selectRecommendedTags(selectedTags: Set<String>) {
        sendEvent(
                eventName = KEY_TRACK_CLICK_EVENT,
                eventAction = "click recommendation tag",
                eventLabel = "$shopId${selectedTags.joinToString(prefix = " - ", separator = " - ")}"
        )
    }

    override fun proceedFromContentTagging() {
        sendEvent(
                eventName = KEY_TRACK_CLICK_EVENT,
                eventAction = "click continue from recommendation tag",
                eventLabel = shopId
        )
    }

    private fun sendEvent(
            eventName: String,
            eventAction: String,
            eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to eventName,
                        KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                        KEY_EVENT_ACTION to eventAction,
                        KEY_EVENT_LABEL to eventLabel,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SHOP_ID to userSession.shopId,
                        KEY_USER_ID to userSession.userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }
}