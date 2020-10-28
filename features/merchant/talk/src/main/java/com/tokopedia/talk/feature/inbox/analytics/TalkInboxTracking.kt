package com.tokopedia.talk.feature.inbox.analytics

import android.os.Bundle
import com.tokopedia.talk.common.analytics.TalkEventTracking
import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
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
        tracker.sendScreenAuthenticated(screenName, mapOf(
                TalkTrackingConstants.TRACKING_CURRENT_SITE to TalkTrackingConstants.CURRENT_SITE_TALK,
                TalkTrackingConstants.TRACKING_BUSINESS_UNIT to TalkTrackingConstants.BUSINESS_UNIT_TALK
        ))
    }

    fun eventItemImpress(inboxType: String, talkId: String, userId: String, position: Int, trackingQueue: TrackingQueue) {
        val eventCategory = String.format(TalkTrackingConstants.EVENT_CATEGORY_INBOX_PRODUCT, inboxType)
        tracker.sendEnhanceEcommerceEvent(TalkInboxTrackingConstants.EVENT_PROMO_VIEW, Bundle().apply {
            putString(TalkTrackingConstants.TRACKING_EVENT_ACTION, TalkInboxTrackingConstants.EVENT_ACTION_IMPRESS_ITEM)
            putString(TalkTrackingConstants.TRACKING_EVENT_CATEGORY, eventCategory)
            putString(TalkTrackingConstants.TRACKING_USER_ID, userId)
            putString(TalkTrackingConstants.TRACKING_SCREEN_NAME, TalkInboxTrackingConstants.SCREEN_NAME)
            putString(TalkTrackingConstants.TRACKING_CURRENT_SITE, TalkTrackingConstants.CURRENT_SITE_TALK)
            putString(TalkTrackingConstants.TRACKING_BUSINESS_UNIT, TalkTrackingConstants.BUSINESS_UNIT_TALK)
            putString(TalkTrackingConstants.TRACKING_ECOMMERCE, mapOf(
                    TalkInboxTrackingConstants.EVENT_PROMO_VIEW to mapOf(
                            TalkTrackingConstants.TRACKING_PROMOTIONS to listOf(
                                    mapOf(
                                            TalkTrackingConstants.TRACKING_ID to talkId,
                                            TalkTrackingConstants.TRACKING_NAME to eventCategory,
                                            TalkTrackingConstants.TRACKING_CREATIVE to talkId,
                                            TalkTrackingConstants.TRACKING_POSITION to position.toString()
                                    )
                            )
                    )).toString()
            )
        })
        """

 'eventCategory' : 'inbox talk - {seller/user}
// active tab name only, do not add number of unread messages',
 'eventAction' : 'view - talk on inbox talk',
 'eventLabel' : '',
 'ecommerce': {
        'promoView': {
        'promotions': [{
            'creative': 'message status:{{read/unread}}',           // name of asset for banner, mandatory
                    }]"
        """.trimIndent()
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