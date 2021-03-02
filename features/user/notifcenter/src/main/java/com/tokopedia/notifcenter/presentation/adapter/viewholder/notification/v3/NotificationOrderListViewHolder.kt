package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.Gravity
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.payload.PayloadOrderList
import com.tokopedia.notifcenter.util.view.ShadowGenerator
import com.tokopedia.notifcenter.widget.ItemOrderListLinearLayout

class NotificationOrderListViewHolder(
        itemView: View?
) : AbstractViewHolder<NotifOrderListUiModel>(itemView) {

    private val firstCard: ItemOrderListLinearLayout? = itemView?.findViewById(R.id.ll_first_card)
    private val secondCard: ItemOrderListLinearLayout? = itemView?.findViewById(R.id.ll_second_card)
    private val bg = ShadowGenerator.generateBackgroundWithShadow(
            firstCard,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.notif_dp_6,
            R.dimen.notif_dp_6
    )

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
        bindFirstCard(element)
        bindFirstCardBackground()
    }

    private fun bindFirstCard(element: NotifOrderListUiModel) {
        val firstItem = element.list.getOrNull(0)
        if (firstItem == null) {
            firstCard?.hide()
        } else {
            firstCard?.show()
            firstCard?.bindItem(firstItem)
        }
    }

    private fun bindFirstCardBackground() {
        if (firstCard?.isVisible == true) {
            firstCard.background = bg
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notification_order_list
    }
}