package com.tokopedia.minicart.common.widget.shoppingsummary.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryTotalTransactionUiModel
import com.tokopedia.minicart.databinding.ItemShoppingSummaryTotalTransactionBinding
import com.tokopedia.utils.htmltags.HtmlUtil

class ShoppingSummaryTotalTransactionViewHolder(private val viewBinding: ItemShoppingSummaryTotalTransactionBinding) :
    AbstractViewHolder<ShoppingSummaryTotalTransactionUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_shopping_summary_total_transaction
    }

    override fun bind(element: ShoppingSummaryTotalTransactionUiModel) {
        renderTitle(element)
        renderValue(element)
    }

    private fun renderTitle(element: ShoppingSummaryTotalTransactionUiModel) {
        viewBinding.textPriceTotalTitle.text = element.name
    }

    private fun renderValue(element: ShoppingSummaryTotalTransactionUiModel) {
        viewBinding.textPriceTotalValue.text = HtmlUtil.fromHtml(element.value)
    }
}
