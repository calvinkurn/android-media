package com.tokopedia.talk.feature.inbox.analytics

import com.tokopedia.talk.common.analytics.TalkEventTracking
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class TalkInboxTracking @Inject constructor() {

    private val tracker = TrackApp.getInstance().gtm

    fun eventClickFilter(filter: String, tab: String, counter: Int, filterStatus: Boolean, shopId: String, userId: String) {
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

    fun eventClickTab(tab: String, userId: String, shopId: String, countUnreadMessages: Int) {
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

    fun eventClickThread(tab: String, talkId: String, productId: String, filterActive: String, isRead: Boolean, shopId: String, countUnreadMessages: Int, userId: String) {
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

    fun openScreen(screenName: String) {
        tracker.sendScreenAuthenticated(screenName)
    }

    private fun getEventCategoryInbox(tab: String): String {
        return when(tab) {
            TalkInboxTab.SHOP_TAB -> {
                String.format(TalkInboxTrackingConstants.EVENT_CATEGORY_INBOX, TalkInboxTrackingConstants.TAB_SELLER)
            }
            TalkInboxTab.BUYER_TAB -> {
                String.format(TalkInboxTrackingConstants.EVENT_CATEGORY_INBOX, TalkInboxTrackingConstants.TAB_BUYER)
            } else -> {
                ""
            }
        }
    }

    private fun getTab(tab: String): String {
        return when(tab) {
            TalkInboxTab.SHOP_TAB -> {
                String.format(TalkInboxTrackingConstants.EVENT_ACTION_CLICK_TAB, TalkInboxTrackingConstants.TAB_SELLER)
            }
            TalkInboxTab.BUYER_TAB -> {
                String.format(TalkInboxTrackingConstants.EVENT_ACTION_CLICK_TAB, TalkInboxTrackingConstants.TAB_BUYER)
            } else -> {
                ""
            }
        }
    }

    private fun getFilter(filter: String): String {
        return when(filter) {
            TalkInboxTrackingConstants.STATUS_READ -> {
                TalkInboxTrackingConstants.FILTER_READ
            }
            TalkInboxTrackingConstants.STATUS_UNREAD -> {
                TalkInboxTrackingConstants.FILTER_UNREAD
            }
            else -> {
                ""
            }
        }
    }

    private fun getStatusRead(isRead: Boolean): String {
        return if(isRead) TalkInboxTrackingConstants.STATUS_READ else TalkInboxTrackingConstants.STATUS_UNREAD
    }

    private fun getFilterStatus(filterStatus: Boolean): String  {
        return if(filterStatus) TalkInboxTrackingConstants.FILTER_STATUS_ACTIVE else TalkInboxTrackingConstants.FILTER_STATUS_INACTIVE
    }
}