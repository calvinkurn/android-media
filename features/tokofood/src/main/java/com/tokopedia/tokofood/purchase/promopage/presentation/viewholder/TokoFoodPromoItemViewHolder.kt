package com.tokopedia.tokofood.purchase.promopage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoCardBinding
import com.tokopedia.tokofood.purchase.promopage.presentation.TokoFoodPromoActionListener
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoItemUiModel

class TokoFoodPromoItemViewHolder(private val viewBinding: ItemTokofoodPromoCardBinding,
                                  private val listener: TokoFoodPromoActionListener)
    : AbstractViewHolder<TokoFoodPromoItemUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_card
    }

    override fun bind(element: TokoFoodPromoItemUiModel) {

    }

}