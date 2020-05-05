package com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.viewbean.*
import com.tokopedia.notifcenter.listener.NotificationFilterListener
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.listener.TransactionMenuListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.*
import com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction.NotificationFilterViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction.TransactionBuyerViewHolder
import com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction.TransactionSellerViewHolder
import com.tokopedia.user.session.UserSessionInterface

class NotificationTransactionFactoryImpl(
        private var notificationUpdateListener: NotificationItemListener,
        private val notificationFilterListener: NotificationFilterListener,
        private val transactionMenuListener: TransactionMenuListener,
        private val userSession: UserSessionInterface
): BaseAdapterTypeFactory(), NotificationTransactionFactory {

    override fun type(buyerNotification: BuyerNotificationViewBean): Int {
        return TransactionBuyerViewHolder.LAYOUT
    }

    override fun type(sellerNotification: SellerNotificationViewBean): Int {
        return TransactionSellerViewHolder.LAYOUT
    }

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
            NotificationItemViewBean.TYPE_PRODUCT_CHECKOUT -> ProductCheckoutViewHolder.LAYOUT
            else -> TextNotificationViewHolder.LAYOUT
        }
    }

    override fun type(filter: NotificationFilterSectionViewBean): Int {
        return NotificationFilterViewHolder.LAYOUT
    }

    override fun type(emptyState: NotificationEmptyStateViewBean): Int {
        return EmptyDataStateViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TransactionBuyerViewHolder.LAYOUT -> TransactionBuyerViewHolder(view, transactionMenuListener)
            TransactionSellerViewHolder.LAYOUT -> TransactionSellerViewHolder(view, transactionMenuListener)
            TextNotificationViewHolder.LAYOUT -> TextNotificationViewHolder(view, notificationUpdateListener)
            SmallBannerNotificationViewHolder.LAYOUT -> SmallBannerNotificationViewHolder(view, notificationUpdateListener)
            BigBannerNotificationViewHolder.LAYOUT -> BigBannerNotificationViewHolder(view, notificationUpdateListener)
            ProductRecomNotificationViewHolder.LAYOUT -> ProductRecomNotificationViewHolder(view, notificationUpdateListener)
            WishListNotificationViewHolder.LAYOUT -> WishListNotificationViewHolder(view, notificationUpdateListener)
            NotificationFilterViewHolder.LAYOUT -> NotificationFilterViewHolder(view, notificationFilterListener, userSession)
            ProductCheckoutViewHolder.LAYOUT -> ProductCheckoutViewHolder(view, notificationUpdateListener)
            EmptyDataStateViewHolder.LAYOUT -> EmptyDataStateViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

}