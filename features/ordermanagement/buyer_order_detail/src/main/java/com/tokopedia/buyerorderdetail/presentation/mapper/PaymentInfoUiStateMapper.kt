package com.tokopedia.buyerorderdetail.presentation.mapper

import androidx.annotation.StringRes
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailTickerType
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState

object PaymentInfoUiStateMapper {

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

    fun mapGetP0DataRequestStateToPaymentInfoUiState(
        getP0DataRequestState: GetP0DataRequestState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return when (getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                when (val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState) {
                    is GetBuyerOrderDetailRequestState.Requesting -> {
                        PaymentInfoUiState.Loading
                    }
                    is GetBuyerOrderDetailRequestState.Success -> {
                        PaymentInfoUiState.Showing(
                            mapPaymentInfoUiModel(
                                getBuyerOrderDetailRequestState.result.payment,
                                getBuyerOrderDetailRequestState.result.cashbackInfo,
                                resourceProvider
                            )
                        )
                    }
                    is GetBuyerOrderDetailRequestState.Error -> {
                        PaymentInfoUiState.Error(getBuyerOrderDetailRequestState.throwable)
                    }
                }
            }
            is GetP0DataRequestState.Success -> {
                val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState
                PaymentInfoUiState.Showing(
                    mapPaymentInfoUiModel(
                        getBuyerOrderDetailRequestState.result.payment,
                        getBuyerOrderDetailRequestState.result.cashbackInfo,
                        resourceProvider
                    )
                )
            }
            else -> {
                PaymentInfoUiState.Loading
            }
        }
    }
}
