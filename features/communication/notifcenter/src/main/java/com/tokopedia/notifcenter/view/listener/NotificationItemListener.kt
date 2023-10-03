package com.tokopedia.notifcenter.view.listener

import com.tokopedia.notifcenter.analytics.NotificationAnalytic
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.entity.orderlist.OrderWidgetUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel

/**
 * used to interact between [androidx.recyclerview.widget.RecyclerView.ViewHolder] and activity
 */
interface NotificationItemListener {
    fun showLongerContent(element: NotificationUiModel)
    fun buyProduct(notification: NotificationUiModel, product: ProductData)
    fun addProductToCart(notification: NotificationUiModel, product: ProductData)
    fun markNotificationAsRead(element: NotificationUiModel)
    fun bumpReminder(
            product: ProductData, notification: NotificationUiModel, adapterPosition: Int
    )
    fun deleteReminder(
            product: ProductData, notification: NotificationUiModel, adapterPosition: Int
    )
    fun addToWishlist(notification: NotificationUiModel, product: ProductData, position: Int)
    fun goToWishlist()
    fun trackProductImpression(
            notification: NotificationUiModel, product: ProductData, position: Int
    )
    fun onProductClicked(
            notification: NotificationUiModel, product: ProductData, position: Int
    )
    fun trackBumpReminder()
    fun trackDeleteReminder()
    fun markAsSeen(notifId: String)
    fun refreshPage()
    fun trackClickCtaWidget(element: NotificationUiModel)
    fun trackExpandTimelineHistory(element: NotificationUiModel)
    fun amISeller(): Boolean
    fun trackClickOrderListItem(order: OrderWidgetUiModel)
    fun getNotifAnalytic(): NotificationAnalytic
    fun getRole(): Int
}
