package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState

object OrderStatusUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: OrderStatusUiState
    ): OrderStatusUiState {
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
                    getBuyerOrderDetailRequestState, p1DataRequestState, currentState
                )
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailRequesting(
        currentState: OrderStatusUiState
    ): OrderStatusUiState {
        return if (currentState is OrderStatusUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetBuyerOrderDetailSuccess(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState,
        currentState: OrderStatusUiState
    ): OrderStatusUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(buyerOrderDetailRequestState, p1DataRequestState, currentState)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(buyerOrderDetailRequestState)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailError(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Error
    ): OrderStatusUiState {
        return mapOnError(buyerOrderDetailRequestState.throwable)
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState.Requesting,
        currentState: OrderStatusUiState
    ): OrderStatusUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnOrderResolutionRequesting(currentState, buyerOrderDetailRequestState)
            }
            else -> {
                mapOnOrderResolutionComplete(buyerOrderDetailRequestState)
            }
        }
    }

    private fun mapOnP1Complete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success
    ): OrderStatusUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result)
    }

    private fun mapOnOrderResolutionRequesting(
        currentState: OrderStatusUiState,
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success
    ): OrderStatusUiState {
        return if (currentState is OrderStatusUiState.HasData) {
            mapOnDataReady(buyerOrderDetailRequestState.result)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnOrderResolutionComplete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success
    ): OrderStatusUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result)
    }

    private fun mapOnLoading(): OrderStatusUiState {
        return OrderStatusUiState.Loading
    }

    private fun mapOnReloading(
        currentState: OrderStatusUiState.HasData
    ): OrderStatusUiState {
        return OrderStatusUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): OrderStatusUiState {
        return OrderStatusUiState.HasData.Showing(
            mapOrderStatusUiModel(
                buyerOrderDetailData.orderStatus,
                buyerOrderDetailData.tickerInfo,
                buyerOrderDetailData.preOrder,
                buyerOrderDetailData.invoice,
                buyerOrderDetailData.invoiceUrl,
                buyerOrderDetailData.deadline,
                buyerOrderDetailData.paymentDate,
                buyerOrderDetailData.orderId
            )
        )
    }

    private fun mapOnError(
        throwable: Throwable?
    ): OrderStatusUiState {
        return OrderStatusUiState.Error(throwable)
    }

    private fun mapOrderStatusUiModel(
        orderStatus: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.OrderStatus,
        tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo,
        preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder,
        invoice: String,
        invoiceUrl: String,
        deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline,
        paymentDate: String,
        orderId: String
    ): OrderStatusUiModel {
        return OrderStatusUiModel(
            orderStatusHeaderUiModel = mapOrderStatusHeaderUiModel(orderStatus, preOrder, orderId),
            orderStatusInfoUiModel = mapOrderStatusInfoUiModel(
                invoice, invoiceUrl, deadline, paymentDate, orderStatus.id, orderId
            ),
            ticker = mapTicker(tickerInfo)
        )
    }

    private fun mapOrderStatusHeaderUiModel(
        orderStatus: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.OrderStatus,
        preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder,
        orderId: String
    ): OrderStatusUiModel.OrderStatusHeaderUiModel {
        return OrderStatusUiModel.OrderStatusHeaderUiModel(
            indicatorColor = orderStatus.indicatorColor,
            orderId = orderId,
            orderStatus = orderStatus.statusName,
            orderStatusId = orderStatus.id,
            preOrder = mapPreOrderUiModel(preOrder)
        )
    }

    private fun mapOrderStatusInfoUiModel(
        invoice: String,
        invoiceUrl: String,
        deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline,
        paymentDate: String,
        orderStatusId: String,
        orderId: String
    ): OrderStatusUiModel.OrderStatusInfoUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel(
            deadline = mapDeadlineUiModel(deadline),
            invoice = mapInvoiceUiModel(invoice, invoiceUrl),
            purchaseDate = paymentDate,
            orderId = orderId,
            orderStatusId = orderStatusId
        )
    }

    private fun mapPreOrderUiModel(
        preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder
    ): OrderStatusUiModel.OrderStatusHeaderUiModel.PreOrderUiModel {
        return OrderStatusUiModel.OrderStatusHeaderUiModel.PreOrderUiModel(
            isPreOrder = preOrder.isPreOrder, label = preOrder.label, value = preOrder.value
        )
    }

    private fun mapDeadlineUiModel(
        deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline
    ): OrderStatusUiModel.OrderStatusInfoUiModel.DeadlineUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel.DeadlineUiModel(
            color = deadline.color, label = deadline.label, value = deadline.value
        )
    }

    private fun mapInvoiceUiModel(
        invoice: String, invoiceUrl: String
    ): OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel(
            invoice = invoice, url = invoiceUrl
        )
    }

    private fun mapTicker(
        tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo,
        actionKey: String? = null
    ): TickerUiModel {
        return TickerUiModel(
            actionKey = actionKey ?: tickerInfo.actionKey,
            actionText = tickerInfo.actionText,
            actionUrl = tickerInfo.actionUrl,
            description = tickerInfo.text,
            type = tickerInfo.type
        )
    }
}
