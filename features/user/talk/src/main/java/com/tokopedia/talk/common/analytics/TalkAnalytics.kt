package com.tokopedia.talk.common.analytics

import android.app.Activity
import android.support.v4.app.FragmentActivity
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import javax.inject.Inject

/**
 * @author by nisie on 8/28/18.
 */
class TalkAnalytics @Inject constructor(val tracker: AnalyticTracker) {
    private val EVENT_CLICK_INBOX_CHAT: String = "clickInboxChat"
    private val EVENT_CLICK_SHOP_PAGE: String = "clickShopPage"

    private val CATEGORY_INBOX_TALK: String = "inbox - talk"
    private val CATEGORY_SHOP_PAGE: String = "shop page"

    fun trackSendCommentTalk(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            tracker.sendEventTracking(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click on send comment on discussion box",
                    ""
            )
        } else {
            tracker.sendEventTracking(
                    EVENT_CLICK_INBOX_CHAT,
                    CATEGORY_INBOX_TALK,
                    "send comment talk",
                    source
            )
        }
    }

    fun sendScreen(activity: Activity, screenName: String) {
        tracker.sendScreen(activity, screenName)
    }

    companion object {
        fun createInstance(analyticTracker: AnalyticTracker): TalkAnalytics {
            return TalkAnalytics(analyticTracker)
        }

        val SCREEN_NAME_INBOX_TALK: String = "Inbox Talk"
        val SCREEN_NAME_REPORT_TALK: String = "Report Talk"
        val SCREEN_NAME_SHOP_TALK: String = "Shop Talk"
        val SCREEN_NAME_PRODUCT_TALK: String = "Product Talk"
        val SCREEN_NAME_ADD_TALK: String = "Add Talk"

    }
}