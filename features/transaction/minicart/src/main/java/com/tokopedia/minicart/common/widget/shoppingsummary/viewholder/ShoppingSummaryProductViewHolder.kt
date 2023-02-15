package com.tokopedia.minicart.common.widget.shoppingsummary.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryProductUiModel
import com.tokopedia.minicart.databinding.ItemShoppingSummaryProductBinding

class ShoppingSummaryProductViewHolder(private val viewBinding: ItemShoppingSummaryProductBinding) :
    AbstractViewHolder<ShoppingSummaryProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_shopping_summary_product
    }

    override fun bind(element: ShoppingSummaryProductUiModel) {
        renderName(element)
        renderValue(element)
    }

    private fun renderName(element: ShoppingSummaryProductUiModel) {
        viewBinding.tpName.text = element.name
    }

    private fun renderValue(element: ShoppingSummaryProductUiModel) {
        viewBinding.tpValue.text = element.value
    }
}
