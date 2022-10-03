package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState

object PGRecommendationWidgetUiStateMapper {

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

    fun mapGetP0DataRequestStateToPGRecommendationWidgetUiState(
        getP0DataRequestState: GetP0DataRequestState
    ): PGRecommendationWidgetUiState {
        return when (getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                when (val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState) {
                    is GetBuyerOrderDetailRequestState.Requesting -> {
                        PGRecommendationWidgetUiState.Loading
                    }
                    is GetBuyerOrderDetailRequestState.Success -> {
                        PGRecommendationWidgetUiState.Showing(
                            mapToRecommendationWidgetUiModel(
                                getBuyerOrderDetailRequestState.result.adsPageName,
                                getBuyerOrderDetailRequestState.result.details?.nonBundles.orEmpty()
                            )
                        )
                    }
                    is GetBuyerOrderDetailRequestState.Error -> {
                        PGRecommendationWidgetUiState.Error(getBuyerOrderDetailRequestState.throwable)
                    }
                }
            }
            is GetP0DataRequestState.Success -> {
                val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState
                PGRecommendationWidgetUiState.Showing(
                    mapToRecommendationWidgetUiModel(
                        getBuyerOrderDetailRequestState.result.adsPageName,
                        getBuyerOrderDetailRequestState.result.details?.nonBundles.orEmpty()
                    )
                )
            }
            else -> {
                PGRecommendationWidgetUiState.Loading
            }
        }
    }
}
