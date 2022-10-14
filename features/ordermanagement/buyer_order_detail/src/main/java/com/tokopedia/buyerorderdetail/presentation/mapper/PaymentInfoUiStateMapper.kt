package com.tokopedia.buyerorderdetail.presentation.mapper

import androidx.annotation.StringRes
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailTickerType
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState

object PaymentInfoUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: PaymentInfoUiState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        val p1DataRequestState = getBuyerOrderDetailDataRequestState.getP1DataRequestState
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }
            is GetBuyerOrderDetailRequestState.Error -> {
                mapOnGetBuyerOrderDetailError(getBuyerOrderDetailRequestState)
            }
            is GetBuyerOrderDetailRequestState.Success -> {
                mapOnGetBuyerOrderDetailSuccess(
                    getBuyerOrderDetailRequestState,
                    p1DataRequestState,
                    currentState,
                    resourceProvider
                )
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailRequesting(
        currentState: PaymentInfoUiState
    ): PaymentInfoUiState {
        return if (currentState is PaymentInfoUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetBuyerOrderDetailSuccess(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState,
        currentState: PaymentInfoUiState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(
                    buyerOrderDetailRequestState, p1DataRequestState, currentState, resourceProvider
                )
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(buyerOrderDetailRequestState, resourceProvider)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailError(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Error
    ): PaymentInfoUiState {
        return mapOnError(buyerOrderDetailRequestState.throwable)
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        currentState: PaymentInfoUiState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnOrderResolutionRequesting(
                    currentState,
                    buyerOrderDetailRequestState,
                    resourceProvider
                )
            }
            else -> {
                mapOnOrderResolutionComplete(buyerOrderDetailRequestState, resourceProvider)
            }
        }
    }

    private fun mapOnP1Complete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result, resourceProvider)
    }

    private fun mapOnOrderResolutionRequesting(
        currentState: PaymentInfoUiState,
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return if (currentState is PaymentInfoUiState.HasData) {
            mapOnDataReady(buyerOrderDetailRequestState.result, resourceProvider)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnOrderResolutionComplete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result, resourceProvider)
    }

    private fun mapOnLoading(): PaymentInfoUiState {
        return PaymentInfoUiState.Loading
    }

    private fun mapOnReloading(
        currentState: PaymentInfoUiState.HasData
    ): PaymentInfoUiState {
        return PaymentInfoUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return PaymentInfoUiState.HasData.Showing(
            mapPaymentInfoUiModel(
                buyerOrderDetailData.payment, buyerOrderDetailData.cashbackInfo, resourceProvider
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable?
    ): PaymentInfoUiState {
        return PaymentInfoUiState.Error(throwable)
    }

    private fun mapPaymentInfoUiModel(
        payment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment,
        cashbackInfo: String,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiModel {
        return PaymentInfoUiModel(
            headerUiModel = mapPlainHeader(resourceProvider.getPaymentInfoSectionHeader()),
            paymentMethodInfoItem = mapPaymentMethodInfoItem(payment.paymentMethod),
            paymentInfoItems = mapPaymentInfoItems(payment.paymentDetails),
            paymentGrandTotal = mapPaymentGrandTotal(payment.paymentAmount),
            ticker = mapPaymentTicker(cashbackInfo)
        )
    }

    private fun mapPlainHeader(
        @StringRes headerStringResId: Int
    ): PlainHeaderUiModel {
        return PlainHeaderUiModel(
            header = mapStringRes(headerStringResId)
        )
    }

    private fun mapPaymentInfoItems(
        paymentDetails: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentDetail>
    ): List<PaymentInfoUiModel.PaymentInfoItemUiModel> {
        return paymentDetails.map {
            mapPaymentInfoItem(it)
        }
    }

    private fun mapPaymentInfoItem(
        paymentDetail: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentDetail
    ): PaymentInfoUiModel.PaymentInfoItemUiModel {
        return PaymentInfoUiModel.PaymentInfoItemUiModel(
            label = paymentDetail.label, value = paymentDetail.value
        )
    }

    private fun mapPaymentMethodInfoItem(
        paymentMethod: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentMethod
    ): PaymentInfoUiModel.PaymentInfoItemUiModel {
        return PaymentInfoUiModel.PaymentInfoItemUiModel(
            label = paymentMethod.label, value = paymentMethod.value
        )
    }

    private fun mapPaymentGrandTotal(
        paymentAmount: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentAmount
    ): PaymentInfoUiModel.PaymentGrandTotalUiModel {
        return PaymentInfoUiModel.PaymentGrandTotalUiModel(
            label = paymentAmount.label, value = paymentAmount.value
        )
    }

    private fun mapPaymentTicker(
        cashbackInfo: String
    ): TickerUiModel {
        return TickerUiModel(
            actionKey = "",
            actionText = "",
            actionUrl = "",
            description = cashbackInfo,
            type = BuyerOrderDetailTickerType.INFO
        )
    }

    private fun mapStringRes(
        @StringRes resId: Int
    ): com.tokopedia.buyerorderdetail.presentation.model.StringRes {
        return com.tokopedia.buyerorderdetail.presentation.model.StringRes(resId)
    }
}
