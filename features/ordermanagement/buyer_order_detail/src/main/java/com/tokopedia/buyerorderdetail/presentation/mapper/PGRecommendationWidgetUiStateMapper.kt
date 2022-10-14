package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState

object PGRecommendationWidgetUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState
    ): PGRecommendationWidgetUiState {
        return when (getBuyerOrderDetailDataRequestState) {
            is GetBuyerOrderDetailDataRequestState.Started -> {
                mapOnGetBuyerOrderDetailDataStarted(getBuyerOrderDetailDataRequestState)
            }
            else -> {
                mapOnGetBuyerOrderDetailIdling()
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailDataStarted(
        buyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState.Started
    ): PGRecommendationWidgetUiState {
        val p1DataRequestState = buyerOrderDetailDataRequestState.getP1DataRequestState
        return when (val p0DataRequestState = buyerOrderDetailDataRequestState.getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                mapOnP0Requesting(p0DataRequestState, p1DataRequestState)
            }
            is GetP0DataRequestState.Success -> {
                mapOnP0Success(p0DataRequestState, p1DataRequestState)
            }
            is GetP0DataRequestState.Error -> {
                mapOnP0Error(p0DataRequestState)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailIdling(): PGRecommendationWidgetUiState {
        return mapOnLoading()
    }

    private fun mapOnP0Requesting(
        p0DataRequestState: GetP0DataRequestState.Requesting,
        p1DataRequestState: GetP1DataRequestState
    ): PGRecommendationWidgetUiState {
        return when (
            val getBuyerOrderDetailRequestState = p0DataRequestState.getBuyerOrderDetailRequestState
        ) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetBuyerOrderDetailRequestState.Success -> {
                mapOnGetBuyerOrderDetailIsSuccess(
                    getBuyerOrderDetailRequestState,
                    p1DataRequestState
                )
            }
            is GetBuyerOrderDetailRequestState.Error -> {
                mapOnError(getBuyerOrderDetailRequestState.throwable)
            }
        }
    }

    private fun mapOnP0Success(
        p0DataRequestState: GetP0DataRequestState.Success,
        p1DataRequestState: GetP1DataRequestState
    ): PGRecommendationWidgetUiState {
        return mapOnGetBuyerOrderDetailIsSuccess(
            p0DataRequestState.getBuyerOrderDetailRequestState,
            p1DataRequestState
        )
    }

    private fun mapOnP0Error(
        p0DataRequestState: GetP0DataRequestState.Error
    ): PGRecommendationWidgetUiState {
        return mapOnError(p0DataRequestState.getThrowable())
    }

    private fun mapOnGetBuyerOrderDetailIsSuccess(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState
    ): PGRecommendationWidgetUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(buyerOrderDetailRequestState, p1DataRequestState)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(buyerOrderDetailRequestState)
            }
        }
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState.Requesting
    ): PGRecommendationWidgetUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnLoading()
            }
            else -> {
                mapOnDataReady(buyerOrderDetailRequestState.result)
            }
        }
    }

    private fun mapOnP1Complete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success
    ): PGRecommendationWidgetUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result)
    }

    private fun mapOnLoading(): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiState.Loading
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiState.Showing(
            mapToRecommendationWidgetUiModel(
                buyerOrderDetailData.adsPageName,
                buyerOrderDetailData.details?.nonBundles.orEmpty()
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable
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
