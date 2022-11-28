package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderInsuranceUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ShipmentInfoUiState

object BuyerOrderDetailUiStateMapper {
    @Suppress("NAME_SHADOWING")
    fun map(
        actionButtonsUiState: ActionButtonsUiState,
        orderStatusUiState: OrderStatusUiState,
        paymentInfoUiState: PaymentInfoUiState,
        productListUiState: ProductListUiState,
        shipmentInfoUiState: ShipmentInfoUiState,
        pgRecommendationWidgetUiState: PGRecommendationWidgetUiState,
        orderResolutionTicketStatusUiState: OrderResolutionTicketStatusUiState,
        orderInsuranceUiState: OrderInsuranceUiState
    ): BuyerOrderDetailUiState {
        return if (
            actionButtonsUiState is ActionButtonsUiState.HasData &&
            orderStatusUiState is OrderStatusUiState.HasData &&
            paymentInfoUiState is PaymentInfoUiState.HasData &&
            productListUiState is ProductListUiState.HasData &&
            shipmentInfoUiState is ShipmentInfoUiState.HasData &&
            pgRecommendationWidgetUiState is PGRecommendationWidgetUiState.HasData &&
            orderResolutionTicketStatusUiState !is OrderResolutionTicketStatusUiState.Loading
        ) {
            if (
                actionButtonsUiState is ActionButtonsUiState.HasData.Reloading ||
                orderStatusUiState is OrderStatusUiState.HasData.Reloading ||
                paymentInfoUiState is PaymentInfoUiState.HasData.Reloading ||
                productListUiState is ProductListUiState.HasData.Reloading ||
                shipmentInfoUiState is ShipmentInfoUiState.HasData.Reloading ||
                pgRecommendationWidgetUiState is PGRecommendationWidgetUiState.HasData.Reloading ||
                orderResolutionTicketStatusUiState is OrderResolutionTicketStatusUiState.HasData.Reloading ||
                orderInsuranceUiState is OrderInsuranceUiState.HasData.Reloading
            ) {
                BuyerOrderDetailUiState.HasData.PullRefreshLoading(
                    actionButtonsUiState,
                    orderStatusUiState,
                    paymentInfoUiState,
                    productListUiState,
                    shipmentInfoUiState,
                    pgRecommendationWidgetUiState,
                    orderResolutionTicketStatusUiState,
                    orderInsuranceUiState
                )
            } else {
                BuyerOrderDetailUiState.HasData.Showing(
                    actionButtonsUiState,
                    orderStatusUiState,
                    paymentInfoUiState,
                    productListUiState,
                    shipmentInfoUiState,
                    pgRecommendationWidgetUiState,
                    orderResolutionTicketStatusUiState,
                    orderInsuranceUiState
                )
            }
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
            BuyerOrderDetailUiState.FullscreenLoading
        }
    }
}
