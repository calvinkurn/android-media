package com.tokopedia.buyerorderdetail.presentation.mapper

import androidx.annotation.StringRes
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailTickerType
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState

object PaymentInfoUiStateMapper {

    private fun mapOnGetBuyerOrderDetailDataStarted(
        buyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState.Started,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        val p1DataRequestState = buyerOrderDetailDataRequestState.getP1DataRequestState
        return when (val p0DataRequestState = buyerOrderDetailDataRequestState.getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                mapOnP0Requesting(p0DataRequestState, p1DataRequestState, resourceProvider)
            }
            is GetP0DataRequestState.Success -> {
                mapOnP0Success(p0DataRequestState, p1DataRequestState, resourceProvider)
            }
            is GetP0DataRequestState.Error -> {
                mapOnP0Error(p0DataRequestState)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailIdling(): PaymentInfoUiState {
        return mapOnLoading()
    }

    private fun mapOnP0Requesting(
        p0DataRequestState: GetP0DataRequestState.Requesting,
        p1DataRequestState: GetP1DataRequestState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return when (
            val getBuyerOrderDetailRequestState = p0DataRequestState.getBuyerOrderDetailRequestState
        ) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetBuyerOrderDetailRequestState.Success -> {
                mapOnGetBuyerOrderDetailIsSuccess(
                    getBuyerOrderDetailRequestState,
                    p1DataRequestState,
                    resourceProvider
                )
            }
            is GetBuyerOrderDetailRequestState.Error -> {
                mapOnError(getBuyerOrderDetailRequestState.throwable)
            }
        }
    }

    private fun mapOnP0Success(
        p0DataRequestState: GetP0DataRequestState.Success,
        p1DataRequestState: GetP1DataRequestState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return mapOnGetBuyerOrderDetailIsSuccess(
            p0DataRequestState.getBuyerOrderDetailRequestState,
            p1DataRequestState,
            resourceProvider
        )
    }

    private fun mapOnP0Error(
        p0DataRequestState: GetP0DataRequestState.Error
    ): PaymentInfoUiState {
        return mapOnError(p0DataRequestState.getThrowable())
    }

    private fun mapOnGetBuyerOrderDetailIsSuccess(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(buyerOrderDetailRequestState, p1DataRequestState, resourceProvider)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(buyerOrderDetailRequestState, resourceProvider)
            }
        }
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnLoading()
            }
            else -> {
                mapOnDataReady(buyerOrderDetailRequestState.result, resourceProvider)
            }
        }
    }

    private fun mapOnP1Complete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result, resourceProvider)
    }

    private fun mapOnLoading(): PaymentInfoUiState {
        return PaymentInfoUiState.Loading
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return PaymentInfoUiState.Showing(
            mapPaymentInfoUiModel(
                buyerOrderDetailData.payment,
                buyerOrderDetailData.cashbackInfo,
                resourceProvider
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable
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

    private fun mapPlainHeader(@StringRes headerStringResId: Int): PlainHeaderUiModel {
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
            label = paymentDetail.label,
            value = paymentDetail.value
        )
    }

    private fun mapPaymentMethodInfoItem(paymentMethod: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentMethod): PaymentInfoUiModel.PaymentInfoItemUiModel {
        return PaymentInfoUiModel.PaymentInfoItemUiModel(
            label = paymentMethod.label,
            value = paymentMethod.value
        )
    }

    private fun mapPaymentGrandTotal(paymentAmount: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentAmount): PaymentInfoUiModel.PaymentGrandTotalUiModel {
        return PaymentInfoUiModel.PaymentGrandTotalUiModel(
            label = paymentAmount.label,
            value = paymentAmount.value
        )
    }

    private fun mapPaymentTicker(cashbackInfo: String): TickerUiModel {
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

    fun mapGetBuyerOrderDetailDataRequestStateToPaymentInfoUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return when (getBuyerOrderDetailDataRequestState) {
            is GetBuyerOrderDetailDataRequestState.Started -> {
                mapOnGetBuyerOrderDetailDataStarted(
                    getBuyerOrderDetailDataRequestState,
                    resourceProvider
                )
            }
            else -> {
                mapOnGetBuyerOrderDetailIdling()
            }
        }
    }
}
