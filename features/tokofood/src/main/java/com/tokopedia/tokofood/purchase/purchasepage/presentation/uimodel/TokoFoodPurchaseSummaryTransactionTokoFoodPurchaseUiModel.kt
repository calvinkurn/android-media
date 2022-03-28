package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel(
        var subTotal: Transaction = Transaction(),
        var wrappingFee: Transaction = Transaction(),
        var shippingFee: Transaction = Transaction(),
        var serviceFee: Transaction = Transaction()
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, BaseTokoFoodPurchaseUiModel() {

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getTransactionList(): List<Transaction> {
        return listOf(subTotal, wrappingFee, shippingFee, serviceFee)
    }

    data class Transaction(
            var title: String = "",
            var value: Long = 0L,
            var defaultValueForZero: Int = DEFAULT_HIDE
    ) {
        companion object {
            const val DEFAULT_HIDE = 0
            const val DEFAULT_FREE = 1
            const val DEFAULT_ZERO = 2
        }
    }

}