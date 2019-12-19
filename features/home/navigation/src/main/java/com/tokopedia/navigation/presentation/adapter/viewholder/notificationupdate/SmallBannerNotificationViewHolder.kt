package com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

class SmallBannerNotificationViewHolder(itemView: View, listener: NotificationUpdateItemListener) : NotificationUpdateItemViewHolder(itemView, listener) {

    private val contentImage: ImageView = itemView.findViewById(R.id.image)

    override fun bindNotificationPayload(element: NotificationUpdateItemViewModel) {
        val imageUrl = element.contentUrl

        ImageHandler.loadImage2(contentImage, imageUrl, R.drawable.ic_loading_toped_new)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_small_banner
    }

}