package com.tokopedia.talk.feature.inbox.analytics

import android.os.Bundle
import com.tokopedia.talk.common.analytics.TalkEventTracking
import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.sellersettings.common.analytics.TalkSellerSettingsTrackingConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class TalkInboxTracking @Inject constructor() {

    private val tracker = TrackApp.getInstance().gtm

    fun eventClickFilter(filter: String, tab: String, counter: Long, filterStatus: Boolean, shopId: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TalkEventTracking(
                        category = getEventCategoryInbox(tab),
                        action = TalkInboxTrackingConstants.EVENT_ACTION_CLICK_FILTER,
                        label = String.format(TalkInboxTrackingConstants.EVENT_LABEL_CLICK_FILTER,
                                getFilter(filter),
                                getFilterStatus(filterStatus),
                                shopId,
                                counter.toString()),
                        userId = userId,
                        productId = "",
                        screenName = TalkInboxTrackingConstants.SCREEN_NAME
                ).dataTracking
        )
    }

    fun eventLazyLoad(tab: String, page: Int, numberOfUnread: Int, numberOfRead: Int, shopId: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TalkEventTracking(
                        category = getEventCategoryInbox(tab),
                        action = TalkInboxTrackingConstants.EVENT_ACTION_LAZY_LOAD,
                        label = String.format(TalkInboxTrackingConstants.EVENT_LABEL_LAZY_LOAD,
                                page.toString(),
                                numberOfUnread.toString(),
                                numberOfRead.toString(),
                                shopId),
                        userId = userId,
                        productId = "",
                        screenName = TalkInboxTrackingConstants.SCREEN_NAME
                ).dataTracking
        )
    }

    fun eventClickTab(tab: String, userId: String, shopId: String, countUnreadMessages: Long) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TalkEventTracking(
                        category = getEventCategoryInbox(tab),
                        action = getTab(tab),
                        label = String.format(TalkInboxTrackingConstants.EVENT_LABEL_CLICK_TAB,
                                shopId,
                                countUnreadMessages.toString()),
                        userId = userId,
                        productId = "",
                        screenName = TalkInboxTrackingConstants.SCREEN_NAME
                ).dataTracking
        )
    }

    fun eventClickThread(tab: String, talkId: String, productId: String, filterActive: String, isRead: Boolean, shopId: String, countUnreadMessages: Long, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TalkEventTracking(
                        category = getEventCategoryInbox(tab),
                        action = TalkInboxTrackingConstants.EVENT_ACTION_CLICK_TALK_MESSAGE,
                        label = String.format(TalkInboxTrackingConstants.EVENT_LABEL_CLICK_TALK,
                                talkId,
                                productId,
                                getFilter(filterActive),
                                getStatusRead(isRead),
                                shopId,
                                countUnreadMessages.toString()),
                        userId = userId,
                        productId = productId,
                        screenName = TalkInboxTrackingConstants.SCREEN_NAME
                ).dataTracking
        )
    }

    fun eventClickThreadEcommerce(inboxType: String, talkId: String, userId: String, position: Int, isUnread: Boolean) {
        val itemBundle = Bundle().apply {
            putString(TalkTrackingConstants.TRACKING_ID, talkId)
            putString(TalkTrackingConstants.TRACKING_NAME, String.format(TalkInboxTrackingConstants.EE_NAME, getInboxType(inboxType)))
            putString(TalkTrackingConstants.TRACKING_CREATIVE, String.format(TalkInboxTrackingConstants.CREATIVE_MESSAGE_STATUS, getStatusRead(!isUnread)))
            putString(TalkTrackingConstants.TRACKING_CREATIVE_URL, "")
            putString(TalkTrackingConstants.TRACKING_POSITION, position.toString())
        }

        val eventDataLayer = Bundle().apply {
            putString(TalkTrackingConstants.TRACKING_EVENT_ACTION, String.format(TalkTrackingConstants.EVENT_CATEGORY_INBOX_PRODUCT, getInboxType(inboxType)))
            putString(TalkTrackingConstants.TRACKING_EVENT_CATEGORY, "")
            putString(TalkTrackingConstants.TRACKING_EVENT_LABEL, TalkInboxTrackingConstants.EVENT_ACTION_CLICK_TALK_MESSAGE)
            putString(TalkTrackingConstants.TRACKING_SCREEN_NAME, TalkInboxTrackingConstants.SCREEN_NAME)
            putString(TalkTrackingConstants.TRACKING_USER_ID, userId)
            putString(TalkTrackingConstants.TRACKING_CURRENT_SITE, TalkTrackingConstants.CURRENT_SITE_TALK)
            putString(TalkTrackingConstants.TRACKING_BUSINESS_UNIT, TalkTrackingConstants.BUSINESS_UNIT_TALK)
            putParcelableArrayList(TalkTrackingConstants.TRACKING_ECOMMERCE, arrayListOf(itemBundle))
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TalkInboxTrackingConstants.EVENT_PROMO_VIEW, eventDataLayer)
    }

    fun openScreen(screenName: String) {
        tracker.sendScreenAuthenticated(screenName, mapOf(
                TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK
        ))
    }

    fun eventItemImpress(inboxType: String, talkId: String, userId: String, position: Int, isUnread: Boolean, trackingQueue: TrackingQueue) {
        val eventCategory = String.format(TalkTrackingConstants.EVENT_CATEGORY_INBOX_PRODUCT, getInboxType(inboxType))
        trackingQueue.putEETracking(
                hashMapOf(
                        TalkTrackingConstants.TRACKING_EVENT to TalkInboxTrackingConstants.EVENT_PROMO_VIEW,
                        TalkTrackingConstants.TRACKING_EVENT_ACTION to TalkInboxTrackingConstants.EVENT_ACTION_IMPRESS_ITEM,
                        TalkTrackingConstants.TRACKING_EVENT_LABEL to "",
                        TalkTrackingConstants.TRACKING_EVENT_CATEGORY to eventCategory,
                        TalkTrackingConstants.TRACKING_USER_ID to userId,
                        TalkTrackingConstants.TRACKING_SCREEN_NAME to TalkInboxTrackingConstants.SCREEN_NAME,
                        TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                        TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK,
                        TalkTrackingConstants.TRACKING_ECOMMERCE to mapOf(
                                TalkInboxTrackingConstants.EVENT_PROMO_VIEW to mapOf(
                                        TalkTrackingConstants.TRACKING_PROMOTIONS to listOf(
                                                mapOf(
                                                        TalkTrackingConstants.TRACKING_ID to talkId,
                                                        TalkTrackingConstants.TRACKING_NAME to eventCategory,
                                                        TalkTrackingConstants.TRACKING_CREATIVE to String.format(TalkInboxTrackingConstants.CREATIVE_MESSAGE_STATUS, getStatusRead(!isUnread)),
                                                        TalkTrackingConstants.TRACKING_POSITION to position.toString()
                                                )
                                        )
                                )
                        )
                )
        )
    }

    fun eventClickSellerFilter(shopId: String, userId: String, filter: String, filterStatus: Boolean, countUnreadMessages: Long) {
        val eventLabel = String.format(TalkInboxTrackingConstants.EVENT_LABEL_CLICK_SELLER_FILTER, getFilter(filter), getFilterStatus(filterStatus), shopId, countUnreadMessages.toString())
        tracker.sendGeneralEvent(getSellerSettingsTrackingMap(shopId, userId, TalkInboxTrackingConstants.EVENT_ACTION_CLICK_FILTER, getEventCategoryInbox(TalkInboxTab.SHOP_TAB), eventLabel))
    }

    fun eventClickSettings(shopId: String, userId: String) {
        tracker.sendGeneralEvent(getSellerSettingsTrackingMap(shopId, userId, TalkInboxTrackingConstants.EVENT_ACTION_CLICK_SETTINGS, getEventCategoryInbox(TalkInboxTab.SHOP_TAB)))
    }

    private fun getSellerSettingsTrackingMap(shopId: String, userId: String, eventAction: String, eventCategory: String, eventLabel: String = ""): Map<String, String> {
        return mapOf(
                TalkTrackingConstants.TRACKING_EVENT to TalkSellerSettingsTrackingConstants.EVENT_INBOX_TALK,
                TalkTrackingConstants.TRACKING_SHOP_ID to shopId,
                TalkTrackingConstants.TRACKING_USER_ID to userId,
                TalkTrackingConstants.TRACKING_EVENT_ACTION to eventAction,
                TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK_INBOX,
                TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                TalkTrackingConstants.TRACKING_EVENT_CATEGORY to eventCategory,
                TalkTrackingConstants.TRACKING_EVENT_LABEL to eventLabel
        )
    }

    private fun getEventCategoryInbox(tab: String): String {
        return when (tab) {
            TalkInboxTab.SHOP_OLD -> {
                String.format(TalkInboxTrackingConstants.EVENT_CATEGORY_INBOX, TalkInboxTrackingConstants.TAB_SELLER)
            }
            TalkInboxTab.SHOP_TAB -> {
                String.format(TalkInboxTrackingConstants.EVENT_CATEGORY_INBOX, TalkInboxTrackingConstants.TAB_SELLER)
            }
            TalkInboxTab.BUYER_TAB -> {
                String.format(TalkInboxTrackingConstants.EVENT_CATEGORY_INBOX, TalkInboxTrackingConstants.TAB_BUYER)
            }
            else -> {
                ""
            }
        }
    }

    private fun getTab(tab: String): String {
        return when (tab) {
            TalkInboxTab.SHOP_OLD -> {
                String.format(TalkInboxTrackingConstants.EVENT_ACTION_CLICK_TAB, TalkInboxTrackingConstants.TAB_SELLER)
            }
            TalkInboxTab.SHOP_TAB -> {
                String.format(TalkInboxTrackingConstants.EVENT_ACTION_CLICK_TAB, TalkInboxTrackingConstants.TAB_SELLER)
            }
            TalkInboxTab.BUYER_TAB -> {
                String.format(TalkInboxTrackingConstants.EVENT_ACTION_CLICK_TAB, TalkInboxTrackingConstants.TAB_BUYER)
            }
            else -> {
                ""
            }
        }
    }

    private fun getInboxType(inboxType: String): String {
        return when (inboxType) {
            TalkInboxTab.SHOP_OLD -> {
                TalkInboxTrackingConstants.TAB_SELLER
            }
            TalkInboxTab.SHOP_TAB -> {
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

    private fun getFilter(filter: String): String {
        return when (filter) {
            TalkInboxFilter.READ_FILTER -> {
                TalkInboxTrackingConstants.FILTER_READ
            }
            TalkInboxFilter.UNREAD_FILTER -> {
                TalkInboxTrackingConstants.FILTER_UNREAD
            }
            TalkInboxFilter.PROBLEM_FILTER -> {
                TalkInboxTrackingConstants.FILTER_PROBLEM
            }
            TalkInboxFilter.UNRESPONDED_FILTER -> {
                TalkInboxTrackingConstants.FILTER_UNRESPONDED
            }
            TalkInboxFilter.AUTOREPLIED_FILTER -> {
                TalkInboxTrackingConstants.FILTER_AUTOREPLIED
            }
            else -> {
                ""
            }
        }
    }

    private fun getStatusRead(isRead: Boolean): String {
        return if (isRead) TalkInboxTrackingConstants.STATUS_READ else TalkInboxTrackingConstants.STATUS_UNREAD
    }

    private fun getFilterStatus(filterStatus: Boolean): String {
        return if (filterStatus) TalkInboxTrackingConstants.FILTER_STATUS_ACTIVE else TalkInboxTrackingConstants.FILTER_STATUS_INACTIVE
    }
}