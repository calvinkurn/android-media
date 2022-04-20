package com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSurgeBottomsheet
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel(
    val transactionList: List<Transaction>
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, CanLoadPartially, BaseTokoFoodPurchaseUiModel() {

    var isLoading = false

    override fun copyWithLoading(isLoading: Boolean): CanLoadPartially {
        return this.copy().apply {
            this.isLoading = isLoading
        }
    }

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Transaction(
            var title: String = "",
            var value: Long = 0L,
            var defaultValueForZero: Int = DEFAULT_HIDE,
            var surgePriceInfo: CheckoutTokoFoodShoppingSurgeBottomsheet? = null
    ) {
        companion object {
            const val DEFAULT_HIDE = 0
            const val DEFAULT_FREE = 1
            const val DEFAULT_ZERO = 2
        }
    }

}