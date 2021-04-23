package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_payment_info_item.view.*

open class PaymentInfoItemViewHolder(itemView: View?): AbstractViewHolder<PaymentInfoUiModel.PaymentInfoItemUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_payment_info_item
    }

    override fun bind(element: PaymentInfoUiModel.PaymentInfoItemUiModel?) {
        element?.let {
            setupPaymentInfoLabel(it.label)
            setupPaymentInfoValue(it.value)
        }
    }

    private fun setupPaymentInfoLabel(label: String) {
        itemView.tvBuyerOrderDetailPaymentInfoLabel?.text = label
    }

    private fun setupPaymentInfoValue(value: String) {
        itemView.tvBuyerOrderDetailPaymentInfoValue?.text = value
    }
}