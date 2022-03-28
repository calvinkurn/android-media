package com.tokopedia.tokofood.purchase.promopage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoTickerBinding
import com.tokopedia.tokofood.purchase.promopage.presentation.TokoFoodPromoActionListener
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoTickerUiModel

class TokoFoodPromoTickerViewHolder(private val viewBinding: ItemTokofoodPromoTickerBinding,
                                    private val listener: TokoFoodPromoActionListener)
    : AbstractViewHolder<TokoFoodPromoTickerUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_ticker
    }

    override fun bind(element: TokoFoodPromoTickerUiModel) {

    }

}