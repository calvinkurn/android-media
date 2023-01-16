package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemPofStillAvailableToggleBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductFulfilledUiModel

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
            setAvailableStockLabel(element.totalFulfilled)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is PofFulfilledToggleUiModel && newItem is PofFulfilledToggleUiModel) {
                if (oldItem.isExpanded != newItem.isExpanded) {
                    binding.setChevronToggle(newItem)
                }
                if (oldItem.totalFulfilled != newItem.totalFulfilled) {
                    binding.setAvailableStockLabel(newItem.totalFulfilled)
                }
            }
        }
    }

    private fun ItemPofStillAvailableToggleBinding.setAvailableStockLabel(totalFulFilled: Long) {
        tvProductLabel.text = getString(
            R.string.buyer_order_detail_pof_item_still_available_label,
            totalFulFilled.toString()
        )
    }

    private fun ItemPofStillAvailableToggleBinding.setChevronToggle(item: PofFulfilledToggleUiModel) {
        icDropdown.rotation = if (item.isExpanded) REVERSE_ROTATION else NO_ROTATION
        icDropdown.setOnClickListener {
            listener.onPofFulfilledToggleClicked(item.isExpanded, item.pofProductFulfilledList)
        }
    }

    interface Listener {
        fun onPofFulfilledToggleClicked(
            isExpanded: Boolean,
            fulfilledItems: List<PofProductFulfilledUiModel>
        )
    }
}
