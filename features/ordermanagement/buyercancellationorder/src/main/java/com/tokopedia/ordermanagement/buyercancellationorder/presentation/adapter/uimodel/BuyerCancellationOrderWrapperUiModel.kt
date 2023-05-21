package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel

import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData

data class BuyerCancellationOrderWrapperUiModel(
    val getCancellationReason: BuyerGetCancellationReasonData.Data.GetCancellationReason,
    val groupedOrderTitle: String,
    val groupedOrders: List<BuyerCancellationProductUiModel>,
    val groupType: Int,
    val tickerInfo: BuyerGetCancellationReasonData.Data.GetCancellationReason.TickerInfo?
) {
    companion object {
        const val NORMAL_ORDER_TYPE = 1
        const val OWOC_ORDER_TYPE = 2
    }

    fun isOwocType() = groupType == OWOC_ORDER_TYPE || tickerInfo != null
}
