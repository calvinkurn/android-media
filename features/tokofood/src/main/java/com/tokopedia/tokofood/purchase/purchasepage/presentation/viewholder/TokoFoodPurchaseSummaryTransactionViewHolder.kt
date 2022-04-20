package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSurgeBottomsheet
import com.tokopedia.tokofood.databinding.ItemPurchaseSummaryTransactionBinding
import com.tokopedia.tokofood.databinding.SubItemPurchaseSummaryTransactionBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TokoFoodPurchaseSummaryTransactionViewHolder(private val viewBinding: ItemPurchaseSummaryTransactionBinding,
                                                   private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_summary_transaction
    }

    override fun bind(element: TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (element.isLoading) {
                constraintTransactionLoading.visible()
                containerTransactionItem.gone()
            } else {
                constraintTransactionLoading.gone()
                containerTransactionItem.removeAllViews()
                element.transactionList.forEach {
                    // TODO: Fix default hide logic
                    if (it.defaultValueForZero != TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_HIDE) {
                        val summaryTransactionItem = SubItemPurchaseSummaryTransactionBinding.inflate(LayoutInflater.from(itemView.context))
                        summaryTransactionItem.textTransactionTitle.text = it.title
                        summaryTransactionItem.iconTransactionSurgePrice.setSurgePrice(it.surgePriceInfo)
                        if (it.defaultValueForZero == TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_ZERO) {
                            summaryTransactionItem.textTransactionValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.value, false).removeDecimalSuffix()
                            containerTransactionItem.addView(summaryTransactionItem.root)
                        } else if (it.defaultValueForZero == TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_FREE) {
                            if (it.value > 0) {
                                summaryTransactionItem.textTransactionValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.value, false).removeDecimalSuffix()
                            } else {
                                summaryTransactionItem.textTransactionValue.text = "Gratis"
                            }
                            containerTransactionItem.addView(summaryTransactionItem.root)
                        } else {
                            if (it.value > 0) {
                                summaryTransactionItem.textTransactionValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.value, false).removeDecimalSuffix()
                                containerTransactionItem.addView(summaryTransactionItem.root)
                            }
                        }
                    }
                }
                containerTransactionItem.show()
            }
            tickerCancellationInfo.showWithCondition(element.bottomTickerMessage.isNotEmpty())
            tickerCancellationInfo.setHtmlDescription(element.bottomTickerMessage)
        }
    }

    private fun IconUnify.setSurgePrice(surgeBottomsheet: CheckoutTokoFoodShoppingSurgeBottomsheet?) {
        if (surgeBottomsheet == null) {
            hide()
            setOnClickListener(null)
        } else {
            show()
            setOnClickListener {
                listener.onSurgePriceIconClicked(surgeBottomsheet.title, surgeBottomsheet.description)
            }
        }
    }

}