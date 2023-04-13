package com.tokopedia.minicart.common.widget.shoppingsummary.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummarySeparatorUiModel
import com.tokopedia.minicart.databinding.ItemShoppingSummarySeparatorBinding

class ShoppingSummarySeparatorViewHolder(viewBinding: ItemShoppingSummarySeparatorBinding) :
    AbstractViewHolder<ShoppingSummarySeparatorUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_shopping_summary_separator
    }

    override fun bind(element: ShoppingSummarySeparatorUiModel) {}
}
