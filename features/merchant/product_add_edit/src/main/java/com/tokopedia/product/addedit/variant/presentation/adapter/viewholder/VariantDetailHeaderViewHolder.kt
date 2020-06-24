package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailHeaderViewModel

class VariantDetailHeaderViewHolder(itemView: View?, listener: OnCollapsibleHeaderClickListener) : AbstractViewHolder<VariantDetailHeaderViewModel>(itemView) {

    interface OnCollapsibleHeaderClickListener {
        fun onHeaderClicked(adapterPosition: Int)
    }

    private var unitValueHeader: AppCompatTextView? = null

    private var headerPosition = 0

    init {
        unitValueHeader = itemView?.findViewById(R.id.tv_unit_value_header)

        itemView?.setOnClickListener {
            listener.onHeaderClicked(headerPosition)
        }
    }

    override fun bind(element: VariantDetailHeaderViewModel?) {
        element?.run {
            headerPosition = element.position
            unitValueHeader?.text = element.header
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.product_variant_detail_header_layout
    }
}