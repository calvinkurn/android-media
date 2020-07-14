package com.tokopedia.notifcenter.analytics

import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

interface NotificationTracker {
    fun saveNotificationImpression(notification: NotificationItemViewBean)
    fun trackNotificationClick(notification: NotificationItemViewBean)
    fun trackMarkAllAsRead(markAllReadCounter: String)
    fun trackScrollBottom(notificationSize: String)
    fun trackClickFilterRequest(filter: String)

    //transaction
    fun sendTrackTransactionTab(parent: String, child: String)
}