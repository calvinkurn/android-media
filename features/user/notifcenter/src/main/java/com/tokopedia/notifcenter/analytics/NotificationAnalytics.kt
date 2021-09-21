package com.tokopedia.notifcenter.analytics

import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

open class NotificationAnalytics {

    fun getImpressionTrackLabel(
            location: String,
            templateKey: String,
            notificationId: String,
            productId: String? = "0"
    ): String {
        return if (productId != "0") {
            "$location - $templateKey - $notificationId - $productId"
        } else {
            "$location - $templateKey - $notificationId"
        }
    }

    fun getImpressionTrackLabel(location: String, notification: NotificationItemViewBean): String {
        return "$location - ${notification.templateKey} - ${notification.notificationId} - ${notification.getAtcProduct()?.productId}"
    }

    fun getImpressionTrackLabel(notificationId: String, productNumber: Int, location: String): String {
        return "$notificationId - $productNumber - $location"
    }

    fun getImpressionWithoutLocationLabel(templateKey: String, notificationId: String): String {
        return "$templateKey - $notificationId"
    }

}