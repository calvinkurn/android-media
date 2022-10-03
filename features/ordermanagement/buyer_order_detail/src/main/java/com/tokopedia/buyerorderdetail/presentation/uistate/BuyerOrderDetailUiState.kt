package com.tokopedia.buyerorderdetail.presentation.uistate

sealed interface BuyerOrderDetailUiState {
    interface HasData : BuyerOrderDetailUiState {
        val actionButtonsUiState: ActionButtonsUiState
        val orderStatusUiState: OrderStatusUiState
        val paymentInfoUiState: PaymentInfoUiState
        val productListUiState: ProductListUiState
        val shipmentInfoUiState: ShipmentInfoUiState
        val pgRecommendationWidgetUiState: PGRecommendationWidgetUiState
        val orderResolutionUiState: OrderResolutionTicketStatusUiState
    }

    object FullscreenLoading : BuyerOrderDetailUiState
    data class PullRefreshLoading(
        override val actionButtonsUiState: ActionButtonsUiState,
        override val orderStatusUiState: OrderStatusUiState,
        override val paymentInfoUiState: PaymentInfoUiState,
        override val productListUiState: ProductListUiState,
        override val shipmentInfoUiState: ShipmentInfoUiState,
        override val pgRecommendationWidgetUiState: PGRecommendationWidgetUiState,
        override val orderResolutionUiState: OrderResolutionTicketStatusUiState
    ) : HasData

    data class Showing(
        override val actionButtonsUiState: ActionButtonsUiState.Showing,
        override val orderStatusUiState: OrderStatusUiState.Showing,
        override val paymentInfoUiState: PaymentInfoUiState.Showing,
        override val productListUiState: ProductListUiState.Showing,
        override val shipmentInfoUiState: ShipmentInfoUiState.Showing,
        override val pgRecommendationWidgetUiState: PGRecommendationWidgetUiState.Showing,
        override val orderResolutionUiState: OrderResolutionTicketStatusUiState
    ) : HasData

    data class Error(val throwable: Throwable) : BuyerOrderDetailUiState
}
