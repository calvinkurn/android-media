package com.tokopedia.navigation.presentation.adapter.viewholder.transaction

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.TransactionItemNotification
import com.tokopedia.navigation.presentation.view.listener.NotificationTransactionItemListener
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

class BigBannerNotificationViewHolder(itemView: View, listener: NotificationTransactionItemListener) : NotificationTransactionItemViewHolder(itemView, listener) {

    private val contentImageBanner: ImageView = itemView.findViewById(R.id.image_banner)

    override fun bindNotificationPayload(element: TransactionItemNotification) {
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