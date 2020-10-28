package com.tokopedia.talk.feature.reply.analytics

import android.os.Bundle
import com.tokopedia.talk.common.analytics.TalkEventTracking
import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.track.TrackApp

object TalkReplyTracking {

    private fun eventTalkReply(action: String, label: String, userId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TalkEventTracking(
                TalkTrackingConstants.EVENT_CATEGORY_TALK, action, label, userId, productId
        ).dataTracking)
    }

    fun eventSendAnswer(userId: String, productId: String, talkId: String) {
        with(TalkReplyTrackingConstants) {
            eventTalkReply(EVENT_ACTION_CLICK_SEND, String.format(EVENT_LABEL_CLICK_SEND, talkId), userId, productId)
        }
    }

    fun eventClickCard(inboxType: String, userId: String, productName: String, productId: String, position: Int) {
        val eventCategory = String.format(TalkTrackingConstants.EVENT_CATEGORY_INBOX_PRODUCT, inboxType)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TalkReplyTrackingConstants.EVENT_PRODUCT_CLICK, Bundle().apply {
            putString(TalkTrackingConstants.TRACKING_EVENT_ACTION, TalkReplyTrackingConstants.EVENT_ACTION_CLICK_PRODUCT_CARD)
            putString(TalkTrackingConstants.TRACKING_EVENT_CATEGORY, eventCategory)
            putString(TalkTrackingConstants.TRACKING_USER_ID, userId)
            putString(TalkTrackingConstants.TRACKING_SCREEN_NAME, TalkReplyTrackingConstants.INBOX_SCREEN_NAME)
            putString(TalkTrackingConstants.TRACKING_CURRENT_SITE, TalkTrackingConstants.CURRENT_SITE_TALK)
            putString(TalkTrackingConstants.TRACKING_BUSINESS_UNIT, TalkTrackingConstants.BUSINESS_UNIT_TALK)
            putString(TalkTrackingConstants.TRACKING_ECOMMERCE, mapOf<String, Any>(
                    TalkTrackingConstants.TRACKING_CLICK to mapOf(
                            TalkTrackingConstants.TRACKING_ACTION_FIELD to "${TalkTrackingConstants.TRACKING_LIST to eventCategory}",
                            TalkTrackingConstants.TRACKING_PRODUCTS to listOf(
                                    mapOf(
                                            TalkTrackingConstants.TRACKING_NAME to productName,
                                            TalkTrackingConstants.TRACKING_ID to productId,
                                            TalkTrackingConstants.TRACKING_LIST to eventCategory,
                                            TalkTrackingConstants.TRACKING_POSITION to position.toString()
                                    )
                            )
                    )).toString()
            )
        })
    }

    fun eventImpressCard(inboxType: String, userId: String, productName: String, productId: String, position: Int) {
        val eventCategory = String.format(TalkTrackingConstants.EVENT_CATEGORY_INBOX_PRODUCT, inboxType)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TalkReplyTrackingConstants.EVENT_PRODUCT_VIEW, Bundle().apply {
            putString(TalkTrackingConstants.TRACKING_EVENT_ACTION, TalkReplyTrackingConstants.EVENT_ACTION_VIEW_PRODUCT_CARD)
            putString(TalkTrackingConstants.TRACKING_EVENT_CATEGORY, eventCategory)
            putString(TalkTrackingConstants.TRACKING_USER_ID, userId)
            putString(TalkTrackingConstants.TRACKING_SCREEN_NAME, TalkReplyTrackingConstants.INBOX_SCREEN_NAME)
            putString(TalkTrackingConstants.TRACKING_CURRENT_SITE, TalkTrackingConstants.CURRENT_SITE_TALK)
            putString(TalkTrackingConstants.TRACKING_BUSINESS_UNIT, TalkTrackingConstants.BUSINESS_UNIT_TALK)
            putString(TalkTrackingConstants.TRACKING_ECOMMERCE, mapOf(
                    TalkTrackingConstants.TRACKING_CURRENCY_CODE to TalkTrackingConstants.IDR_CURRENCY,
                    TalkTrackingConstants.TRACKING_IMPRESSIONS to mapOf(
                            TalkTrackingConstants.TRACKING_ACTION_FIELD to "${TalkTrackingConstants.TRACKING_LIST to eventCategory}",
                            TalkTrackingConstants.TRACKING_PRODUCTS to listOf(
                                    mapOf(
                                            TalkTrackingConstants.TRACKING_NAME to productName,
                                            TalkTrackingConstants.TRACKING_ID to productId,
                                            TalkTrackingConstants.TRACKING_LIST to eventCategory,
                                            TalkTrackingConstants.TRACKING_POSITION to position.toString()
                                    )
                            )
                    )).toString()
            )
        })
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