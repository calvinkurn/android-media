package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofPriceBreakdownTotalBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofPriceBreakdownTotalUiModel

class PofPriceBreakdownTotalViewHolder(
    view: View
) : AbstractViewHolder<PofPriceBreakdownTotalUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_price_breakdown_total
    }

    private val binding = ItemPofPriceBreakdownTotalBinding.bind(view)

    override fun bind(element: PofPriceBreakdownTotalUiModel) {
        setupLabel(element.label)
        setupValue(element.value)
    }

    override fun bind(element: PofPriceBreakdownTotalUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupLabel(text: String) {
        binding.tvPofPriceBreakdownTotalLabel.text = text
    }

    private fun setupValue(text: String) {
        binding.tvPofPriceBreakdownTotalValue.text = text
    }
}
