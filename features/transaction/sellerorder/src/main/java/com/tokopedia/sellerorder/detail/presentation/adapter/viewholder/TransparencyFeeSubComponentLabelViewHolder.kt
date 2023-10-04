package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeSubComponentLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSubComponentLabelUiModel

class TransparencyFeeSubComponentLabelViewHolder(
    view: View
) : AbstractViewHolder<TransparencyFeeSubComponentLabelUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_sub_component_label
    }

    private val binding = ItemDetailTransparencyFeeSubComponentLabelBinding.bind(view)

    override fun bind(element: TransparencyFeeSubComponentLabelUiModel) {
        binding.root.text = element.text
    }
}
