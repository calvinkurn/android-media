package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.PlusSavings
import com.tokopedia.buyerorderdetail.presentation.model.SavingsWidgetUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.SavingsWidgetUiState

object SavingsWidgetUiStateMapper {
    fun map(getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState): SavingsWidgetUiState {
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                SavingsWidgetUiState.Error(getBuyerOrderDetailRequestState.throwable)
            }

            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                val savingsData = getBuyerOrderDetailRequestState.result.additionalData?.plusSavings
                val isPlus = getBuyerOrderDetailRequestState.result.isPlus
                if (savingsData == null || savingsData.shouldHide()) {
                    SavingsWidgetUiState.Hide
                } else {
                    mapIntoUiModel(savingsData, isPlus ?: false)
                }
            }

            else -> {
                SavingsWidgetUiState.Nothing
            }
        }
    }

    private fun mapIntoUiModel(
        plusSavings: PlusSavings,
        isPlus: Boolean
    ): SavingsWidgetUiState.Success {
        return SavingsWidgetUiState.Success(
            SavingsWidgetUiModel(
                plusTicker = plusSavings.plusTicker,
                plusComponents = plusSavings.plusComponents,
                isPlus = isPlus
            )
        )
    }
}
