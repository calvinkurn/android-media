package com.tokopedia.notifcenter.listener

import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.UserInfo
import com.tokopedia.notifcenter.data.state.BottomSheetType
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

interface NotificationItemListener {
    fun onItemStockHandlerClick(notification: NotificationItemViewBean)
    fun itemClicked(notification: NotificationItemViewBean, adapterPosition: Int)
    fun getAnalytic(): NotificationUpdateAnalytics
    fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit)
    fun addProductToCheckout(userInfo: UserInfo, element: NotificationItemViewBean)
    fun showNotificationDetail(bottomSheet: BottomSheetType, element: NotificationItemViewBean)
    fun trackNotificationImpression(element: NotificationItemViewBean)
    fun onSuccessReminderStock()
}