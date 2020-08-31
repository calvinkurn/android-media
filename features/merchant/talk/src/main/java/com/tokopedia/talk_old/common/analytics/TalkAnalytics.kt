package com.tokopedia.talk_old.common.analytics

import android.app.Activity
import com.tokopedia.talk_old.producttalk.view.data.ChatBannerUiModel
import com.tokopedia.talk_old.talkdetails.view.activity.TalkDetailsActivity
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author by nisie on 8/28/18.
 */
class TalkAnalytics @Inject constructor() {
    private val EVENT_CLICK_INBOX_CHAT: String = "clickInboxChat"
    private val EVENT_CLICK_SHOP_PAGE: String = "clickShopPage"
    private val EVENT_CLICK_PDP: String = "clickPDP"

    private val CATEGORY_INBOX_TALK: String = "inbox - talk"
    private val CATEGORY_SHOP_PAGE: String = "shop page"
    private val CATEGORY_PDP: String = "product detail page"
    private val CATEGORY_PRODUCT_TALK: String = "product talk"

    private val ACTION_CREATE_NEW_TALK: String = "click - kirim to create new talk"
    private val ACTION_REPLY_TALK: String = "click - kirim to reply talk"
    private val ACTION_CLICK_CHAT_TICKER: String = "click chat on ticker"

    private val KEY_PRODUCT_ID = "productId"

    fun trackSendCommentTalk(source: String, talkId: String, productId: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click on send comment on discussion box",
                    ""
            ))
        } else {
            val data = mapOf(
                    TrackAppUtils.EVENT to EVENT_CLICK_PDP,
                    TrackAppUtils.EVENT_CATEGORY to CATEGORY_PRODUCT_TALK,
                    TrackAppUtils.EVENT_ACTION to ACTION_REPLY_TALK,
                    TrackAppUtils.EVENT_LABEL to talkId,
                    KEY_PRODUCT_ID to productId
            )

            TrackApp.getInstance().gtm.sendGeneralEvent(data)
        }
    }

    fun sendScreen(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackClickReplyButton(talkId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click on reply discussion",
                talkId
        ))
    }

    fun trackClickReplyButtonFromShop(talkId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_SHOP_PAGE,
                CATEGORY_SHOP_PAGE,
                "click on reply discussion box",
                talkId
        ))
    }

    fun trackClickProduct() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click link / photo product",
                ""
        ))
    }

    fun trackClickProductFromAttachment() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_SHOP_PAGE,
                CATEGORY_SHOP_PAGE,
                "click link / photo from talk",
                ""
        ))
    }

    fun trackClickProductFromAttachmentInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            trackClickProductFromAttachmentFromShop()
        } else {
            trackClickProductFromAttachment()
        }
    }

    fun trackClickProductFromAttachmentFromShop() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_SHOP_PAGE,
                CATEGORY_SHOP_PAGE,
                "click product link from talk on shop page",
                ""
        ))
    }

    fun trackSelectTab(tabName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click on tab",
                tabName
        ))
    }

    fun trackClickFilter(filter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "choose filter",
                filter
        ))
    }


    fun trackClickOnMenuDelete() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click three balls menu",
                "delete talk"
        ))
    }

    fun trackClickOnMenuDeleteInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click three balls menu",
                    "delete talk"
            ))
        } else {
            trackClickOnMenuDelete()
        }
    }

    fun trackClickOnMenuFollow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click three balls menu",
                "follow talk"
        ))
    }


    fun trackClickOnMenuFollowInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click three balls menu",
                    "follow talk"
            ))
        } else {
            trackClickOnMenuFollow()
        }
    }

    fun trackClickOnMenuUnfollow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click three balls menu",
                "unfollow talk"
        ))
    }

    fun trackClickOnMenuUnfollowInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click three balls menu",
                    "unfollow talk"
            ))
        } else {
            trackClickOnMenuUnfollow()
        }
    }

    fun trackClickOnMenuReport() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click three balls menu",
                "click on laporkan"
        ))
    }


    fun trackClickOnMenuReportInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CLICK_SHOP_PAGE,
                    CATEGORY_SHOP_PAGE,
                    "click three balls menu",
                    "click on laporkan"
            ))
        } else {
            trackClickOnMenuReport()
        }
    }

    fun trackClickUserProfile() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_INBOX_CHAT,
                CATEGORY_INBOX_TALK,
                "click user profile from talk on inbox",
                ""
        ))
    }

    fun trackClickUserProfileFromShop() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_SHOP_PAGE,
                CATEGORY_SHOP_PAGE,
                "click user profile from talk on shop page",
                ""
        ))
    }

    fun trackClickUserProfileInDetail(source: String) {
        if (source == TalkDetailsActivity.SOURCE_SHOP) {
            trackClickUserProfileFromShop()
        } else {
            trackClickUserProfile()
        }
    }

    fun trackClickSendNewTalk(productId: String) {
        val data = mapOf(
                TrackAppUtils.EVENT to EVENT_CLICK_PDP,
                TrackAppUtils.EVENT_CATEGORY to CATEGORY_PRODUCT_TALK,
                TrackAppUtils.EVENT_ACTION to ACTION_CREATE_NEW_TALK,
                TrackAppUtils.EVENT_LABEL to "",
                KEY_PRODUCT_ID to productId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    //#CT
    fun trackOnClickChatBanner(element: ChatBannerUiModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_PDP,
                CATEGORY_PDP,
                ACTION_CLICK_CHAT_TICKER,
                element.productId
        )
    }

    companion object {

        val SCREEN_NAME_INBOX_TALK: String = "Inbox Talk"
        val SCREEN_NAME_REPORT_TALK: String = "Report Talk"
        val SCREEN_NAME_SHOP_TALK: String = "Shop Talk"
        val SCREEN_NAME_PRODUCT_TALK: String = "Product Talk"
        val SCREEN_NAME_ADD_TALK: String = "Add Talk"

    }
}