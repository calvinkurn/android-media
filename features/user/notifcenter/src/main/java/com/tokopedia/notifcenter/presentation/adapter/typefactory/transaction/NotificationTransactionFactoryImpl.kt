package com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.viewbean.NotificationFilterSectionViewBean
import com.tokopedia.notifcenter.data.viewbean.PurchaseNotificationViewBean
import com.tokopedia.notifcenter.data.viewbean.SaleNotificationViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.listener.TransactionMenuListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.*
import com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction.NotificationFilterViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction.PurchaseViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction.SaleViewHolder
import com.tokopedia.user.session.UserSessionInterface

class NotificationTransactionFactoryImpl(
        private var notificationUpdateListener: NotificationItemListener,
        private val notificationFilterListener: NotificationFilterViewHolder.NotifFilterListener,
        private val transactionMenuListener: TransactionMenuListener,
        private val userSession: UserSessionInterface
): BaseAdapterTypeFactory(), NotificationTransactionFactory {

    override fun type(purchaseNotification: PurchaseNotificationViewBean): Int = PurchaseViewHolder.LAYOUT

    override fun type(saleNotification: SaleNotificationViewBean): Int = SaleViewHolder.LAYOUT

    override fun type(viewItem: NotificationItemViewBean): Int {
        return when (viewItem.typeLink) {
            NotificationItemViewBean.TYPE_BANNER_1X1 -> {
                val imageUrl = viewItem.contentUrl
                if (imageUrl.isEmpty()) {
                    TextNotificationViewHolder.LAYOUT
                } else {
                    SmallBannerNotificationViewHolder.LAYOUT
                }
            }
            NotificationItemViewBean.TYPE_BANNER_2X1 -> BigBannerNotificationViewHolder.LAYOUT
            NotificationItemViewBean.TYPE_RECOMMENDATION -> ProductRecomNotificationViewHolder.LAYOUT
            NotificationItemViewBean.TYPE_WISHLIST -> WishListNotificationViewHolder.LAYOUT
            else -> TextNotificationViewHolder.LAYOUT
        }
    }

    override fun type(filter: NotificationFilterSectionViewBean): Int {
        return NotificationFilterViewHolder.LAYOUT
    }

    override fun type(emptyState: NotificationEmptyStateViewBean): Int {
        return EmptyDataStateViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PurchaseViewHolder.LAYOUT -> PurchaseViewHolder(parent, transactionMenuListener)
            SaleViewHolder.LAYOUT -> SaleViewHolder(parent, transactionMenuListener)
            TextNotificationViewHolder.LAYOUT -> TextNotificationViewHolder(parent, notificationUpdateListener)
            SmallBannerNotificationViewHolder.LAYOUT -> SmallBannerNotificationViewHolder(parent, notificationUpdateListener)
            BigBannerNotificationViewHolder.LAYOUT -> BigBannerNotificationViewHolder(parent, notificationUpdateListener)
            ProductRecomNotificationViewHolder.LAYOUT -> ProductRecomNotificationViewHolder(parent, notificationUpdateListener)
            WishListNotificationViewHolder.LAYOUT -> WishListNotificationViewHolder(parent, notificationUpdateListener)
            NotificationFilterViewHolder.LAYOUT -> NotificationFilterViewHolder(parent, notificationFilterListener, userSession)
            EmptyDataStateViewHolder.LAYOUT -> EmptyDataStateViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}