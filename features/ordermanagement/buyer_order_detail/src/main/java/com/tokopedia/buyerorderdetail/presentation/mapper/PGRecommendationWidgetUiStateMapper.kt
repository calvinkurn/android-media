package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState

object PGRecommendationWidgetUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: PGRecommendationWidgetUiState
    ): PGRecommendationWidgetUiState {
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }
            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                mapOnGetBuyerOrderDetailError(getBuyerOrderDetailRequestState.throwable)
            }
            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                mapOnGetBuyerOrderDetailSuccess(getBuyerOrderDetailRequestState.result)
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
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): PGRecommendationWidgetUiState {
        return mapOnDataReady(buyerOrderDetailData)
    }

    private fun mapOnGetBuyerOrderDetailError(
        throwable: Throwable?
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
