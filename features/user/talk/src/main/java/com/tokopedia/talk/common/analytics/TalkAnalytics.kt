package com.tokopedia.talk.common.analytics

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
        val SCREEN_NAME_REPORT_TALK: String = "Report Talk Page"
        val SCREEN_NAME_SHOP_TALK: String = "Shop Talk Page"
        val SCREEN_NAME_PRODUCT_TALK: String = "Product Talk Page"
        val SCREEN_NAME_ADD_TALK: String = "Add Talk Page"

    }
}