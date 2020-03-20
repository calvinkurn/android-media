package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.InvoiceSummery
import com.tokopedia.thankyou_native.presentation.adapter.model.ObtainedAfterTransaction

class ObtainedBenefitViewHolder (val view: View) : AbstractViewHolder<ObtainedAfterTransaction>(view) {

    override fun bind(element: ObtainedAfterTransaction?) {
        element?.let {
//            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalPrice)
//                    .text = getString(R.string.thank_invoice_total_price, element.totalItemCount)
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_obtained_benifit
    }
}