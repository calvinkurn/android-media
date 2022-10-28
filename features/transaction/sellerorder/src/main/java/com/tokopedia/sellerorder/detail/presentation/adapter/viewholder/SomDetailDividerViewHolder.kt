package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemDividerBinding
import com.tokopedia.sellerorder.detail.presentation.model.DividerUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SomDetailDividerViewHolder(
    itemView: View?
): AbstractViewHolder<DividerUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_divider
    }

    private val binding by viewBinding<ItemDividerBinding>()

    override fun bind(element: DividerUiModel) {
        binding?.divider?.apply {
            val layoutParamsCopy = layoutParams
            layoutParamsCopy.height = element.height
            layoutParams = layoutParamsCopy
            setMargin(Int.ZERO, element.marginTop, Int.ZERO, element.marginBottom)
        }
    }
}