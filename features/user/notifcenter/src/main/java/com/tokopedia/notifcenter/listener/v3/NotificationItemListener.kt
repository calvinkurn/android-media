package com.tokopedia.notifcenter.listener.v3

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
    fun addToWishlist(product: ProductData)
    fun goToWishlist()
    fun trackProductImpression(
            notification: NotificationUiModel, product: ProductData, position: Int
    )
    fun trackProductClick(
            notification: NotificationUiModel, product: ProductData, position: Int
    )
    fun markAsSeen(notifId: String)
    fun refreshPage()
    fun trackClickCtaWidget(element: NotificationUiModel)
    fun trackExpandTimelineHistory(element: NotificationUiModel)
    fun amISeller(): Boolean
    fun trackClickOrderListItem(order: OrderWidgetUiModel)
    fun getNotifAnalytic(): NotificationAnalytic
    fun getRole(): Int
}