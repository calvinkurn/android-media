package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel

import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData

data class BuyerCancellationOrderWrapperUiModel(
    val getCancellationReason: BuyerGetCancellationReasonData.Data.GetCancellationReason,
    val groupedOrderTitle: String,
    val groupedOrders: List<BuyerCancellationProductUiModel>,
    val tickerInfo: BuyerGetCancellationReasonData.Data.GetCancellationReason.TickerInfo?
) {

    fun isOwocType(): Boolean {
        return tickerInfo != null && tickerInfo.text.isNotBlank()
    }
}
