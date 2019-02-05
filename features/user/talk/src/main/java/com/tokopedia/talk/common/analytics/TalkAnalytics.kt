package com.tokopedia.talk.common.analytics

import android.app.Activity
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

    fun trackClickReplyButton(talkId: String) {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click on reply discussion",
                talkId
        )
    }

    fun trackClickReplyButtonFromShop(talkId: String) {
        tracker.sendEventTracking(
                EVENT_CLICK_SHOP_PAGE,
                CATEGORY_SHOP_PAGE,
                "click on reply discussion box",
                talkId
        )
    }

    fun trackClickProduct() {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click link / photo product",
                ""
        )
    }

    fun trackClickProductFromAttachment() {
        tracker.sendEventTracking(
                EVENT_CLICK_SHOP_PAGE,
                CATEGORY_SHOP_PAGE,
                "click link / photo from talk",
                ""
        )
    }

    fun trackClickProductFromAttachmentInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            trackClickProductFromAttachmentFromShop()
        } else {
            trackClickProductFromAttachment()
        }
    }

    fun trackClickProductFromAttachmentFromShop() {
        tracker.sendEventTracking(
                EVENT_CLICK_SHOP_PAGE,
                CATEGORY_SHOP_PAGE,
                "click product link from talk on shop page",
                ""
        )
    }

    fun trackSelectTab(tabName: String) {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click on tab",
                tabName
        )
    }

    fun trackClickFilter(filter: String) {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "choose filter",
                filter
        )
    }


    fun trackClickOnMenuDelete() {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click three balls menu",
                "delete talk"
        )
    }

    fun trackClickOnMenuDeleteInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            tracker.sendEventTracking(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click three balls menu",
                    "delete talk"
            )
        } else {
            trackClickOnMenuDelete()
        }
    }

    fun trackClickOnMenuFollow() {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click three balls menu",
                "follow talk"
        )
    }


    fun trackClickOnMenuFollowInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            tracker.sendEventTracking(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click three balls menu",
                    "follow talk"
            )
        } else {
            trackClickOnMenuFollow()
        }
    }

    fun trackClickOnMenuUnfollow() {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click three balls menu",
                "unfollow talk"
        )
    }

    fun trackClickOnMenuUnfollowInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            tracker.sendEventTracking(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click three balls menu",
                    "unfollow talk"
            )
        } else {
            trackClickOnMenuUnfollow()
        }
    }

    fun trackClickOnMenuReport() {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click three balls menu",
                "click on laporkan"
        )
    }


    fun trackClickOnMenuReportInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            tracker.sendEventTracking(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click three balls menu",
                    "click on laporkan"
            )
        } else {
            trackClickOnMenuReport()
        }
    }

    fun trackClickUserProfile() {
        tracker.sendEventTracking(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click user profile from talk on inbox",
                ""
        )
    }

    fun trackClickUserProfileFromShop() {
        tracker.sendEventTracking(
                EVENT_CLICK_SHOP_PAGE,
                CATEGORY_SHOP_PAGE,
                "click user profile from talk on shop page",
                ""
        )
    }

    fun trackClickUserProfileInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            trackClickUserProfileFromShop()
        } else {
            trackClickUserProfile()
        }
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