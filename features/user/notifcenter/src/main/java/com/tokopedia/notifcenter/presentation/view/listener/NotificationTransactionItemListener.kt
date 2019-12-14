package com.tokopedia.notifcenter.presentation.view.listener

import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.domain.model.TransactionItemNotification
import com.tokopedia.notifcenter.domain.pojo.ProductData

interface NotificationTransactionItemListener {
    fun itemClicked(notification: TransactionItemNotification, adapterPosition: Int)
    fun getAnalytic(): NotificationUpdateAnalytics
    fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit)
    fun showTextLonger(element: TransactionItemNotification)
    fun trackNotificationImpression(element: TransactionItemNotification)
}