package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofFullyFulfilledProductListHeaderBinding
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofFullyFulfilledProductListHeaderUiModel

class PofFullyFulfilledProductListHeaderViewHolder(
    view: View
) : AbstractViewHolder<PofFullyFulfilledProductListHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_fully_fulfilled_product_list_header
    }

    private val binding = ItemPofFullyFulfilledProductListHeaderBinding.bind(view)

    override fun bind(element: PofFullyFulfilledProductListHeaderUiModel) {
        setupText(element.text)
    }

    override fun bind(element: PofFullyFulfilledProductListHeaderUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupText(text: StringRes) {
        binding.tvPofProductListHeaderFullyFulfilled.text = text.getString(binding.root.context)
    }
}
