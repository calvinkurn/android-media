package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.Utils
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_status_info_header.view.*

class OrderStatusHeaderViewHolder(itemView: View?) : AbstractViewHolder<OrderStatusUiModel.OrderStatusHeaderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_status_info_header
    }

    init {
        setupSeeOrderStatusDetail()
    }

    private var orderId: String = ""

    override fun bind(element: OrderStatusUiModel.OrderStatusHeaderUiModel?) {
        element?.let {
            orderId = it.orderId
            setupIndicatorColor(it.indicatorColor)
            setupStatusHeader(it.orderStatus)
        }
    }

    override fun bind(element: OrderStatusUiModel.OrderStatusHeaderUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is OrderStatusUiModel.OrderStatusHeaderUiModel && newItem is OrderStatusUiModel.OrderStatusHeaderUiModel) {
                    itemView.container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    this.orderId = newItem.orderId
                    if (oldItem.indicatorColor != newItem.indicatorColor) {
                        setupIndicatorColor(newItem.indicatorColor)
                    }
                    if (oldItem.orderStatus != newItem.orderStatus) {
                        setupStatusHeader(newItem.orderStatus)
                    }
                    itemView.container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupSeeOrderStatusDetail() {
        itemView.tvBuyerOrderDetailSeeDetail?.apply {
            setOnClickListener {
                BuyerOrderDetailNavigator.goToTrackOrderPage(context, orderId)
            }
        }
    }

    private fun setupIndicatorColor(indicatorColor: String) {
        itemView.buyerOrderDetailIndicator?.background = Utils.getColoredIndicator(itemView.context, indicatorColor)
    }

    private fun setupStatusHeader(orderStatus: String) {
        itemView.tvBuyerOrderDetailStatusOrder?.text = orderStatus
    }
}