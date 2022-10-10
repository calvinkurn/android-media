package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderInsuranceUiState

object OrderInsuranceUiStateMapper {

    private fun mapOnGetBuyerOrderDetailDataStarted(
        buyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState.Started
    ): OrderInsuranceUiState {
        return when (val p1DataRequestState = buyerOrderDetailDataRequestState.getP1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(p1DataRequestState)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(p1DataRequestState)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailIdling(): OrderInsuranceUiState {
        return mapOnLoading()
    }

    private fun mapOnP1Requesting(
        p1DataRequestState: GetP1DataRequestState.Requesting
    ): OrderInsuranceUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetOrderResolutionRequestState.Success -> {
                mapOnOrderResolutionSuccess(p1DataRequestState)
            }
            is GetOrderResolutionRequestState.Error -> {
                mapOnHidden()
            }
        }
    }

    private fun mapOnP1Complete(
        p1DataRequestState: GetP1DataRequestState.Complete
    ): OrderInsuranceUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetOrderResolutionRequestState.Success -> {
                mapOnOrderResolutionSuccess(p1DataRequestState)
            }
            is GetOrderResolutionRequestState.Error -> {
                mapOnHidden()
            }
        }
    }

    private fun mapOnOrderResolutionSuccess(
        p1DataRequestState: GetP1DataRequestState
    ): OrderInsuranceUiState {
        return when (
            val insuranceDetailRequestState =p1DataRequestState.getInsuranceDetailRequestState
        ) {
            is GetInsuranceDetailRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetInsuranceDetailRequestState.Success -> {
                mapOnDataReady(insuranceDetailRequestState.result)
            }
            is GetInsuranceDetailRequestState.Error -> {
                mapOnHidden()
            }
        }
    }

    private fun mapOnLoading(): OrderInsuranceUiState {
        return OrderInsuranceUiState.Loading
    }

    private fun mapOnDataReady(
        orderResolutionData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?
    ): OrderInsuranceUiState {
        return OrderInsuranceUiState.Showing(mapOrderInsurance(orderResolutionData))
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

    fun mapGetBuyerOrderDetailDataRequestStateToOrderInsuranceUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState
    ): OrderInsuranceUiState {
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
