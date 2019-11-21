package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.domain.model.PurchaseNotification
import com.tokopedia.navigation.domain.model.SaleNotification
import com.tokopedia.navigation.domain.model.TransactionItemNotification
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.BigBannerNotificationViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.ProductRecomNotificationViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.PurchaseViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.SaleViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.SmallBannerNotificationViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.TextNotificationViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.WishListNotificationViewHolder
import com.tokopedia.navigation.presentation.view.listener.NotificationTransactionItemListener
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

class NotificationTransactionFactoryImpl(
        var notificationUpdateListener: NotificationTransactionItemListener
): BaseAdapterTypeFactory(), NotificationTransactionFactory {

    override fun type(purchaseNotification: PurchaseNotification): Int = PurchaseViewHolder.LAYOUT

    override fun type(saleNotification: SaleNotification): Int = SaleViewHolder.LAYOUT

    override fun type(notification: TransactionItemNotification): Int {
        return when (notification.typeLink) {
            NotificationUpdateItemViewModel.TYPE_BANNER_1X1 -> {
                val imageUrl = notification.contentUrl
                if (imageUrl.isEmpty()) {
                    TextNotificationViewHolder.LAYOUT
                } else {
                    SmallBannerNotificationViewHolder.LAYOUT
                }
            }
            NotificationUpdateItemViewModel.TYPE_BANNER_2X1 -> BigBannerNotificationViewHolder.LAYOUT
            NotificationUpdateItemViewModel.TYPE_RECOMMENDATION -> ProductRecomNotificationViewHolder.LAYOUT
            NotificationUpdateItemViewModel.TYPE_WISHLIST -> WishListNotificationViewHolder.LAYOUT
            else -> TextNotificationViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PurchaseViewHolder.LAYOUT -> PurchaseViewHolder(parent)
            SaleViewHolder.LAYOUT -> SaleViewHolder(parent)
            TextNotificationViewHolder.LAYOUT -> TextNotificationViewHolder(parent, notificationUpdateListener)
            SmallBannerNotificationViewHolder.LAYOUT -> SmallBannerNotificationViewHolder(parent, notificationUpdateListener)
            BigBannerNotificationViewHolder.LAYOUT -> BigBannerNotificationViewHolder(parent, notificationUpdateListener)
            ProductRecomNotificationViewHolder.LAYOUT -> ProductRecomNotificationViewHolder(parent, notificationUpdateListener)
            WishListNotificationViewHolder.LAYOUT -> WishListNotificationViewHolder(parent, notificationUpdateListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}