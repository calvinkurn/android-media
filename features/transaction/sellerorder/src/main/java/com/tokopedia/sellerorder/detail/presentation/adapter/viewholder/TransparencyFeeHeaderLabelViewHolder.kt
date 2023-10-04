package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeHeaderLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderLabelUiModel

class TransparencyFeeHeaderLabelViewHolder(
    view: View
): AbstractViewHolder<TransparencyFeeHeaderLabelUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_header_label
    }

    private val binding = ItemDetailTransparencyFeeHeaderLabelBinding.bind(view)

    override fun bind(element: TransparencyFeeHeaderLabelUiModel) {
        binding.root.text = element.text
    }
}
