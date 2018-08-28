package com.tokopedia.talk.common

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker

/**
 * @author by nisie on 8/28/18.
 */
class TalkAnalytics(val tracker: AnalyticTracker) {

    companion object {
        fun createInstance(analyticTracker: AnalyticTracker): TalkAnalytics {
            return TalkAnalytics(analyticTracker)
        }

        val SCREEN_NAME_INBOX_TALK: String = "Inbox Talk Page"
    }
}