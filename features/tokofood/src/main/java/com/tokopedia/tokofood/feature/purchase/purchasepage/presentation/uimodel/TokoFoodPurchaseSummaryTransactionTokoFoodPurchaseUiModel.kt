package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.domain.response.CartListAddOnsCustomResponseInfo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodSummaryItemDetailInfo
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

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
            var value: String = "",
            var detailInfo: CartListAddOnsCustomResponseInfo? = null
    )

}
