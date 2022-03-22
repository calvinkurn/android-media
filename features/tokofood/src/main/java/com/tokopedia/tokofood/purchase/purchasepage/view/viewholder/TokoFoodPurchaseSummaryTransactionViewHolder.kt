package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseSummaryTransactionBinding
import com.tokopedia.tokofood.databinding.SubItemPurchaseSummaryTransactionBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseSummaryTransactionUiModel
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TokoFoodPurchaseSummaryTransactionViewHolder(private val viewBinding: ItemPurchaseSummaryTransactionBinding,
                                                   private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseSummaryTransactionUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_summary_transaction
    }

    override fun bind(element: TokoFoodPurchaseSummaryTransactionUiModel) {
        with(viewBinding) {
            containerTransactionItem.removeAllViews()
            element.transactions.forEach {
                val summaryTransactionItem = SubItemPurchaseSummaryTransactionBinding.inflate(LayoutInflater.from(itemView.context))
                summaryTransactionItem.textTransactionTitle.text = it.title
                if (it.defaultValueForZero == TokoFoodPurchaseSummaryTransactionUiModel.Transaction.DEFAULT_ZERO) {
                    summaryTransactionItem.textTransactionValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.value, false).removeDecimalSuffix()
                    summaryTransactionItem.textTransactionValue.show()
                } else if (it.defaultValueForZero == TokoFoodPurchaseSummaryTransactionUiModel.Transaction.DEFAULT_FREE) {
                    if (it.value > 0) {
                        summaryTransactionItem.textTransactionValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.value, false).removeDecimalSuffix()
                    } else {
                        summaryTransactionItem.textTransactionValue.text = "Gratis"
                    }
                    summaryTransactionItem.textTransactionValue.show()
                } else {
                    if (it.value > 0) {
                        summaryTransactionItem.textTransactionValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.value, false).removeDecimalSuffix()
                        summaryTransactionItem.textTransactionValue.show()
                    } else {
                        summaryTransactionItem.textTransactionValue.gone()
                    }
                }
                containerTransactionItem.addView(summaryTransactionItem.root)
            }
            containerTransactionItem.show()
        }
    }

}