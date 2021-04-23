package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_status_info.view.*
import java.util.*

class OrderStatusInfoViewHolder(itemView: View?) : AbstractViewHolder<OrderStatusUiModel.OrderStatusInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_status_info
    }

    override fun bind(element: OrderStatusUiModel.OrderStatusInfoUiModel?) {
        element?.let {
            setupInvoice(it.invoice)
            setupPurchaseDate(it.purchaseDate)
            setupDeadline(it.deadline)
        }
    }

    private fun setupInvoice(invoice: String) {
        itemView.tvBuyerOrderDetailInvoice?.text = invoice
    }

    private fun setupPurchaseDate(purchaseDate: String) {
        itemView.tvBuyerOrderDetailPurchaseDateValue?.text = purchaseDate
    }

    private fun setupDeadline(deadline: Long) {
        itemView.timerBuyerOrderDetailDeadline?.apply {
            targetDate = Calendar.getInstance().apply {
                timeInMillis = deadline
            }
        }
    }
}