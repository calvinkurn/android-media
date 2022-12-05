package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionResponse
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.kotlin.extensions.orFalse

object OrderResolutionTicketStatusUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: OrderResolutionTicketStatusUiState
    ): OrderResolutionTicketStatusUiState {
        val getOrderResolutionRequestState = getBuyerOrderDetailDataRequestState
            .getP1DataRequestState
            .getOrderResolutionRequestState
        return when (getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnGetOrderResolutionRequesting(currentState)
            }
            is GetOrderResolutionRequestState.Complete.Error -> {
                mapOnGetOrderResolutionError()
            }
            is GetOrderResolutionRequestState.Complete.Success -> {
                mapOnGetOrderResolutionSuccess(getOrderResolutionRequestState)
            }
        }
    }

    private fun mapOnGetOrderResolutionSuccess(
        orderResolutionRequestState: GetOrderResolutionRequestState.Complete.Success
    ): OrderResolutionTicketStatusUiState {
        return mapOnDataReady(orderResolutionRequestState.result)
    }

    private fun mapOnGetOrderResolutionRequesting(
        currentState: OrderResolutionTicketStatusUiState
    ): OrderResolutionTicketStatusUiState {
        return if (currentState is OrderResolutionTicketStatusUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetOrderResolutionError(): OrderResolutionTicketStatusUiState {
        return mapOnHidden()
    }

    private fun mapOnLoading(): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiState.Loading
    }

    private fun mapOnReloading(
        currentState: OrderResolutionTicketStatusUiState.HasData
    ): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnDataReady(
        orderResolutionData: GetOrderResolutionResponse.ResolutionGetTicketStatus.ResolutionData?
    ): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiState.HasData.Showing(
            mapOrderResolutionTicketStatus(orderResolutionData)
        )
    }

    private fun mapOnHidden(): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiState.HasData.Hidden
    }

    private fun mapOrderResolutionTicketStatus(
        resolutionData: GetOrderResolutionResponse.ResolutionGetTicketStatus.ResolutionData?
    ): OrderResolutionUiModel {
        return OrderResolutionUiModel(
            title = resolutionData?.cardTitle.orEmpty(),
            status = resolutionData?.resolutionStatus?.text.orEmpty(),
            description = resolutionData?.description.orEmpty(),
            picture = resolutionData?.profilePicture.orEmpty(),
            showDeadline = resolutionData?.deadline?.showDeadline.orFalse(),
            deadlineDateTime = resolutionData?.deadline?.datetime.orEmpty(),
            backgroundColor = resolutionData?.deadline?.backgroundColor.orEmpty(),
            redirectPath = resolutionData?.redirectPath?.android.orEmpty(),
            resolutionStatusFontColor = resolutionData?.resolutionStatus?.fontColor.orEmpty()
        )
    }
}
