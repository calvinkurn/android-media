package com.tokopedia.buyerorderdetail.presentation.uistate

data class BuyerOrderDetailChatCounterUiState(
    val counter: Int = 0,
    val error: Throwable? = null
)
