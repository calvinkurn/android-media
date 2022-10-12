package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetResolutionTicketStatusResponse
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUIModel
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.kotlin.extensions.orFalse

object OrderResolutionTicketStatusUiStateMapper {

    private fun mapOnGetBuyerOrderDetailDataStarted(
        buyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState.Started
    ): OrderResolutionTicketStatusUiState {
        return when (val p1DataRequestState = buyerOrderDetailDataRequestState.getP1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(p1DataRequestState)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(p1DataRequestState)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailIdling(): OrderResolutionTicketStatusUiState {
        return mapOnLoading()
    }

    private fun mapOnP1Requesting(
        p1DataRequestState: GetP1DataRequestState.Requesting
    ): OrderResolutionTicketStatusUiState {
        return when (
            val getOrderResolutionRequestState = p1DataRequestState.getOrderResolutionRequestState
        ) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetOrderResolutionRequestState.Success -> {
                mapOnDataReady(getOrderResolutionRequestState.result)
            }
            is GetOrderResolutionRequestState.Error -> {
                mapOnHidden()
            }
        }
    }

    private fun mapOnP1Complete(
        p1DataRequestState: GetP1DataRequestState.Complete
    ): OrderResolutionTicketStatusUiState {
        return when (
            val getOrderResolutionRequestState = p1DataRequestState.getOrderResolutionRequestState
        ) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetOrderResolutionRequestState.Success -> {
                mapOnDataReady(getOrderResolutionRequestState.result)
            }
            is GetOrderResolutionRequestState.Error -> {
                mapOnHidden()
            }
        }
    }

    private fun mapOnLoading(): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiState.Loading
    }

    private fun mapOnDataReady(
        orderResolutionData: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData?
    ): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiState.Showing(
            mapOrderResolutionTicketStatus(orderResolutionData)
        )
    }

    private fun mapOnHidden(): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiState.Hidden
    }

    private fun mapOrderResolutionTicketStatus(
        resolutionData: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData?
    ): OrderResolutionUIModel {
        return OrderResolutionUIModel(
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

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState
    ): OrderResolutionTicketStatusUiState {
        return when (getBuyerOrderDetailDataRequestState) {
            is GetBuyerOrderDetailDataRequestState.Started -> {
                mapOnGetBuyerOrderDetailDataStarted(getBuyerOrderDetailDataRequestState)
            }
            else -> {
                mapOnGetBuyerOrderDetailIdling()
            }
        }
    }
}
