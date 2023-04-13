package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemPofDetailBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailUiModel

class PofDetailViewHolder(view: View) : AbstractViewHolder<PofDetailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_detail
    }

    private val binding = ItemPofDetailBinding.bind(itemView)

    override fun bind(element: PofDetailUiModel) {
        with(binding) {
            setPofDetailLabel(element.label)
            setPofDetailValue(element.value)
        }
    }

    private fun ItemPofDetailBinding.setPofDetailLabel(pofDetailLabel: String) {
        tvPofDetailLabel.text = pofDetailLabel
    }

    private fun ItemPofDetailBinding.setPofDetailValue(pofDetailValue: String) {
        tvPofDetailValue.text = pofDetailValue
    }
}
