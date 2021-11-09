package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartUnavailableReasonBinding

class MiniCartUnavailableReasonViewHolder(private val viewBinding: ItemMiniCartUnavailableReasonBinding)
    : AbstractViewHolder<MiniCartUnavailableReasonUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_unavailable_reason
    }

    override fun bind(element: MiniCartUnavailableReasonUiModel) {
        with(viewBinding) {
            textDisabledTitle.text = element.reason
            if (element.description.isNotBlank()) {
                textDisabledSubTitle.text = element.description
                textDisabledSubTitle.show()
            } else {
                textDisabledSubTitle.gone()
            }
        }
    }

}