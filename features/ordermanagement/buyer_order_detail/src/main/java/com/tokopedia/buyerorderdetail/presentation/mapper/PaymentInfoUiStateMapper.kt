package com.tokopedia.buyerorderdetail.presentation.mapper

import androidx.annotation.StringRes
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailTickerType
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.EstimateInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofSummaryInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundSummaryUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState

object PaymentInfoUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: PaymentInfoUiState,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
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
                mapOnGetBuyerOrderDetailSuccess(
                    getBuyerOrderDetailRequestState.result,
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
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail,
        resourceProvider: ResourceProvider
    ): PaymentInfoUiState {
        return mapOnDataReady(buyerOrderDetailData, resourceProvider)
    }

    private fun mapOnGetBuyerOrderDetailError(
        throwable: Throwable?
    ): PaymentInfoUiState {
        return mapOnError(throwable)
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
            pofRefundInfoUiModel = mapPaymentRefundEstimate(payment.paymentRefund),
            ticker = mapPaymentTicker(cashbackInfo)
        )
    }

    private fun mapPlainHeader(
        @StringRes headerStringResId: Int
    ): PlainHeaderUiModel {
        return PlainHeaderUiModel(header = mapStringRes(headerStringResId))
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

    private fun mapPaymentRefundEstimate(
        paymentRefund: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentRefund
    ): PofRefundInfoUiModel {
        val paymentTotalAmount = paymentRefund.totalAmount
        return PofRefundInfoUiModel(
            totalAmountLabel = paymentTotalAmount.label,
            totalAmountValue = paymentTotalAmount.value,
            isRefunded = paymentRefund.isRefunded,
            estimateInfoUiModel = paymentRefund.estimateInfo?.let {
                EstimateInfoUiModel(
                    title = it.title,
                    info = it.info
                )
            },
            pofRefundSummaryUiModel = paymentRefund.summaryInfo?.let {
                PofRefundSummaryUiModel(
                    totalAmountValue = it.totalAmount.value,
                    totalAmountLabel = it.totalAmount.label,
                    footerInfo = it.footer,
                    detailsSummary = it.details.map { detail ->
                        PofSummaryInfoUiModel(
                            label = detail.label,
                            value = detail.value
                        )
                    }
                )
            }
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
