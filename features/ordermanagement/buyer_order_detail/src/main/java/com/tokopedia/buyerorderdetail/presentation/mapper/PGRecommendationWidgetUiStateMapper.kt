package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState

object PGRecommendationWidgetUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: PGRecommendationWidgetUiState
    ): PGRecommendationWidgetUiState {
        val p1DataRequestState = getBuyerOrderDetailDataRequestState.getP1DataRequestState
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }
            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                mapOnGetBuyerOrderDetailError(
                    getBuyerOrderDetailRequestState.throwable, p1DataRequestState, currentState
                )
            }
            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                mapOnGetBuyerOrderDetailSuccess(
                    getBuyerOrderDetailRequestState.result, p1DataRequestState, currentState
                )
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailRequesting(
        currentState: PGRecommendationWidgetUiState
    ): PGRecommendationWidgetUiState {
        return if (currentState is PGRecommendationWidgetUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetBuyerOrderDetailSuccess(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        p1DataRequestState: GetP1DataRequestState,
        currentState: PGRecommendationWidgetUiState
    ): PGRecommendationWidgetUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(buyerOrderDetailData, p1DataRequestState, currentState)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(buyerOrderDetailData)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailError(
        throwable: Throwable?,
        p1DataRequestState: GetP1DataRequestState,
        currentState: PGRecommendationWidgetUiState
    ): PGRecommendationWidgetUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(throwable, p1DataRequestState, currentState)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(throwable)
            }
        }
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        currentState: PGRecommendationWidgetUiState
    ): PGRecommendationWidgetUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnOrderResolutionRequesting(currentState, buyerOrderDetailData)
            }
            is GetOrderResolutionRequestState.Complete -> {
                mapOnOrderResolutionComplete(buyerOrderDetailData)
            }
        }
    }

    private fun mapOnP1Requesting(
        throwable: Throwable?,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        currentState: PGRecommendationWidgetUiState
    ): PGRecommendationWidgetUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnOrderResolutionRequesting(currentState)
            }
            is GetOrderResolutionRequestState.Complete -> {
                mapOnOrderResolutionComplete(throwable)
            }
        }
    }

    private fun mapOnP1Complete(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): PGRecommendationWidgetUiState {
        return mapOnDataReady(buyerOrderDetailData)
    }

    private fun mapOnP1Complete(
        throwable: Throwable?
    ): PGRecommendationWidgetUiState {
        return mapOnError(throwable)
    }

    private fun mapOnOrderResolutionRequesting(
        currentState: PGRecommendationWidgetUiState,
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): PGRecommendationWidgetUiState {
        return if (currentState is PGRecommendationWidgetUiState.HasData) {
            mapOnReloading(buyerOrderDetailData)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnOrderResolutionRequesting(
        currentState: PGRecommendationWidgetUiState
    ): PGRecommendationWidgetUiState {
        return if (currentState is PGRecommendationWidgetUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnOrderResolutionComplete(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): PGRecommendationWidgetUiState {
        return mapOnDataReady(buyerOrderDetailData)
    }

    private fun mapOnOrderResolutionComplete(
        throwable: Throwable?,
    ): PGRecommendationWidgetUiState {
        return mapOnError(throwable)
    }

    private fun mapOnLoading(): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiState.Loading
    }

    private fun mapOnReloading(
        currentState: PGRecommendationWidgetUiState.HasData
    ): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnReloading(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiState.HasData.Reloading(
            mapToRecommendationWidgetUiModel(
                buyerOrderDetailData.adsPageName, buyerOrderDetailData.details?.nonBundles.orEmpty()
            )
        )
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiState.HasData.Showing(
            mapToRecommendationWidgetUiModel(
                buyerOrderDetailData.adsPageName, buyerOrderDetailData.details?.nonBundles.orEmpty()
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable?
    ): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiState.Error(throwable)
    }

    private fun mapToRecommendationWidgetUiModel(
        adsPageName: String,
        productsList: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Details.NonBundle>
    ): PGRecommendationWidgetUiModel {
        val productIdList = arrayListOf<String>()
        productsList.forEach { product ->
            productIdList.add(product.productId)
        }
        return PGRecommendationWidgetUiModel(adsPageName, productIdList)
    }
}
