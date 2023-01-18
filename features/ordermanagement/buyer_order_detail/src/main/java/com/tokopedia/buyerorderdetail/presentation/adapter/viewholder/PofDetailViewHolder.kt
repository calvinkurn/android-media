package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemPofDetailBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel

class PofDetailViewHolder(view: View) : CustomPayloadViewHolder<PofDetailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_detail
    }

    private val binding = ItemPofDetailBinding.bind(itemView)

    override fun bind(element: PofDetailUiModel) {
        with(binding) {
            setPofDetailLabel(element.label)
            setPofDetailValue(element.value)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is PofDetailUiModel && newItem is PofDetailUiModel) {
                if (oldItem.label != newItem.label) {
                    binding.setPofDetailLabel(newItem.label)
                }
                if (oldItem.value != newItem.value) {
                    binding.setPofDetailValue(newItem.value)
                }
            }
        }
    }

    private fun ItemPofDetailBinding.setPofDetailLabel(pofDetailLabel: String) {
        tvPofDetailLabel.text = pofDetailLabel
    }

    private fun ItemPofDetailBinding.setPofDetailValue(pofDetailValue: String) {
        tvPofDetailValue.text = pofDetailValue
    }
}
