package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.BillDetail

class BillDetailViewHolder(val view: View) : AbstractViewHolder<BillDetail>(view) {

    override fun bind(element: BillDetail?) {
        element?.let {
            view.findViewById<TextView>(R.id.tvInvoiceTotalBillValue)
                    .text = getString(R.string.thankyou_rp, element.totalBillAmountStr)

            element.serviceFee?.let {
                view.findViewById<TextView>(R.id.tvInvoiceServiceTaxValue)
                        .text = getString(R.string.thankyou_rp, element.serviceFee)
                view.findViewById<TextView>(R.id.tvInvoiceServiceTaxValue).visible()
                view.findViewById<TextView>(R.id.tvInvoiceServiceTax).visible()
            } ?: run {
                view.findViewById<TextView>(R.id.tvInvoiceServiceTaxValue).gone()
                view.findViewById<TextView>(R.id.tvInvoiceServiceTax).gone()
            }

            element.tokoPointDeduction?.let {
                view.findViewById<TextView>(R.id.tvInvoiceUsedTokopoints).text = getString(R.string.thank_invoice_used_tokopoint, element.tokoPointDeduction)
                view.findViewById<TextView>(R.id.tvInvoiceUsedTokopointsValue).text = getString(R.string.thankyou_discounted_rp, element.tokoPointDeduction)
            } ?: run {
                view.findViewById<TextView>(R.id.tvInvoiceUsedTokopoints).gone()
                view.findViewById<TextView>(R.id.tvInvoiceUsedTokopointsValue).gone()
            }
        }

    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_bill_detail
    }
}
