package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofProductListHeaderBinding
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductListHeaderUiModel

class PofProductListHeaderViewHolder(
    view: View
) : AbstractViewHolder<PofProductListHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_product_list_header
    }

    private val binding = ItemPofProductListHeaderBinding.bind(view)

    override fun bind(element: PofProductListHeaderUiModel) {
        setupTextLeft(element.textLeft)
        setupTextRight(element.textRight)
    }

    override fun bind(element: PofProductListHeaderUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupTextLeft(text: StringRes) {
        binding.tvPofProductListHeaderLeft.text = text.getString(binding.root.context)
    }

    private fun setupTextRight(text: StringRes) {
        binding.tvPofProductListHeaderRight.text = text.getString(binding.root.context)
    }
}
