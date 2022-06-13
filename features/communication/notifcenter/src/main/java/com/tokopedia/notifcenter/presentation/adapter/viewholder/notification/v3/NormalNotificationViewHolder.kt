package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener

class NormalNotificationViewHolder constructor(
        itemView: View?,
        private val listener: NotificationItemListener?
) : BaseNotificationViewHolder(itemView, listener) {

    companion object {
        val LAYOUT = R.layout.item_notifcenter_normal_notification
    }
}