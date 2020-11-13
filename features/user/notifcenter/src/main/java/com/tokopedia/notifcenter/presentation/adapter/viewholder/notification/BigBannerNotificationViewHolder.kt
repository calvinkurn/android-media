package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.media.loader.loadImage
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseNotificationItemViewHolder

class BigBannerNotificationViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseNotificationItemViewHolder(itemView, listener) {

    private val contentImageBanner: ImageView = itemView.findViewById(R.id.image_banner)

    override fun bindNotificationPayload(element: NotificationItemViewBean) {
        contentImageBanner.loadImage(element.contentUrl)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_big_banner
    }

}