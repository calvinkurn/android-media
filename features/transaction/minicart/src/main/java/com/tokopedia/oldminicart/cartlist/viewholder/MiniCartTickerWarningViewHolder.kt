package com.tokopedia.oldminicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.databinding.ItemMiniCartTickerWarningBinding
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartTickerWarningUiModel

class MiniCartTickerWarningViewHolder(private val viewBinding: ItemMiniCartTickerWarningBinding)
    : AbstractViewHolder<MiniCartTickerWarningUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_ticker_warning
    }

    override fun bind(element: MiniCartTickerWarningUiModel) {
        with(viewBinding) {
            tickerInformation.setHtmlDescription(element.warningMessage)
        }
    }

}