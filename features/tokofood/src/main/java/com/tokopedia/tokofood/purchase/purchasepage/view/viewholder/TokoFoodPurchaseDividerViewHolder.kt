package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseDividerBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseDividerUiModel

class TokoFoodPurchaseDividerViewHolder(private val viewBinding: ItemPurchaseDividerBinding,
                                        private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseDividerUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_divider
    }

    override fun bind(element: TokoFoodPurchaseDividerUiModel) {
        with(viewBinding) {

        }
    }

}