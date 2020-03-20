package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.BillDetail

class BillDetailViewHolder(val view: View) : AbstractViewHolder<BillDetail>(view) {

    override fun bind(element: BillDetail?) {
        element?.let {
            view.findViewById<TextView>(R.id.tvInvoiceTotalBillValue)
                    .text = getString(R.string.thankyou_rp, element.totalBillAmountStr)
            view.findViewById<TextView>(R.id.tvInvoiceServiceTaxValue)
                    .text = getString(R.string.thankyou_rp, element.serviceFee)
            view.findViewById<TextView>(R.id.tvInvoiceUsedTokopoints).text = getString(R.string.thank_invoice_used_tokopoint, element.tokoPointDeduction)
            view.findViewById<TextView>(R.id.tvInvoiceUsedTokopointsValue)
                    .text = getString(R.string.thankyou_discounted_rp, element.tokoPointDeduction)
        }

    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_bill_detail
    }
}
