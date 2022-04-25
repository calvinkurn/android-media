package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseTotalAmountBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TokoFoodPurchaseTotalAmountViewHolder(private val viewBinding: ItemPurchaseTotalAmountBinding,
                                            private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_total_amount
    }

    override fun bind(element: TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            when {
                element.isLoading -> {
                    totalAmountPurchase.isTotalAmountLoading.let { isLoading ->
                        if (!isLoading) {
                            totalAmountPurchase.isTotalAmountLoading = true
                        }
                    }
                }
                element.isEnabled -> {
                    totalAmountPurchase.amountCtaView.isLoading = element.isButtonLoading
                    if (totalAmountPurchase.isTotalAmountLoading) {
                        totalAmountPurchase.isTotalAmountLoading = false
                    }
                    totalAmountPurchase.amountCtaView.isEnabled = true
                    totalAmountPurchase.setCtaText("Pilih Pembayaran")
                    totalAmountPurchase.setLabelTitle("Total Tagihan")
                    val totalAmountString = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.totalAmount, false).removeDecimalSuffix()
                    totalAmountPurchase.setAmount(totalAmountString)
                    totalAmountPurchase.amountCtaView.setOnClickListener {
                        totalAmountPurchase.amountCtaView.isLoading = true
                        element.isButtonLoading = true
                        listener.onButtonCheckoutClicked()
                        totalAmountPurchase.amountCtaView.setOnClickListener(null)
                    }
                }
                else -> {
                    totalAmountPurchase.amountCtaView.isEnabled = false
                    totalAmountPurchase.setCtaText("Pilih Pembayaran")
                    totalAmountPurchase.setLabelTitle("Total Tagihan")
                    totalAmountPurchase.setAmount("-")
                }
            }
        }
    }

}