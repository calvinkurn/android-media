package com.tokopedia.navigation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate.BigBannerNotificationViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate.ProductRecomNotificationViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate.SmallBannerNotificationViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate.TextNotificationViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate.WishListNotificationViewHolder
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateTypeFactoryImpl(var notificationUpdateListener: NotificationUpdateItemListener) : BaseAdapterTypeFactory(), NotificationUpdateTypeFactory {

    override fun type(notificationUpdateDefaultViewModel: NotificationUpdateItemViewModel): Int {
        return when (notificationUpdateDefaultViewModel.typeLink) {
            NotificationUpdateItemViewModel.TYPE_BANNER_1X1 -> {
                val imageUrl = notificationUpdateDefaultViewModel.contentUrl
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

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TextNotificationViewHolder.LAYOUT -> TextNotificationViewHolder(parent, notificationUpdateListener)
            SmallBannerNotificationViewHolder.LAYOUT -> SmallBannerNotificationViewHolder(parent, notificationUpdateListener)
            BigBannerNotificationViewHolder.LAYOUT -> BigBannerNotificationViewHolder(parent, notificationUpdateListener)
            ProductRecomNotificationViewHolder.LAYOUT -> ProductRecomNotificationViewHolder(parent, notificationUpdateListener)
            WishListNotificationViewHolder.LAYOUT -> WishListNotificationViewHolder(parent, notificationUpdateListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}