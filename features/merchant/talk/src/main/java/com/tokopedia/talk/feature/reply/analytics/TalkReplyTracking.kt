package com.tokopedia.talk.feature.reply.analytics

import com.tokopedia.talk.common.analytics.TalkEventTracking
import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTrackingConstants
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.track.TrackApp

object TalkReplyTracking {

    private fun eventTalkReply(action: String, label: String, userId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TalkEventTracking(
                TalkTrackingConstants.EVENT_CATEGORY_TALK, action, label, userId, productId
        ).dataTracking)
    }

    private fun getInboxType(inboxType: String): String {
        return when (inboxType) {
            TalkInboxTab.SHOP_OLD -> {
                TalkInboxTrackingConstants.TAB_SELLER
            }
            TalkInboxTab.BUYER_TAB -> {
                TalkInboxTrackingConstants.TAB_BUYER
            }
            else -> {
                ""
            }
        }
    }

    fun eventSendAnswer(userId: String, productId: String, talkId: String, isProductCardShown: Boolean) {
        with(TalkReplyTrackingConstants) {
            eventTalkReply(EVENT_ACTION_CLICK_SEND, String.format(EVENT_LABEL_CLICK_SEND, talkId, if (isProductCardShown) INBOX_PAGE else PRODUCT_PAGE), userId, productId)
        }
    }

    fun eventClickCard(inboxType: String, userId: String, productName: String, productId: String, position: Int) {
        val eventCategory = String.format(TalkTrackingConstants.EVENT_CATEGORY_INBOX_PRODUCT, getInboxType(inboxType))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                mapOf(
                        TalkTrackingConstants.TRACKING_EVENT to TalkReplyTrackingConstants.EVENT_PRODUCT_CLICK,
                        TalkTrackingConstants.TRACKING_EVENT_ACTION to TalkReplyTrackingConstants.EVENT_ACTION_CLICK_PRODUCT_CARD,
                        TalkTrackingConstants.TRACKING_EVENT_CATEGORY to eventCategory,
                        TalkTrackingConstants.TRACKING_EVENT_LABEL to "",
                        TalkTrackingConstants.TRACKING_USER_ID to userId,
                        TalkTrackingConstants.TRACKING_SCREEN_NAME to TalkReplyTrackingConstants.INBOX_SCREEN_NAME,
                        TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                        TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK,
                        TalkTrackingConstants.TRACKING_ECOMMERCE to mapOf<String, Any>(
                                TalkTrackingConstants.TRACKING_CLICK to mapOf(
                                        TalkTrackingConstants.TRACKING_ACTION_FIELD to mapOf(TalkTrackingConstants.TRACKING_LIST to eventCategory),
                                        TalkTrackingConstants.TRACKING_PRODUCTS to listOf(
                                                mapOf(
                                                        TalkTrackingConstants.TRACKING_NAME to productName,
                                                        TalkTrackingConstants.TRACKING_ID to productId,
                                                        TalkTrackingConstants.TRACKING_LIST to eventCategory,
                                                        TalkTrackingConstants.TRACKING_POSITION to position.toString()
                                                )
                                        )
                                ))
                )
        )
    }

    fun eventImpressCard(inboxType: String, userId: String, productName: String, productId: String, position: Int) {
        val eventCategory = String.format(TalkTrackingConstants.EVENT_CATEGORY_INBOX_PRODUCT, getInboxType(inboxType))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                mapOf(
                        TalkTrackingConstants.TRACKING_EVENT to TalkReplyTrackingConstants.EVENT_PRODUCT_VIEW,
                        TalkTrackingConstants.TRACKING_EVENT_ACTION to TalkReplyTrackingConstants.EVENT_ACTION_VIEW_PRODUCT_CARD,
                        TalkTrackingConstants.TRACKING_EVENT_CATEGORY to eventCategory,
                        TalkTrackingConstants.TRACKING_EVENT_LABEL to "",
                        TalkTrackingConstants.TRACKING_USER_ID to userId,
                        TalkTrackingConstants.TRACKING_SCREEN_NAME to TalkReplyTrackingConstants.INBOX_SCREEN_NAME,
                        TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                        TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK,
                        TalkTrackingConstants.TRACKING_ECOMMERCE to mapOf(
                                TalkTrackingConstants.TRACKING_CURRENCY_CODE to TalkTrackingConstants.IDR_CURRENCY,
                                TalkTrackingConstants.TRACKING_IMPRESSIONS to listOf(
                                        mapOf(
                                                TalkTrackingConstants.TRACKING_NAME to productName,
                                                TalkTrackingConstants.TRACKING_ID to productId,
                                                TalkTrackingConstants.TRACKING_LIST to eventCategory,
                                                TalkTrackingConstants.TRACKING_POSITION to position.toString()
                                        )
                                )
                        )
                )
        )
    }

    fun sendScreen(screenName: String, productId: String, userId: String) {
        with(TalkTrackingConstants) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                    mapOf(
                            TRACKING_EVENT to EVENT_OPEN_SCREEN,
                            TRACKING_SCREEN_NAME to screenName,
                            TRACKING_PRODUCT_ID to productId,
                            TRACKING_CURRENT_SITE to CURRENT_SITE_TALK,
                            TRACKING_IS_LOGGED_IN to TalkReplyTrackingConstants.IS_LOGGED_IN,
                            TRACKING_USER_ID to userId,
                            TRACKING_BUSINESS_UNIT to BUSINESS_UNIT_TALK
                    )
            )
        }
    }
}