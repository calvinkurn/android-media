package com.tokopedia.notifcenter.listener.v3

import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel

/**
 * used to interact between [androidx.recyclerview.widget.RecyclerView.ViewHolder] and activity
 */
interface NotificationItemListener {
    fun showLongerContent(element: NotificationUiModel)
    fun showProductBottomSheet(element: NotificationUiModel)
    fun buyProduct(product: ProductData)
    fun addProductToCart(product: ProductData)
    fun markNotificationAsRead(element: NotificationUiModel)
    fun bumpReminder(
            product: ProductData, notification: NotificationUiModel, adapterPosition: Int
    )

    fun deleteReminder(
            product: ProductData, notification: NotificationUiModel, adapterPosition: Int
    )

    fun trackProductImpression(notification: NotificationUiModel, product: ProductData, position: Int)
}