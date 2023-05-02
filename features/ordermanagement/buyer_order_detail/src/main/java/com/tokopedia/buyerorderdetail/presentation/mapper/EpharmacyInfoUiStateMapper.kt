package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.EpharmacyInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.EpharmacyInfoUiState

object EpharmacyInfoUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: EpharmacyInfoUiState
    ): EpharmacyInfoUiState {
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }
            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                mapOnGetBuyerOrderDetailError(
                    getBuyerOrderDetailRequestState.throwable
                )
            }
            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                mapOnDataReady(
                    getBuyerOrderDetailRequestState.result
                )
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailRequesting(
        currentState: EpharmacyInfoUiState
    ): EpharmacyInfoUiState {
        return if (currentState is EpharmacyInfoUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnLoading(): EpharmacyInfoUiState {
        return EpharmacyInfoUiState.Loading
    }

    private fun mapOnReloading(
        currentState: EpharmacyInfoUiState.HasData
    ): EpharmacyInfoUiState {
        return EpharmacyInfoUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnError(
        throwable: Throwable?
    ): EpharmacyInfoUiState {
        return EpharmacyInfoUiState.Error(throwable)
    }

    private fun mapOnGetBuyerOrderDetailError(
        throwable: Throwable?
    ): EpharmacyInfoUiState {
        return mapOnError(throwable)
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
    ): EpharmacyInfoUiState {
        return EpharmacyInfoUiState.HasData.Showing(
            mapEpharmacyInfoUiModel(
                buyerOrderDetailData.additionalData.epharmacyData
            )
        )
    }

    private fun mapEpharmacyInfoUiModel(
        epharmacyData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BomAdditionalData.EpharmacyData
    ): EpharmacyInfoUiModel {
        return EpharmacyInfoUiModel(
            consultationDate = epharmacyData.consultationDate,
            consultationDoctorName = epharmacyData.consultationDoctorName,
            consultationExpiryDate = epharmacyData.consultationExpiryDate,
            consultationName = epharmacyData.consultationName,
            consultationPatientName = epharmacyData.consultationPatientName,
            consultationPrescriptionNumber = epharmacyData.consultationPrescriptionNumber
        )
    }

}