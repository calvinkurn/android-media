package com.tokopedia.report.view.adapter

import android.view.View
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.util.ChildViewHolder
import kotlinx.android.synthetic.main.item_product_report_reason_parent.view.*

class ProductReasonChildViewHolder (view: View): ChildViewHolder<ProductReportReason>(view){

    fun bind(productReportReason: ProductReportReason) {
        itemView.title.text = productReportReason.value
    }

    companion object {
        val LAYOUT = R.layout.item_product_report_reason_child
    }
}