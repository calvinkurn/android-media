package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ShipmentInfoUiState

object BuyerOrderDetailUiStateMapper {
    fun map(
        actionButtonsUiState: ActionButtonsUiState,
        orderStatusUiState: OrderStatusUiState,
        paymentInfoUiState: PaymentInfoUiState,
        productListUiState: ProductListUiState,
        shipmentInfoUiState: ShipmentInfoUiState,
        pgRecommendationWidgetUiState: PGRecommendationWidgetUiState,
        orderResolutionTicketStatusUiState: OrderResolutionTicketStatusUiState,
        currentState: BuyerOrderDetailUiState
    ): BuyerOrderDetailUiState {
        return if (
            actionButtonsUiState is ActionButtonsUiState.Showing &&
            orderStatusUiState is OrderStatusUiState.Showing &&
            paymentInfoUiState is PaymentInfoUiState.Showing &&
            productListUiState is ProductListUiState.Showing &&
            shipmentInfoUiState is ShipmentInfoUiState.Showing &&
            pgRecommendationWidgetUiState is PGRecommendationWidgetUiState.Showing &&
            orderResolutionTicketStatusUiState !is OrderResolutionTicketStatusUiState.Loading
        ) {
            BuyerOrderDetailUiState.Showing(
                actionButtonsUiState,
                orderStatusUiState,
                paymentInfoUiState,
                productListUiState,
                shipmentInfoUiState,
                pgRecommendationWidgetUiState,
                orderResolutionTicketStatusUiState
            )
        } else if (actionButtonsUiState is ActionButtonsUiState.Error) {
            BuyerOrderDetailUiState.Error(actionButtonsUiState.throwable)
        } else if (orderStatusUiState is OrderStatusUiState.Error) {
            BuyerOrderDetailUiState.Error(orderStatusUiState.throwable)
        } else if (paymentInfoUiState is PaymentInfoUiState.Error) {
            BuyerOrderDetailUiState.Error(paymentInfoUiState.throwable)
        } else if (productListUiState is ProductListUiState.Error) {
            BuyerOrderDetailUiState.Error(productListUiState.throwable)
        } else if (shipmentInfoUiState is ShipmentInfoUiState.Error) {
            BuyerOrderDetailUiState.Error(shipmentInfoUiState.throwable)
        } else if (pgRecommendationWidgetUiState is PGRecommendationWidgetUiState.Error) {
            BuyerOrderDetailUiState.Error(pgRecommendationWidgetUiState.throwable)
        } else {
            if (currentState is BuyerOrderDetailUiState.HasData) {
                BuyerOrderDetailUiState.PullRefreshLoading(
                    actionButtonsUiState,
                    orderStatusUiState,
                    paymentInfoUiState,
                    productListUiState,
                    shipmentInfoUiState,
                    pgRecommendationWidgetUiState,
                    orderResolutionTicketStatusUiState
                )
            } else {
                BuyerOrderDetailUiState.FullscreenLoading
            }
        }
    }
}
