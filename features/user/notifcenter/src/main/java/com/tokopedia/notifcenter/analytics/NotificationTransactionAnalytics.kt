package com.tokopedia.notifcenter.analytics

import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class NotificationTransactionAnalytics @Inject constructor() {

    private val seenNotifications = HashSet<String>()

    companion object {
        private const val EVENT = "clickNotifCenter"
        private const val EVENT_IMPRESSION_IRIS = "viewNotifCenterIris"

        private const val EVENT_CATEGORY = "transaction tab"

        private const val EVENT_TRANSACTION_VIEW_ACTION = "view on transaction notif"
        private const val EVENT_TRANSACTION_CLICK_ACTION = "click on transaction notif"
        private const val EVENT_TRANSACTION_SCROLL_ACTION = "scroll on transaction notif"
        private const val EVENT_TRANSACTION_TAB_ACTION = "click on transaction process"
        private const val EVENT_TRANSACTION_FILTER_ACTION = "click on transaction filter"

        private const val LABEL_LOCATION = "lonceng"
    }

    //10A
    fun sendTrackTransactionTab(parent: String, child: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_TAB_ACTION,
                String.format("%s - %s", parent, child)
        ))
    }

    //10B
    fun trackClickFilterRequest(filter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_FILTER_ACTION,
                filter
        ))
    }

    //10C
    fun saveNotificationImpression(notification: NotificationItemViewBean) {
        val notificationId = notification.notificationId
        val isNotAlreadyTracked = seenNotifications.add(notificationId)
        if (isNotAlreadyTracked) {
            trackNotificationImpression(notification)
        }
    }

    //10D
    fun trackNotificationClick(notification: NotificationItemViewBean) {
        val label = notification.getImpressionTrackLabel(LABEL_LOCATION)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_CLICK_ACTION,
                label
        ))
    }

    //10E
    fun trackScrollBottom(notifItemPosition: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_SCROLL_ACTION,
                notifItemPosition
        ))
    }

    private fun trackNotificationImpression(notification: NotificationItemViewBean) {
        val label = notification.getImpressionTrackLabel(LABEL_LOCATION)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_IMPRESSION_IRIS,
                EVENT_CATEGORY,
                EVENT_TRANSACTION_VIEW_ACTION,
                label
        ))
    }

}