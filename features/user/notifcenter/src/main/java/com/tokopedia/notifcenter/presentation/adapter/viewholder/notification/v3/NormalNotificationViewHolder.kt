package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel

class NormalNotificationViewHolder(
        itemView: View?
) : AbstractViewHolder<NotificationUiModel>(itemView) {

    override fun bind(element: NotificationUiModel?) {

    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_normal_notification
    }
}