package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model

import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofRequestParams

sealed interface UiEvent {

    object None : UiEvent
    data class OpenScreen(
        val orderId: Long,
        val initialPofStatus: Int
    ) : UiEvent

    object ClickRetryOnErrorState : UiEvent
    object OnClickRetryFetchPofEstimate : UiEvent
    object OnClickOpenPofInfoSummary : UiEvent
    object OnClickDismissSummaryBottomSheet : UiEvent

    data class ProductAvailableQuantityChanged(
        val orderDetailId: Long,
        val availableQuantity: Int,
        val exceedCheckoutQuantity: Boolean
    ) : UiEvent

    data class OnClickSendPof(
        val pofDetailList: List<SendPofRequestParams.PofDetail>
    ) : UiEvent
}
