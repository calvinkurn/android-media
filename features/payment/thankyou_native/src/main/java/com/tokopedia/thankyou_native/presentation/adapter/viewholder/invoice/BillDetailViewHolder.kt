package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.TotalFee
import kotlinx.android.synthetic.main.thank_widget_bill_detail.view.*

class BillDetailViewHolder(val view: View) : AbstractViewHolder<TotalFee>(view) {

    private val tvInvoiceTotalBillValue = itemView.tvInvoiceTotalBillValue
    private val tvInvoiceServiceTaxValue = itemView.tvInvoiceServiceTaxValue
    private val tvInvoiceServiceTax = itemView.tvInvoiceServiceTax

    override fun bind(element: TotalFee?) {
        element?.let {
            tvInvoiceTotalBillValue.text = getString(R.string.thankyou_rp_without_space, element.totalBillAmountStr)
            element.serviceFee?.let {
                tvInvoiceServiceTaxValue.text = getString(R.string.thankyou_rp_without_space, element.serviceFee)
                tvInvoiceServiceTaxValue.visible()
                tvInvoiceServiceTax.visible()
            } ?: run {
                tvInvoiceServiceTaxValue.gone()
                tvInvoiceServiceTax.gone()
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_bill_detail
    }
}
