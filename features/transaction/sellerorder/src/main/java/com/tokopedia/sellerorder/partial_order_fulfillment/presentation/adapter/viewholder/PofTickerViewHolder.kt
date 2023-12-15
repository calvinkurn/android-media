package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofTickerBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofTickerUiModel

class PofTickerViewHolder(
    view: View
) : AbstractViewHolder<PofTickerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_ticker
    }

    private val binding = ItemPofTickerBinding.bind(view)

    override fun bind(element: PofTickerUiModel) {
        setupText(element.text)
    }

    override fun bind(element: PofTickerUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupText(text: String) {
        binding.root.setHtmlDescription(text)
    }
}
