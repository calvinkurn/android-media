package com.tokopedia.notifcenter.presentation.adapter.typefactory.update

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.*

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateTypeFactoryImpl(
        var notificationUpdateListener: NotificationItemListener
) : BaseAdapterTypeFactory(), NotificationUpdateTypeFactory {

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

    override fun type(emptyState: NotificationEmptyStateViewBean): Int {
        return EmptyDataStateViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            TextNotificationViewHolder.LAYOUT -> TextNotificationViewHolder(view, notificationUpdateListener)
            SmallBannerNotificationViewHolder.LAYOUT -> SmallBannerNotificationViewHolder(view, notificationUpdateListener)
            BigBannerNotificationViewHolder.LAYOUT -> BigBannerNotificationViewHolder(view, notificationUpdateListener)
            ProductRecomNotificationViewHolder.LAYOUT -> ProductRecomNotificationViewHolder(view, notificationUpdateListener)
            WishListNotificationViewHolder.LAYOUT -> WishListNotificationViewHolder(view, notificationUpdateListener)
            EmptyDataStateViewHolder.LAYOUT -> EmptyDataStateViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }
}