package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.InvoiceSummery

class InvoiceSummaryViewHolder(val view: View) : AbstractViewHolder<InvoiceSummery>(view) {

    override fun bind(element: InvoiceSummery?) {
        element?.let {
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalPrice)
                    .text = getString(R.string.thank_invoice_total_price, element.totalItemCount)
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalPriceValue)
                    .text = getString(R.string.thankyou_rp, element.totalItemCount)
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalDiscountValue)
                    .text = getString(R.string.thankyou_discounted_rp, element.totalItemDiscountStr)
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalProductProtectionValue)
                    .text = getString(R.string.thankyou_rp, element.totalProductProtectionStr)
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalShippingCostValue)
                    .text = getString(R.string.thankyou_rp, element.totalShippingChargeStr)
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalShippingCostDiscountValue)
                    .text = getString(R.string.thankyou_discounted_rp, element.totalShippingDiscountStr)
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalShippingInsuranceValue)
                    .text = getString(R.string.thankyou_rp, element.totalShippingInsuranceStr)
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalDonationValue)
                    .text = getString(R.string.thankyou_rp, element.donationAmountStr)
            view.findViewById<TextView>(R.id.tvInvoiceSummaryTotalEGoldValue)
                    .text = getString(R.string.thankyou_rp, element.eGoldPriceStr)
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_invoice_summary
    }
}