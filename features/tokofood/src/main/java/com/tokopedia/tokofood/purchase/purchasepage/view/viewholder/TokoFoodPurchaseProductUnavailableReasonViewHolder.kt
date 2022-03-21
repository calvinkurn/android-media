package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseProductUnavailableReasonBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseProductUnavailableReasonUiModel

class TokoFoodPurchaseProductUnavailableReasonViewHolder(private val viewBinding: ItemPurchaseProductUnavailableReasonBinding,
                                                         private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductUnavailableReasonUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product_unavailable_reason
    }

    override fun bind(element: TokoFoodPurchaseProductUnavailableReasonUiModel) {
        with(viewBinding) {

        }
    }

}