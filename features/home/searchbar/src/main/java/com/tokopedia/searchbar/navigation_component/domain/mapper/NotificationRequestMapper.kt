package com.tokopedia.searchbar.navigation_component.domain.mapper

import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.searchbar.navigation_component.data.notification.NotifcenterUnread
import com.tokopedia.searchbar.navigation_component.data.notification.NotificationResponse
import com.tokopedia.searchbar.navigation_component.data.notification.Notifications
import com.tokopedia.searchbar.navigation_component.datamodel.TopNavNotificationModel
import com.tokopedia.searchbar.navigation_component.util.IntegerUtil

object NotificationRequestMapper {
    const val NOTIF_99 = "99+"
    const val NOTIF_99_NUMBER = 99

    fun mapToNotificationModel(notificationResponse: NotificationResponse): TopNavNotificationModel {
        val notification = notificationResponse.notifications
        val totalNewInbox = notification.inboxCounter.all.totalInt
        val totalCart = notification.totalCart
        val totalInbox = buildTotalInbox(notification)
        val totalNotif = buildTotalNotif(notificationResponse)

        val totalSellerOrderCount = buildTotalOrderCount(notificationResponse)
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

    private fun buildTotalOrderCount(data: NotificationResponse): Int {
        return data.notifications.sellerOrderStatus.newOrder
                .plus(data.notifications.sellerOrderStatus.readyToShip)
                .plus(data.notifications.sellerOrderStatus.inResolution)
    }

    private fun buildTotalNotif(data: NotificationResponse): Int {
        val notification = data.notifications
        return notification.sellerInfo.notification +
                notification.buyerOrderStatus.paymentStatus +
                notification.buyerOrderStatus.confirmed +
                notification.buyerOrderStatus.processed +
                notification.buyerOrderStatus.shipped +
                notification.buyerOrderStatus.arriveAtDestination +
                notification.sellerOrderStatus.newOrder +
                notification.sellerOrderStatus.shipped +
                notification.sellerOrderStatus.readyToShip +
                notification.sellerOrderStatus.arriveAtDestination +
                notification.resolutionAs.buyer +
                notification.resolutionAs.seller +
                buildUnreadNotificationCount(data.notifcenterUnread)
    }

    private fun buildUnreadNotificationCount(notifcenterUnread: NotifcenterUnread): Int {
        return if (notifcenterUnread.notifUnread == NOTIF_99)
            NOTIF_99_NUMBER
        else IntegerUtil.tryParseInt(notifcenterUnread.notifUnread)
    }

    private fun buildTotalInbox(notification: Notifications): Int {
        return notification.chat.unreads +
                notification.inbox.talk +
                notification.inbox.review +
                notification.inbox.ticket
    }
}
