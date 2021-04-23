package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.Utils
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_status_info_header.view.*

class OrderStatusHeaderViewHolder(itemView: View?) : AbstractViewHolder<OrderStatusUiModel.OrderStatusHeaderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_status_info_header
    }

    override fun bind(element: OrderStatusUiModel.OrderStatusHeaderUiModel?) {
        element?.let {
            setupIndicatorColor(it.indicatorColor)
            setupStatusHeader(it.orderStatus)
        }
    }

    private fun setupIndicatorColor(indicatorColor: String) {
        itemView.buyerOrderDetailIndicator?.background = Utils.getColoredIndicator(itemView.context, indicatorColor)
    }

    private fun setupStatusHeader(orderStatus: String) {
        itemView.tvBuyerOrderDetailStatusOrder?.text = orderStatus
    }
}