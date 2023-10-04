package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeComponentLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentLabelUiModel

class TransparencyFeeComponentLabelViewHolder(
    view: View
) : AbstractViewHolder<TransparencyFeeComponentLabelUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_component_label
    }

    private val binding = ItemDetailTransparencyFeeComponentLabelBinding.bind(view)

    override fun bind(element: TransparencyFeeComponentLabelUiModel) {
        binding.root.text = element.text
    }
}
