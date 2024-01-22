package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofSummaryDescriptionBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofSummaryDescriptionUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper

class PofSummaryDescriptionViewHolder(
    view: View
) : AbstractViewHolder<PofSummaryDescriptionUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_summary_description
    }

    private val binding = ItemPofSummaryDescriptionBinding.bind(view)

    override fun bind(element: PofSummaryDescriptionUiModel) {
        setupText(element.text)
    }

    override fun bind(element: PofSummaryDescriptionUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupText(text: String) {
        binding.tvPofSummaryDescription.text = HtmlLinkHelper(
            binding.root.context, text
        ).spannedString ?: String.EMPTY
    }
}
