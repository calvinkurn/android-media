package com.tokopedia.report.view.adapter

import android.view.View
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.util.ParentViewHolder
import kotlinx.android.synthetic.main.item_product_report_reason_parent.view.*

class ProductReasonParentViewHolder(val view: View): ParentViewHolder<ProductReportReason, ProductReportReason>(view){

    fun bind(parent: ProductReportReason) {
        itemView.title.text = parent.value
    }

    companion object {
        val LAYOUT = R.layout.item_product_report_reason_parent
    }
}