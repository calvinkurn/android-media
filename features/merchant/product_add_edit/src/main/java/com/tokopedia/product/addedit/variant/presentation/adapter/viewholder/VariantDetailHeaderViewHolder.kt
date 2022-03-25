package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailHeaderUiModel

class VariantDetailHeaderViewHolder(itemView: View?, listener: OnCollapsibleHeaderClickListener) : AbstractViewHolder<VariantDetailHeaderUiModel>(itemView) {

    private var unitValueHeader: AppCompatTextView? = null
    private var accordionIndicator: AppCompatImageView? = null
    private var headerPosition = 0

    init {
        unitValueHeader = itemView?.findViewById(com.tokopedia.product.addedit.R.id.tv_unit_value_header)
        accordionIndicator = itemView?.findViewById(com.tokopedia.product.addedit.R.id.iv_accordion_indicator)

        itemView?.setOnClickListener {
            val isCollapsed = listener.onHeaderClicked(headerPosition)
            changeIndicatorIcon(isCollapsed)
        }
    }

    override fun bind(element: VariantDetailHeaderUiModel?) {
        element?.run {
            headerPosition = element.position
            unitValueHeader?.text = element.headerTitle
            changeIndicatorIcon(element.isCollapsed)
        }
    }

    private fun changeIndicatorIcon(isCollapsed: Boolean) {
        accordionIndicator?.setImageResource(
            if (isCollapsed)
                com.tokopedia.product.addedit.R.drawable.product_add_edit_ic_chevron_up
            else
                com.tokopedia.product.addedit.R.drawable.product_add_edit_ic_chevron_down
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.product.addedit.R.layout.product_variant_detail_header_layout
    }

    interface OnCollapsibleHeaderClickListener {
        fun onHeaderClicked(headerPosition:Int): Boolean
    }
}