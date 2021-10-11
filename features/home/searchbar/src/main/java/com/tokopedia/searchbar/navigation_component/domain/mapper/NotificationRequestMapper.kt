package com.tokopedia.searchbar.navigation_component.domain.mapper

import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.searchbar.navigation_component.data.notification.NotificationResponse
import com.tokopedia.searchbar.navigation_component.data.notification.Notifications
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel

object NotificationRequestMapper {
    const val NOTIF_99 = "99+"
    const val NOTIF_99_NUMBER = 99
    const val DEFAULT_SHOP_ID_NOT_OPEN = "-1"
    const val DEFAULT_TOTAL_ORDER_COUNT = 0

    fun mapToNotificationModel(notificationResponse: NotificationResponse): TopNavNotificationModel {
        val hasShop = notificationResponse.userShopInfo.info.shopId != DEFAULT_SHOP_ID_NOT_OPEN

        val notification = notificationResponse.notifications
        val totalNewInbox = notification.inboxCounter.all.totalInt
        val totalCart = notification.totalCart
        val totalInbox = buildTotalInbox(notification)
        val totalNotif = notificationResponse.notifications.inboxCounter.all.notifcenterInt

        val totalSellerOrderCount = buildTotalOrderCount(hasShop, notificationResponse)
        val totalReviewCount = notification.inbox.review
        val totalInboxTicket = notification.inbox.ticket
        val totalUnreadComplain = notification.resolutionAs.buyer
        val totalGlobalNavNotif = totalSellerOrderCount + totalReviewCount + totalInboxTicket + totalUnreadComplain
        val totalGlobalNavBadge = if (totalGlobalNavNotif > 0) NavConstant.ICON_COUNTER_NONE_TYPE else 0

        return TopNavNotificationModel(
                totalNewInbox = totalNewInbox,
                totalInbox = totalInbox,
                totalCart = totalCart,
                totalNotif = totalNotif,
                totalGlobalNavNotif = totalGlobalNavBadge
        )
    }

    private fun buildTotalOrderCount(hasShop: Boolean, data: NotificationResponse): Int {
        return if (hasShop) {
            data.notifications.sellerOrderStatus.newOrder
                    .plus(data.notifications.sellerOrderStatus.readyToShip)
                    .plus(data.notifications.sellerOrderStatus.inResolution)
        } else DEFAULT_TOTAL_ORDER_COUNT
    }

    private fun buildTotalInbox(notification: Notifications): Int {
        return notification.chat.unreads +
                notification.inbox.talk +
                notification.inbox.review +
                notification.inbox.ticket
    }
}
