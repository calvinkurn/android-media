package com.tokopedia.report.view.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.util.ExpandableRecyclerViewAdapter

class ProductReportReasonAdapter : ExpandableRecyclerViewAdapter<ProductReportReason, ProductReportReason,
        ProductReasonParentViewHolder, ProductReasonChildViewHolder>() {

    override fun onCreateParentViewHolder(parent: ViewGroup, viewType: Int): ProductReasonParentViewHolder {
        return ProductReasonParentViewHolder(parent.inflateLayout(ProductReasonParentViewHolder.LAYOUT))
    }

    override fun onCreateChildrenViewHolder(parent: ViewGroup, viewType: Int): ProductReasonChildViewHolder {
        return ProductReasonChildViewHolder(parent.inflateLayout(ProductReasonChildViewHolder.LAYOUT))
    }

    override fun onBindParentViewHolder(holder: ProductReasonParentViewHolder, parentPosition: Int) {
        val parent = parentList[parentPosition]
        holder.bind(parent)
        holder.itemView.setOnClickListener {
            if (parent.children.isNotEmpty())
                holder.onClick(it)
            else
                openFormReport(it.context, parent.categoryId, parent.value, parent.detail, parent.additionalFields)
        }
    }

    override fun onBindChildViewHolder(holder: ProductReasonChildViewHolder, parentPosition: Int, childPosition: Int) {
        val parent = parentList[parentPosition]
        val child = parent.children[childPosition]
        holder.bind(child)
        holder.itemView.setOnClickListener {
            openFormReport(it.context, child.categoryId, child.value, child.detail, parent.additionalFields)
        }
    }

    private fun openFormReport(context: Context, id: Int, title: String, detail: String,
                               fields: List<ProductReportReason.AdditionalField>){
        //TODO open form
    }
}