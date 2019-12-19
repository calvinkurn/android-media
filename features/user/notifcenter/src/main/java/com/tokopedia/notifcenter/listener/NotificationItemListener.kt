package com.tokopedia.notifcenter.listener

import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.domain.pojo.ProductData
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationItemViewBean

interface NotificationItemListener {
    fun itemClicked(notification: NotificationItemViewBean, adapterPosition: Int)
    fun getAnalytic(): NotificationUpdateAnalytics
    fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit)
    fun showTextLonger(element: NotificationItemViewBean)
    fun trackNotificationImpression(element: NotificationItemViewBean)
}