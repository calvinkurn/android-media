package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseProductBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseProductUiModel

class TokoFoodPurchaseProductViewHolder(private val viewBinding: ItemPurchaseProductBinding,
                                        private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product
    }

    override fun bind(element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {

        }
    }

}