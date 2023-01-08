package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingInvoiceOrderNumberBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel

class InvoiceOrderNumberViewHolder(
    view: View,
    private val listener: Listener
) : CustomPayloadViewHolder<InvoiceOrderNumberUiModel>(view) {

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
            setInvoiceNumberClickListener(element)
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

    private fun ItemTokofoodOrderTrackingInvoiceOrderNumberBinding.setInvoiceNumberClickListener(
        element: InvoiceOrderNumberUiModel
    ) {
        tvInvoiceNumberValue.setOnClickListener {
            listener.onInvoiceOrderClicked(element.invoiceNumber, element.invoiceUrl)
        }
    }

    private fun ItemTokofoodOrderTrackingInvoiceOrderNumberBinding.setOrderNumber(orderNumber: String) {
        if (orderNumber.isNotEmpty()) {
            tvOrderNumberLabel.show()
            tvOrderNumberValue.show()
            tvOrderNumberValue.text = orderNumber
        } else {
            tvOrderNumberLabel.hide()
            tvOrderNumberValue.hide()
        }
    }

    private fun ItemTokofoodOrderTrackingInvoiceOrderNumberBinding.setPaymentDate(paymentDate: String) {
        tvPaymentDateValue.text = paymentDate
    }

    interface Listener {
        fun onInvoiceOrderClicked(invoiceNumber: String, invoiceUrl: String)
    }
}