package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListUiModel
import com.tokopedia.notifcenter.data.entity.orderlist.OrderWidgetUiModel
import com.tokopedia.notifcenter.view.adapter.common.NotificationAdapterListener
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.payload.PayloadOrderList
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.util.OrderWidgetDiffUtil
import com.tokopedia.notifcenter.view.customview.ShadowGenerator
import com.tokopedia.notifcenter.view.customview.widget.ItemOrderListLinearLayout
import com.tokopedia.notifcenter.view.listener.NotificationItemListener

class NotificationOrderListViewHolder constructor(
    itemView: View?,
    private val notificationItemListener: NotificationItemListener?,
    private val adapterListener: NotificationAdapterListener?,
    private val listener: Listener?
) : AbstractViewHolder<NotifOrderListUiModel>(itemView) {

    private val rv: RecyclerView? = itemView?.findViewById(R.id.rv_order_list)
    private val typeFactory = DefaultOrderListTypeFactory(notificationItemListener)
    private val rvAdapter = OrderListAdapter(typeFactory)
    private val rvLm = LinearLayoutManager(
        itemView?.context,
        LinearLayoutManager.HORIZONTAL,
        false
    )

    interface Listener {
        fun saveOrderWidgetState(key: String, currentState: Parcelable?)
        fun getSavedOrderCarouselState(key: String): Parcelable?
    }

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv?.apply {
            setHasFixedSize(true)
            setRecycledViewPool(adapterListener?.getNotificationOrderViewPool())
            itemAnimator = null
            layoutManager = rvLm
            adapter = rvAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val currentState = rvLm.onSaveInstanceState()
                        val key = getStateKey()
                        listener?.saveOrderWidgetState(key, currentState)
                    }
                }
            })
        }
    }

    private fun getStateKey(): String {
        return "${notificationItemListener?.getRole()}-$adapterPosition"
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
        bindScrollState(element)
    }

    private fun updateListData(element: NotifOrderListUiModel) {
        rvAdapter.updateData(element.list)
    }

    private fun bindScrollState(element: NotifOrderListUiModel) {
        val key = getStateKey()
        listener?.getSavedOrderCarouselState(key)?.let { previousState ->
            rvLm.onRestoreInstanceState(previousState)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notification_order_list
    }

    /**
     * other class specific to this viewholder
     */

    interface OrderListTypeFactory : AdapterTypeFactory {
        fun type(orderWidgetUiModel: OrderWidgetUiModel): Int
    }

    class DefaultOrderListTypeFactory(
        private val notificationItemListener: NotificationItemListener?
    ) : BaseAdapterTypeFactory(), OrderListTypeFactory {
        override fun type(orderWidgetUiModel: OrderWidgetUiModel): Int {
            return OrderItemViewHolder.LAYOUT
        }

        override fun createViewHolder(
            parent: View?,
            type: Int
        ): AbstractViewHolder<out Visitable<*>> {
            return when (type) {
                OrderItemViewHolder.LAYOUT -> OrderItemViewHolder(
                    parent,
                    notificationItemListener
                )
                else -> super.createViewHolder(parent, type)
            }
        }
    }

    class OrderListAdapter(
        adapterTypeFactory: OrderListTypeFactory?
    ) : BaseAdapter<OrderListTypeFactory>(adapterTypeFactory) {
        fun updateData(list: List<OrderWidgetUiModel>) {
            val diffUtil = OrderWidgetDiffUtil(visitables, list)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            visitables.clear()
            visitables.addAll(list)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    class OrderItemViewHolder(
        itemView: View?,
        private val notificationItemListener: NotificationItemListener?
    ) : AbstractViewHolder<OrderWidgetUiModel>(itemView) {

        private val card: ItemOrderListLinearLayout? = itemView?.findViewById(R.id.ll_card_uoh)
        private val bg = ShadowGenerator.generateBackgroundWithShadow(
            card,
            com.tokopedia.unifyprinciples.R.color.Unify_Background,
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

        override fun bind(element: OrderWidgetUiModel) {
            bindCardContent(element)
            bindCardBackground(element)
            bindTitleMargin(element)
        }

        private fun bindCardBackground(element: OrderWidgetUiModel) {
            card?.background = bg
        }

        private fun bindCardContent(element: OrderWidgetUiModel) {
            card?.bindItem(element) {
                notificationItemListener?.trackClickOrderListItem(element)
            }
        }

        private fun bindTitleMargin(element: OrderWidgetUiModel) {
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
