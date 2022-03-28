package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseTotalAmountBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTotalAmountUiModel
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TokoFoodPurchaseTotalAmountViewHolder(private val viewBinding: ItemPurchaseTotalAmountBinding,
                                            private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseTotalAmountUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_total_amount
    }

    override fun bind(element: TokoFoodPurchaseTotalAmountUiModel) {
        with(viewBinding) {
            if (element.isDisabled) {
                totalAmountPurchase.amountCtaView.isEnabled = false
                totalAmountPurchase.setCtaText("Pilih Pembayaran")
                totalAmountPurchase.setLabelTitle("Total Tagihan")
                totalAmountPurchase.setAmount("-")
            } else {
                totalAmountPurchase.amountCtaView.isEnabled = true
                totalAmountPurchase.setCtaText("Pilih Pembayaran")
                totalAmountPurchase.setLabelTitle("Total Tagihan")
                val totalAmountString = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.totalAmount, false).removeDecimalSuffix()
                totalAmountPurchase.setAmount(totalAmountString)
                totalAmountPurchase.amountCtaView.setOnClickListener {
                    listener.onButtonCheckoutClicked()
                }
            }
        }
    }

}