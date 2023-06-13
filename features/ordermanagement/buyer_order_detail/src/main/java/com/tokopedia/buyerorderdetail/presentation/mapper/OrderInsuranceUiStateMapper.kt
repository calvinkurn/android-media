package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetInsuranceDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.OrderInsuranceUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderInsuranceUiState
import com.tokopedia.kotlin.model.ImpressHolder

object OrderInsuranceUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        val getInsuranceDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP1DataRequestState
            .getInsuranceDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }
            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                mapOnGetBuyerOrderDetailError(getInsuranceDetailRequestState, currentState)
            }
            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                mapOnGetBuyerOrderDetailSuccess(
                    getBuyerOrderDetailRequestState.result,
                    getInsuranceDetailRequestState,
                    currentState
                )
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailRequesting(
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        return if (currentState is OrderInsuranceUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetBuyerOrderDetailError(
        getInsuranceDetailRequestState: GetInsuranceDetailRequestState,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        return when (getInsuranceDetailRequestState) {
            is GetInsuranceDetailRequestState.Requesting -> {
                mapOnGetInsuranceDetailRequesting(currentState)
            }
            is GetInsuranceDetailRequestState.Complete -> {
                mapOnGetInsuranceDetailComplete()
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailSuccess(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        getInsuranceDetailRequestState: GetInsuranceDetailRequestState,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        return when (getInsuranceDetailRequestState) {
            is GetInsuranceDetailRequestState.Requesting -> {
                mapOnGetInsuranceDetailRequesting(currentState)
            }
            is GetInsuranceDetailRequestState.Complete.Success -> {
                mapOnGetInsuranceDetailSuccess(
                    buyerOrderDetailData,
                    getInsuranceDetailRequestState.result,
                    currentState
                )
            }
            is GetInsuranceDetailRequestState.Complete.Error -> {
                mapOnGetInsuranceDetailError()
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

    private fun mapOnGetInsuranceDetailComplete(): OrderInsuranceUiState {
        return mapOnHidden()
    }

    private fun mapOnGetInsuranceDetailSuccess(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        return mapOnDataReady(buyerOrderDetailData, insuranceDetailData, currentState)
    }

    private fun mapOnGetInsuranceDetailError(): OrderInsuranceUiState {
        return mapOnHidden()
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
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiState {
        return OrderInsuranceUiState.HasData.Showing(
            mapOrderInsurance(buyerOrderDetailData, insuranceDetailData, currentState)
        )
    }

    private fun mapOnHidden(): OrderInsuranceUiState {
        return OrderInsuranceUiState.HasData.Hidden
    }

    private fun mapOrderInsurance(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        insuranceDetailData: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?,
        currentState: OrderInsuranceUiState
    ): OrderInsuranceUiModel {
        return OrderInsuranceUiModel(
            logoUrl = insuranceDetailData?.orderConfig?.icon?.banner.orEmpty(),
            title = insuranceDetailData?.orderConfig?.wording?.id?.bannerTitle.orEmpty(),
            subtitle = insuranceDetailData?.orderConfig?.wording?.id?.bannerSubtitle.orEmpty(),
            appLink = insuranceDetailData?.orderConfig?.redirection.orEmpty(),
            impressHolder = if (currentState is OrderInsuranceUiState.HasData) {
                currentState.data.impressHolder
            } else {
                ImpressHolder()
            },
            trackerData = mapTrackerData(buyerOrderDetailData)
        )
    }

    private fun mapTrackerData(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): OrderInsuranceUiModel.TrackerData {
        return OrderInsuranceUiModel.TrackerData(
            orderStatusCode = buyerOrderDetailData.orderStatus.id,
            orderId = buyerOrderDetailData.orderId
        )
    }
}
