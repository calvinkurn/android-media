package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderInsuranceUiState

object OrderInsuranceUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        val p1DataRequestState = getBuyerOrderDetailDataRequestState.getP1DataRequestState
        val getInsuranceDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP1DataRequestState
            .getInsuranceDetailRequestState
        return when (getInsuranceDetailRequestState) {
            is GetInsuranceDetailRequestState.Requesting -> {
                mapOnGetInsuranceDetailRequesting(currentState)
            }
            is GetInsuranceDetailRequestState.Complete.Error -> {
                mapOnGetInsuranceDetailError()
            }
            is GetInsuranceDetailRequestState.Complete.Success -> {
                mapOnGetInsuranceDetailSuccess(
                    getInsuranceDetailRequestState, p1DataRequestState, currentState
                )
            }
        }
    }

    private fun mapOnGetInsuranceDetailRequesting(
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        return if (currentState is OrderInsuranceUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetInsuranceDetailSuccess(
        insuranceDetailRequestState: GetInsuranceDetailRequestState.Complete.Success,
        p1DataRequestState: GetP1DataRequestState,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(insuranceDetailRequestState, p1DataRequestState, currentState)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(insuranceDetailRequestState)
            }
        }
    }

    private fun mapOnGetInsuranceDetailError(): OrderInsuranceUiState {
        return mapOnHidden()
    }

    private fun mapOnP1Requesting(
        insuranceDetailRequestState: GetInsuranceDetailRequestState.Complete.Success,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnOrderResolutionRequesting(currentState, insuranceDetailRequestState)
            }
            is GetOrderResolutionRequestState.Complete -> {
                mapOnOrderResolutionComplete(insuranceDetailRequestState)
            }
        }
    }

    private fun mapOnP1Complete(
        insuranceDetailRequestState: GetInsuranceDetailRequestState.Complete.Success
    ): OrderInsuranceUiState {
        return mapOnDataReady(insuranceDetailRequestState.result)
    }

    private fun mapOnOrderResolutionRequesting(
        currentState: OrderInsuranceUiState,
        insuranceDetailRequestState: GetInsuranceDetailRequestState.Complete.Success
    ): OrderInsuranceUiState {
        return if (currentState is OrderInsuranceUiState.HasData) {
            mapOnDataReady(insuranceDetailRequestState.result)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnOrderResolutionComplete(
        insuranceDetailRequestState: GetInsuranceDetailRequestState.Complete.Success
    ): OrderInsuranceUiState {
        return mapOnDataReady(insuranceDetailRequestState.result)
    }

    private fun mapOnLoading(): OrderInsuranceUiState {
        return OrderInsuranceUiState.Loading
    }

    private fun mapOnReloading(
        currentState: OrderInsuranceUiState.HasData
    ): OrderInsuranceUiState {
        return OrderInsuranceUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnDataReady(
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?
    ): OrderInsuranceUiState {
        return OrderInsuranceUiState.HasData.Showing(
            mapOrderInsurance(insuranceDetailData)
        )
    }

    private fun mapOnHidden(): OrderInsuranceUiState {
        return OrderInsuranceUiState.Hidden
    }

    private fun mapOrderInsurance(
        orderResolutionData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?
    ): OrderInsuranceUiModel {
        return OrderInsuranceUiModel(
            logoUrl = orderResolutionData?.orderConfig?.icon?.banner.orEmpty(),
            title = orderResolutionData?.orderConfig?.wording?.id?.bannerTitle.orEmpty(),
            subtitle = orderResolutionData?.orderConfig?.wording?.id?.bannerSubtitle.orEmpty(),
            appLink = orderResolutionData?.orderConfig?.redirection.orEmpty()
        )
    }
}
