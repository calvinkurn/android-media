package com.tokopedia.cartrevamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartAddOnSummaryBinding
import com.tokopedia.cartrevamp.domain.model.cartlist.SummaryTransactionUiModel

class CartAddOnSummaryViewHolder(private val binding: ItemCartAddOnSummaryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(element: SummaryTransactionUiModel.SummaryAddOns) {
        with(binding) {
            tvSummaryAddOnWording.text = element.wording
            tvSummaryAddOnValue.text = element.priceLabel
        }
    }

    companion object {
        val LAYOUT = R.layout.item_cart_add_on_summary
    }
}
