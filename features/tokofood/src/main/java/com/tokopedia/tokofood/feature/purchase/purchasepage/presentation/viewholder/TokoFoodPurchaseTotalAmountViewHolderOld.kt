package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseTotalAmountOldBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModelOld
import com.tokopedia.tokofood.feature.purchase.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TokoFoodPurchaseTotalAmountViewHolderOld(
    private val viewBinding: ItemPurchaseTotalAmountOldBinding,
    private val listener: TokoFoodPurchaseActionListener
) : AbstractViewHolder<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModelOld>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_total_amount_old
    }

    override fun bind(element: TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModelOld) {
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
                    totalAmountPurchase.setCtaText(getString(com.tokopedia.tokofood.R.string.text_purchase_choose_payment))
                    totalAmountPurchase.setLabelTitle(getString(com.tokopedia.tokofood.R.string.text_purchase_payment_total))
                    val totalAmountString =
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(element.totalAmount, false)
                            .removeDecimalSuffix()
                    totalAmountPurchase.setAmount(totalAmountString)
                    totalAmountPurchase.amountCtaView.setOnClickListener {
                        totalAmountPurchase.amountCtaView.isLoading = true
                        element.isButtonLoading = true
                        listener.onButtonCheckoutClicked()
                        totalAmountPurchase.amountCtaView.setOnClickListener(null)
                    }
                }
                else -> {
                    if (totalAmountPurchase.isTotalAmountLoading) {
                        totalAmountPurchase.isTotalAmountLoading = false
                    }
                    totalAmountPurchase.amountCtaView.isEnabled = false
                    totalAmountPurchase.setCtaText(getString(com.tokopedia.tokofood.R.string.text_purchase_choose_payment))
                    totalAmountPurchase.setLabelTitle(getString(com.tokopedia.tokofood.R.string.text_purchase_payment_total))
                    totalAmountPurchase.setAmount(getString(com.tokopedia.tokofood.R.string.text_purchase_dash))
                }
            }
        }
    }

}
