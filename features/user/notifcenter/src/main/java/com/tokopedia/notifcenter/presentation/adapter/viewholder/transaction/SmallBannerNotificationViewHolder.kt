package com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.domain.model.TransactionItemNotification
import com.tokopedia.notifcenter.presentation.view.listener.NotificationTransactionItemListener
import com.tokopedia.notifcenter.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateItemViewModel

class SmallBannerNotificationViewHolder(itemView: View, listener: NotificationTransactionItemListener) : NotificationTransactionItemViewHolder(itemView, listener) {

    private val contentImage: ImageView = itemView.findViewById(R.id.image)

    override fun bindNotificationPayload(element: TransactionItemNotification) {
        val imageUrl = element.contentUrl

        ImageHandler.loadImage2(contentImage, imageUrl, R.drawable.ic_loading_toped_new)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_small_banner
    }

}