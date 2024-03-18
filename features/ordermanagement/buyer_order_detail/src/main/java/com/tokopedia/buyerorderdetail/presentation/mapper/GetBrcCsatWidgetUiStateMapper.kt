package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBrcCsatWidgetRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBrcCsatWidgetResponse
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.presentation.model.WidgetBrcCsatUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.WidgetBrcCsatUiState
import com.tokopedia.kotlin.extensions.orFalse

object GetBrcCsatWidgetUiStateMapper {
    fun map(
        currentState: WidgetBrcCsatUiState,
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState
    ): WidgetBrcCsatUiState {
        val brcCsatWidgetRequestState = getBuyerOrderDetailDataRequestState
            .getP1DataRequestState
            .getBrcCsatWidgetRequestState
        val orderID = (getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState as? GetBuyerOrderDetailRequestState.Complete.Success)
            ?.result
            ?.orderId
            .orEmpty()
        return when (brcCsatWidgetRequestState) {
            is GetBrcCsatWidgetRequestState.Requesting -> onRequesting(currentState = currentState)
            else -> {
                if (brcCsatWidgetRequestState is GetBrcCsatWidgetRequestState.Complete.Success) {
                    onSuccess(
                        currentState = currentState,
                        response = brcCsatWidgetRequestState.response,
                        orderID = orderID,
                        buyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState.getP0DataRequestState.getBuyerOrderDetailRequestState
                    )
                } else {
                    onError()
                }
            }
        }
    }

    private fun onRequesting(currentState: WidgetBrcCsatUiState): WidgetBrcCsatUiState {
        return if (currentState is WidgetBrcCsatUiState.HasData) {
            WidgetBrcCsatUiState.HasData.Reloading(currentState.data)
        } else {
            WidgetBrcCsatUiState.Loading
        }
    }

    private fun onSuccess(
        currentState: WidgetBrcCsatUiState,
        response: GetBrcCsatWidgetResponse.Data.ResolutionGetCsatFormV4?,
        orderID: String,
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState
    ): WidgetBrcCsatUiState {
        val helpPageUrl = (buyerOrderDetailRequestState as? GetBuyerOrderDetailRequestState.Complete.Success)
            ?.result
            ?.widget
            ?.resoCsat
            ?.helpUrl
            .orEmpty()
        val shouldShow = response?.data?.isEligible.orFalse() && helpPageUrl.isNotBlank()
        return if (shouldShow) {
            val expanded = if (currentState is WidgetBrcCsatUiState.HasData) {
                currentState.data.expanded
            } else {
                true
            }
            WidgetBrcCsatUiState.HasData.Showing(
                WidgetBrcCsatUiModel(orderID = orderID, helpUrl = helpPageUrl, expanded = expanded)
            )
        } else {
            WidgetBrcCsatUiState.Hidden
        }
    }

    private fun onError(): WidgetBrcCsatUiState.Hidden {
        return WidgetBrcCsatUiState.Hidden
    }
}
