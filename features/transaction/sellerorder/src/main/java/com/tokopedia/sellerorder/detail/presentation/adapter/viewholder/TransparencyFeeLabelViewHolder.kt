package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDetailTransparencyFeeLabelBinding
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLabelUiModel

class TransparencyFeeLabelViewHolder(view: View?):
    AbstractViewHolder<TransparencyFeeLabelUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_transparency_fee_label
    }

    private val binding = ItemDetailTransparencyFeeLabelBinding.bind(itemView)

    override fun bind(element: TransparencyFeeLabelUiModel) {
        setupLabel(element)
    }

    private fun setupLabel(element: TransparencyFeeLabelUiModel) {
        binding.detailIncomeLabel.run {
            text = element.label
            setLabel(element.labelType)
        }
    }

}
