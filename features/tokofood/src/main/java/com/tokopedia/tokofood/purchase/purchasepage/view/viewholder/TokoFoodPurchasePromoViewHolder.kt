package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchasePromoBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchasePromoUiModel

class TokoFoodPurchasePromoViewHolder(private val viewBinding: ItemPurchasePromoBinding,
                                      private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchasePromoUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_promo
    }

    override fun bind(element: TokoFoodPurchasePromoUiModel) {
        with(viewBinding) {

        }
    }

}