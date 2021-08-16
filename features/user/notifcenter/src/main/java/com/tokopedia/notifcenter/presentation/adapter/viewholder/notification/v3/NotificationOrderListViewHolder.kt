package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.orderlist.Card
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.payload.PayloadOrderList
import com.tokopedia.notifcenter.util.view.ShadowGenerator
import com.tokopedia.notifcenter.widget.ItemOrderListLinearLayout

// TODO: Retain state when user scroll back
// TODO: Compare cache and network response, update necessary item only
// TODO: Fix this instrumentation test
class NotificationOrderListViewHolder constructor(
    itemView: View?,
    notificationItemListener: NotificationItemListener?,
    private val adapterListener: NotificationAdapterListener?
) : AbstractViewHolder<NotifOrderListUiModel>(itemView) {

    private val rv: RecyclerView? = itemView?.findViewById(R.id.rv_order_list)
    private val typeFactory = DefaultOrderListTypeFactory(notificationItemListener)
    private val rvAdapter = OrderListAdapter(typeFactory)

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv?.apply {
            setHasFixedSize(true)
            setRecycledViewPool(adapterListener?.getNotificationOrderViewPool())
            itemAnimator = null
            layoutManager = LinearLayoutManager(
                itemView.context, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = rvAdapter
        }
    }

    override fun bind(element: NotifOrderListUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            is PayloadOrderList -> {
                bind(element)
            }
        }
    }

    override fun bind(element: NotifOrderListUiModel) {
        updateListData(element)
    }

    private fun updateListData(element: NotifOrderListUiModel) {
        rvAdapter.updateData(element.list)
    }

    companion object {
        val LAYOUT = R.layout.item_notification_order_list
    }

    /**
     * other class specific to this viewholder
     */

    interface OrderListTypeFactory : AdapterTypeFactory {
        fun type(card: Card): Int
    }

    class DefaultOrderListTypeFactory(
        private val notificationItemListener: NotificationItemListener?
    ) : BaseAdapterTypeFactory(), OrderListTypeFactory {
        override fun type(card: Card): Int {
            return OrderItemViewHolder.LAYOUT
        }

        override fun createViewHolder(
            parent: View?,
            type: Int
        ): AbstractViewHolder<out Visitable<*>> {
            return when (type) {
                OrderItemViewHolder.LAYOUT -> OrderItemViewHolder(
                    parent, notificationItemListener
                )
                else -> super.createViewHolder(parent, type)
            }
        }
    }

    class OrderListAdapter(
        adapterTypeFactory: OrderListTypeFactory?
    ) : BaseAdapter<OrderListTypeFactory>(adapterTypeFactory) {
        fun updateData(list: List<Card>) {
            visitables.clear()
            visitables.addAll(list)
            notifyDataSetChanged()
        }
    }

    class OrderItemViewHolder(
        itemView: View?,
        private val notificationItemListener: NotificationItemListener?
    ) : AbstractViewHolder<Card>(itemView) {

        private val card: ItemOrderListLinearLayout? = itemView?.findViewById(R.id.ll_card_uoh)
        private val bg = ShadowGenerator.generateBackgroundWithShadow(
            card,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8,
            R.color.notifcenter_dms_order_list_card_shadow,
            R.dimen.notif_dp_6,
            R.dimen.notif_dp_6
        )
        private val titleWithIconMargin = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_4
        )
        private val titleWithoutIconMargin = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.unify_space_8
        )

        override fun bind(element: Card) {
            bindCardContent(element)
            bindCardBackground(element)
            bindTitleMargin(element)
        }

        private fun bindCardBackground(element: Card) {
            card?.background = bg
        }

        private fun bindCardContent(element: Card) {
            card?.bindItem(element) {
                notificationItemListener?.trackClickOrderListItem(element)
            }
        }

        private fun bindTitleMargin(element: Card) {
            if (card?.hasVisibleIcon() == true) {
                card.setTitleMarginStart(titleWithIconMargin)
            } else {
                card?.setTitleMarginStart(titleWithoutIconMargin)
            }
        }

        companion object {
            val LAYOUT = R.layout.item_order_list_uoh
        }
    }
}