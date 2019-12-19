package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationItemViewBean

class TextNotificationViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseNotificationItemViewHolder(itemView, listener) {

    override fun bindNotificationPayload(element: NotificationItemViewBean) {}

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_update_text
    }

}