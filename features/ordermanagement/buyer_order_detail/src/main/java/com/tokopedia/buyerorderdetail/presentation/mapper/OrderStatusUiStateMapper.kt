package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState

object OrderStatusUiStateMapper {

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
                invoice,
                invoiceUrl,
                deadline,
                paymentDate,
                orderStatus.id,
                orderId
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

    private fun mapPreOrderUiModel(preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder): OrderStatusUiModel.OrderStatusHeaderUiModel.PreOrderUiModel {
        return OrderStatusUiModel.OrderStatusHeaderUiModel.PreOrderUiModel(
            isPreOrder = preOrder.isPreOrder,
            label = preOrder.label,
            value = preOrder.value
        )
    }

    private fun mapDeadlineUiModel(deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline): OrderStatusUiModel.OrderStatusInfoUiModel.DeadlineUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel.DeadlineUiModel(
            color = deadline.color,
            label = deadline.label,
            value = deadline.value
        )
    }

    private fun mapInvoiceUiModel(
        invoice: String,
        invoiceUrl: String
    ): OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel(
            invoice = invoice,
            url = invoiceUrl
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

    fun mapGetP0DataRequestStateToOrderStatusUiState(
        getP0DataRequestState: GetP0DataRequestState,
    ): OrderStatusUiState {
        return when (getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                when (val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState) {
                    is GetBuyerOrderDetailRequestState.Requesting -> {
                        OrderStatusUiState.Loading
                    }
                    is GetBuyerOrderDetailRequestState.Success -> {
                        OrderStatusUiState.Showing(
                            mapOrderStatusUiModel(
                                getBuyerOrderDetailRequestState.result.orderStatus,
                                getBuyerOrderDetailRequestState.result.tickerInfo,
                                getBuyerOrderDetailRequestState.result.preOrder,
                                getBuyerOrderDetailRequestState.result.invoice,
                                getBuyerOrderDetailRequestState.result.invoiceUrl,
                                getBuyerOrderDetailRequestState.result.deadline,
                                getBuyerOrderDetailRequestState.result.paymentDate,
                                getBuyerOrderDetailRequestState.result.orderId
                            )
                        )
                    }
                    is GetBuyerOrderDetailRequestState.Error -> {
                        OrderStatusUiState.Error(getBuyerOrderDetailRequestState.throwable)
                    }
                }
            }
            is GetP0DataRequestState.Success -> {
                val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState
                OrderStatusUiState.Showing(
                    mapOrderStatusUiModel(
                        getBuyerOrderDetailRequestState.result.orderStatus,
                        getBuyerOrderDetailRequestState.result.tickerInfo,
                        getBuyerOrderDetailRequestState.result.preOrder,
                        getBuyerOrderDetailRequestState.result.invoice,
                        getBuyerOrderDetailRequestState.result.invoiceUrl,
                        getBuyerOrderDetailRequestState.result.deadline,
                        getBuyerOrderDetailRequestState.result.paymentDate,
                        getBuyerOrderDetailRequestState.result.orderId
                    )
                )
            }
            else -> {
                OrderStatusUiState.Loading
            }
        }
    }
}
