package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.media.loader.GlideBuilder.blurring
import com.tokopedia.media.loader.loadImage
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseNotificationItemViewHolder

class BigBannerNotificationViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseNotificationItemViewHolder(itemView, listener) {

    private val contentImageBanner: ImageView = itemView.findViewById(R.id.image_banner)
    private val contentImageBannerBlur: ImageView = itemView.findViewById(R.id.image_banner_blur)

    override fun bindNotificationPayload(element: NotificationItemViewBean) {
        val imageUrl = element.contentUrl

        contentImageBanner.loadImage(imageUrl)
        contentImageBanner.loadImage(blurring(contentImageBannerBlur, "APL4W-D4.AtS"))
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_big_banner
    }

}