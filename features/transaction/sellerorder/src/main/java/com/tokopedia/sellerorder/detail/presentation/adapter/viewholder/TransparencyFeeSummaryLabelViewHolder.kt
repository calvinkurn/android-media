package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeSummaryLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSummaryLabelUiModel

class TransparencyFeeSummaryLabelViewHolder(
    view: View
) : AbstractViewHolder<TransparencyFeeSummaryLabelUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_summary_label
    }

    private val binding = ItemDetailTransparencyFeeSummaryLabelBinding.bind(view)

    override fun bind(element: TransparencyFeeSummaryLabelUiModel) {
        binding.root.text = element.text
    }
}
