package com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.domain.model.TransactionItemNotification
import com.tokopedia.notifcenter.presentation.view.listener.NotificationTransactionItemListener
import com.tokopedia.notifcenter.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateItemViewModel

class TextNotificationViewHolder(itemView: View, listener: NotificationTransactionItemListener) : NotificationTransactionItemViewHolder(itemView, listener) {

    override fun bindNotificationPayload(element: TransactionItemNotification) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_text
    }

}