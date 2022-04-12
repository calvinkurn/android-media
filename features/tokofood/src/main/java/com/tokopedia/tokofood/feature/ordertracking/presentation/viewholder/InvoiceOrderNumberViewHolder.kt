package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingInvoiceOrderNumberBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel

class InvoiceOrderNumberViewHolder(view: View) :
    BaseOrderTrackingViewHolder<InvoiceOrderNumberUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_invoice_order_number
    }

    private val binding = ItemTokofoodOrderTrackingInvoiceOrderNumberBinding.bind(itemView)

    override fun bind(element: InvoiceOrderNumberUiModel) {
        with(binding) {
            setInvoiceNumber(element.invoiceNumber)
            setOrderNumber(element.goFoodOrderNumber)
            setPaymentDate(element.paymentDate)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let {
            val (oldItem, newItem) = it
            if (oldItem is InvoiceOrderNumberUiModel && newItem is InvoiceOrderNumberUiModel) {
                if (oldItem.invoiceNumber != newItem.invoiceNumber) {
                    binding.setInvoiceNumber(newItem.invoiceNumber)
                }
                if (oldItem.goFoodOrderNumber != newItem.goFoodOrderNumber) {
                    binding.setOrderNumber(newItem.goFoodOrderNumber)
                }
                if (oldItem.paymentDate != newItem.paymentDate) {
                    binding.setPaymentDate(newItem.paymentDate)
                }
            }
        }
    }

    private fun ItemTokofoodOrderTrackingInvoiceOrderNumberBinding.setInvoiceNumber(invoiceNumber: String) {
        tvInvoiceNumberValue.text = invoiceNumber
    }

    private fun ItemTokofoodOrderTrackingInvoiceOrderNumberBinding.setOrderNumber(orderNumber: String) {
        if (orderNumber.isNotEmpty()) {
            tvOrderNumberValue.show()
            tvOrderNumberValue.text = orderNumber
        } else {
            tvOrderNumberValue.hide()
        }
    }

    private fun ItemTokofoodOrderTrackingInvoiceOrderNumberBinding.setPaymentDate(paymentDate: String) {
        tvPaymentDateValue.text = paymentDate
    }

}