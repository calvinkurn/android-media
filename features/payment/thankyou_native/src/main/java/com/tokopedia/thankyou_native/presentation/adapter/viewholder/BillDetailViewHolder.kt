package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.BillDetail
import kotlinx.android.synthetic.main.thank_widget_bill_detail.view.*

class BillDetailViewHolder(val view: View) : AbstractViewHolder<BillDetail>(view) {

    private val tvInvoiceTotalBillValue = itemView.tvInvoiceTotalBillValue
    private val tvInvoiceServiceTaxValue = itemView.tvInvoiceServiceTaxValue
    private val tvInvoiceServiceTax = itemView.tvInvoiceServiceTax
    private val tvInvoiceUsedTokopoints = itemView.tvInvoiceUsedTokopoints
    private val tvInvoiceUsedTokopointsValue = itemView.tvInvoiceUsedTokopointsValue

    override fun bind(element: BillDetail?) {
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

            element.tokoPointDeduction?.let {
                tvInvoiceUsedTokopoints.text = getString(R.string.thank_invoice_used_tokopoint, element.tokoPointDeduction)
                tvInvoiceUsedTokopointsValue.text = getString(R.string.thankyou_discounted_rp, element.tokoPointDeduction)
                tvInvoiceUsedTokopoints.visible()
                tvInvoiceUsedTokopointsValue.visible()
            } ?: run {
                tvInvoiceUsedTokopoints.gone()
                tvInvoiceUsedTokopointsValue.gone()
            }
        }

    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_bill_detail
    }
}
