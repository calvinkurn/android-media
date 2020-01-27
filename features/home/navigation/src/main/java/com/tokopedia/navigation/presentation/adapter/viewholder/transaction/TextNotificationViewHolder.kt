package com.tokopedia.navigation.presentation.adapter.viewholder.transaction

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.TransactionItemNotification
import com.tokopedia.navigation.presentation.view.listener.NotificationTransactionItemListener
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

class TextNotificationViewHolder(itemView: View, listener: NotificationTransactionItemListener) : NotificationTransactionItemViewHolder(itemView, listener) {

    override fun bindNotificationPayload(element: TransactionItemNotification) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_text
    }

}