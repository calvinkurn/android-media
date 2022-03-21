package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseSummaryTransactionBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseSummaryTransactionUiModel

class TokoFoodPurchaseSummaryTransactionViewHolder(private val viewBinding: ItemPurchaseSummaryTransactionBinding,
                                                   private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseSummaryTransactionUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_summary_transaction
    }

    override fun bind(element: TokoFoodPurchaseSummaryTransactionUiModel) {
        with(viewBinding) {

        }
    }

}