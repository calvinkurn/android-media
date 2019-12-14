package com.tokopedia.notifcenter.presentation.adapter.viewholder.notificationupdate

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateItemViewModel

class BigBannerNotificationViewHolder(itemView: View, listener: NotificationUpdateItemListener) : NotificationUpdateItemViewHolder(itemView, listener) {

    private val contentImageBanner: ImageView = itemView.findViewById(R.id.image_banner)

    override fun bindNotificationPayload(element: NotificationUpdateItemViewModel) {
        val imageUrl = element.contentUrl

        contentImageBanner.shouldShowWithAction(imageUrl.isNotEmpty()) {
            ImageHandler.loadImage2(contentImageBanner, imageUrl, R.drawable.ic_loading_toped_new)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_big_banner
    }

}