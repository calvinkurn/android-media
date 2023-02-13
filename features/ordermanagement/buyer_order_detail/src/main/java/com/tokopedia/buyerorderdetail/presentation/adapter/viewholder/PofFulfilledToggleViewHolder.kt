package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemPofStillAvailableToggleBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel

class PofFulfilledToggleViewHolder(
    view: View,
    private val listener: Listener
) : CustomPayloadViewHolder<PofFulfilledToggleUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_still_available_toggle

        const val NO_ROTATION = 0F
        const val REVERSE_ROTATION = 180F
    }

    private val binding = ItemPofStillAvailableToggleBinding.bind(itemView)

    override fun bind(element: PofFulfilledToggleUiModel) {
        with(binding) {
            setChevronToggle(element)
            setAvailableStockLabel(element.headerFulfilled)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is PofFulfilledToggleUiModel && newItem is PofFulfilledToggleUiModel) {
                if (oldItem.isExpanded != newItem.isExpanded) {
                    binding.setChevronToggle(newItem)
                }
                if (oldItem.headerFulfilled != newItem.headerFulfilled) {
                    binding.setAvailableStockLabel(newItem.headerFulfilled)
                }
            }
        }
    }

    private fun ItemPofStillAvailableToggleBinding.setAvailableStockLabel(headerFulfilled: String) {
        tvProductLabel.text = headerFulfilled
    }

    private fun ItemPofStillAvailableToggleBinding.setChevronToggle(item: PofFulfilledToggleUiModel) {
        icDropdown.rotation = if (item.isExpanded) NO_ROTATION else REVERSE_ROTATION
        icDropdown.setOnClickListener {
            listener.onPofFulfilledToggleClicked(!item.isExpanded, item)
        }
        binding.root.setOnClickListener {
            listener.onPofFulfilledToggleClicked(!item.isExpanded, item)
        }
    }

    interface Listener {
        fun onPofFulfilledToggleClicked(
            isExpanded: Boolean,
            item: PofFulfilledToggleUiModel
        )
    }
}
