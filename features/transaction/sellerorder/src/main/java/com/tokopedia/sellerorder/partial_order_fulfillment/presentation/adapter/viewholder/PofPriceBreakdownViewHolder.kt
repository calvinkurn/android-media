package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofPriceBreakdownBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofPriceBreakdownUiModel

class PofPriceBreakdownViewHolder(
    view: View
) : AbstractViewHolder<PofPriceBreakdownUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_price_breakdown
    }

    private val binding = ItemPofPriceBreakdownBinding.bind(view)

    override fun bind(element: PofPriceBreakdownUiModel) {
        setupLabel(element.label)
        setupValue(element.value, element.color)
    }

    override fun bind(element: PofPriceBreakdownUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupLabel(text: String) {
        binding.tvPofPriceBreakdownLabel.text = text
    }

    private fun setupValue(text: String, color: Int) {
        binding.tvPofPriceBreakdownValue.apply {
            this.text = text
            setTextColorCompat(color)
        }
    }
}
