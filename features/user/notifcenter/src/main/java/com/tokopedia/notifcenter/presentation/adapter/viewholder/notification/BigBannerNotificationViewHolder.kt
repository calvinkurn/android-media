package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseNotificationItemViewHolder

class BigBannerNotificationViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseNotificationItemViewHolder(itemView, listener) {

    private val contentImageBanner: ImageView = itemView.findViewById(R.id.image_banner)
    private val contentImageBanner2: ImageView = itemView.findViewById(R.id.image_banner_2)

    override fun bindNotificationPayload(element: NotificationItemViewBean) {
        val imageUrl = element.contentUrl

        contentImageBanner.shouldShowWithAction(imageUrl.isNotEmpty()) {
            contentImageBanner.loadImage(imageUrl) {
                placeHolder = R.drawable.ic_notifcenter_loading_toped
            }
        }

        contentImageBanner.shouldShowWithAction(imageUrl.isNotEmpty()) {
            contentImageBanner.loadImageRounded(imageUrl, 10f)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_big_banner
    }

}