package com.tokopedia.notifcenter.analytics

import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

open class NotificationAnalytics {

    fun getImpressionTrackLabel(
            location: String,
            templateKey: String,
            notificationId: String,
            productId: String?
    ): String {
        return "$location - $templateKey - $notificationId - $productId"
    }

    fun getImpressionTrackLabel(location: String, notification: NotificationItemViewBean): String {
        return "$location - ${notification.templateKey} - ${notification.notificationId} - ${notification.getAtcProduct()?.productId}"
    }

    fun getImpressionTrackLabel(notificationId: String, productNumber: String, location: String): String {
        return "$notificationId - $productNumber - $location"
    }

    fun getImpressionWithoutLocationLabel(templateKey: String, notificationId: String, productId: String?): String {
        return "$templateKey - $notificationId - $productId"
    }

}