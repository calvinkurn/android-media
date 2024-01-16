package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofThinDividerBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThinDividerUiModel

class PofThinDividerViewHolder(
    view: View
) : AbstractViewHolder<PofThinDividerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_thin_divider
    }

    private val binding = ItemPofThinDividerBinding.bind(view)

    override fun bind(element: PofThinDividerUiModel) {
        binding.dividerPofThin.setMargin(
            left = binding.dividerPofThin.resources.getDimensionPixelSize(element.marginStart),
            right = binding.dividerPofThin.resources.getDimensionPixelSize(element.marginEnd),
            top = binding.dividerPofThin.resources.getDimensionPixelSize(element.marginTop),
            bottom = binding.dividerPofThin.resources.getDimensionPixelSize(element.marginBottom)
        )
    }

    override fun bind(element: PofThinDividerUiModel, payloads: MutableList<Any>) {
        bind(element)
    }
}
