package com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

class TextNotificationViewHolder(itemView: View, listener: NotificationUpdateItemListener) : NotificationUpdateItemViewHolder(itemView, listener) {

    override fun bindNotificationPayload(element: NotificationUpdateItemViewModel) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_text
    }

}