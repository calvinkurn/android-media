package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.payload.PayloadOrderList

class NotificationOrderListViewHolder(
        itemView: View?
) : AbstractViewHolder<NotifOrderListUiModel>(itemView) {

    override fun bind(element: NotifOrderListUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (val payload = payloads.first()) {
            is PayloadOrderList -> {
                element.update(payload.orderList)
                bind(element)
            }
        }
    }

    override fun bind(element: NotifOrderListUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_notification_order_list
    }
}