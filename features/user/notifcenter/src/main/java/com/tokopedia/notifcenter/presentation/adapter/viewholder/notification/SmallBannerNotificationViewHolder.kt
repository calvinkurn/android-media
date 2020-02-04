package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

class SmallBannerNotificationViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseNotificationItemViewHolder(itemView, listener) {

    private val contentImage: ImageView = itemView.findViewById(R.id.image)

    override fun bindNotificationPayload(element: NotificationItemViewBean) {
        val imageUrl = element.contentUrl
        ImageHandler.loadImage2(contentImage, imageUrl, R.drawable.ic_loading_toped_new)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_small_banner
    }

}