package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.InvoiceSummery
import kotlinx.android.synthetic.main.thank_widget_invoice_summary.view.*

class InvoiceSummaryViewHolder(val view: View) : AbstractViewHolder<InvoiceSummery>(view) {

    private val invoiceSummaryContainer: LinearLayout = view.llInvoiceSummaryContainer

    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(view.context)
    }

    override fun bind(element: InvoiceSummery?) {
        invoiceSummaryContainer.removeAllViews()
        element?.apply {
            addInvoiceSummaryRow(getString(R.string.thank_invoice_total_price, element.totalCount.toString()), element.totalPriceStr)
            invoiceSummaryMapList.forEach {
                val valueStr = if (it.isDiscounted) {
                    getString(R.string.thankyou_discounted_rp, it.value)
                } else {
                    getString(R.string.thankyou_rp_without_space, it.value)
                }
                addInvoiceSummaryRow(it.title, valueStr)
            }
        }
    }

    private fun addInvoiceSummaryRow(titleStr: String, valueStr: String) {
        val rowView = inflater.inflate(R.layout.thank_payment_mode_item, null, false)
        val tvTitle = rowView.findViewById<TextView>(R.id.tvInvoicePaymentModeName)
        val tvValue = rowView.findViewById<TextView>(R.id.tvInvoicePaidWithModeValue)
        tvTitle.text = titleStr
        tvValue.text = valueStr
        invoiceSummaryContainer.addView(rowView)
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_invoice_summary
    }
}