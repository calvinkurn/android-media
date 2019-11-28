package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.navigation.domain.model.PurchaseNotification
import com.tokopedia.navigation.domain.model.SaleNotification
import com.tokopedia.navigation.domain.model.TransactionItemNotification
import com.tokopedia.navigation.listener.TransactionMenuListener
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.*
import com.tokopedia.navigation.presentation.view.listener.NotificationTransactionItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.user.session.UserSessionInterface

class NotificationTransactionFactoryImpl(
        private var notificationUpdateListener: NotificationTransactionItemListener,
        private val notificationFilterListener: NotificationFilterViewHolder.NotifFilterListener,
        private val transactionMenuListener: TransactionMenuListener,
        private val userSession: UserSessionInterface
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

    override fun type(filter: NotificationFilterSectionWrapper): Int {
        return NotificationFilterViewHolder.LAYOUT
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
            else -> super.createViewHolder(parent, type)
        }
    }

}