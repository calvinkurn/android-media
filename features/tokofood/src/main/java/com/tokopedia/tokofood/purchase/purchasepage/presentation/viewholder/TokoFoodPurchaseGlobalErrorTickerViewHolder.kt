package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseGlobalErrorTickerBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseGlobalErrorTickerUiModel

class TokoFoodPurchaseGlobalErrorTickerViewHolder(private val viewBinding: ItemPurchaseGlobalErrorTickerBinding,
                                                  private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseGlobalErrorTickerUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_global_error_ticker
    }

    override fun bind(element: TokoFoodPurchaseGlobalErrorTickerUiModel) {
        with(viewBinding) {

        }
    }

}