package com.tokopedia.notifcenter.analytics

import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class NotificationTransactionAnalytics: NotificationAnalytics(), NotificationTracker {

    private val seenNotifications = HashSet<String>()

    companion object {
        private const val EVENT = "clickNotifCenter"
        private const val EVENT_CATEGORY_NOTIF_CENTER = "notif center"
        private const val EVENT_IMPRESSION_IRIS = "viewNotifCenterIris"

        private const val EVENT_CATEGORY = "transaction tab"

        private const val EVENT_TRANSACTION_VIEW_ACTION = "view on transaction notif"
        private const val EVENT_TRANSACTION_CLICK_ACTION = "click on transaction notif"
        private const val EVENT_TRANSACTION_SCROLL_ACTION = "scroll on transaction notif"
        private const val EVENT_TRANSACTION_TAB_ACTION = "click on transaction process"
        private const val EVENT_TRANSACTION_FILTER_ACTION = "click on transaction filter"
        private const val EVENT_ACTION_MARK_ALL_AS_READ = "mark all as read"

        private const val LABEL_LOCATION = "lonceng"
    }

    // #NC5
    override fun trackMarkAllAsRead(markAllReadCounter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_MARK_ALL_AS_READ,
                markAllReadCounter
        ))
    }

    //10A
    override fun sendTrackTransactionTab(parent: String, child: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_TAB_ACTION,
                String.format("%s - %s", parent, child)
        ))
    }

    //10B
    override fun trackClickFilterRequest(filter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_FILTER_ACTION,
                filter
        ))
    }

    //10C
    override fun saveNotificationImpression(notification: NotificationItemViewBean) {
        val notificationId = notification.notificationId
        val isNotAlreadyTracked = seenNotifications.add(notificationId)
        if (isNotAlreadyTracked) {
            trackNotificationImpression(notification)
        }
    }

    //10D
    override fun trackNotificationClick(notification: NotificationItemViewBean) {
        val label = getImpressionTrackLabel(LABEL_LOCATION, notification)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_CLICK_ACTION,
                label
        ))
    }

    //10E
    override fun trackScrollBottom(notificationSize: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_SCROLL_ACTION,
                notificationSize
        ))
    }

    private fun trackNotificationImpression(notification: NotificationItemViewBean) {
        val label = getImpressionTrackLabel(LABEL_LOCATION, notification)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_IMPRESSION_IRIS,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_VIEW_ACTION,
                label
        ))
    }

}