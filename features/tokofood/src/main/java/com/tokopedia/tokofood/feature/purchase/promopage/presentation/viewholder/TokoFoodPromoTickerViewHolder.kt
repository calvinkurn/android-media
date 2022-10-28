package com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoTickerBinding
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoTickerUiModel

class TokoFoodPromoTickerViewHolder(private val viewBinding: ItemTokofoodPromoTickerBinding)
    : AbstractViewHolder<TokoFoodPromoTickerUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_ticker
    }

    override fun bind(element: TokoFoodPromoTickerUiModel) {
        viewBinding.tickerPromoTokofood.setHtmlDescription(element.message)
    }

}