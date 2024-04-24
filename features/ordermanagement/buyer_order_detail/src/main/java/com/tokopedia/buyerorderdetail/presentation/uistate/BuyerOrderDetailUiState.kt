package com.tokopedia.buyerorderdetail.presentation.uistate

sealed interface BuyerOrderDetailUiState {

    object FullscreenLoading : BuyerOrderDetailUiState

    sealed interface HasData : BuyerOrderDetailUiState {
        val actionButtonsUiState: ActionButtonsUiState.HasData
        val orderStatusUiState: OrderStatusUiState.HasData
        val paymentInfoUiState: PaymentInfoUiState.HasData
        val productListUiState: ProductListUiState.HasData
        val shipmentInfoUiState: ShipmentInfoUiState.HasData
        val pgRecommendationWidgetUiState: PGRecommendationWidgetUiState.HasData
        val orderResolutionUiState: OrderResolutionTicketStatusUiState
        val orderInsuranceUiState: OrderInsuranceUiState
        val epharmacyInfoUiState: EpharmacyInfoUiState.HasData
        val scpRewardsMedalTouchPointWidgetUiState: ScpRewardsMedalTouchPointWidgetUiState
        val savingsWidgetUiState: SavingsWidgetUiState
        val brcCsatUiState: WidgetBrcCsatUiState

        data class PullRefreshLoading(
            override val actionButtonsUiState: ActionButtonsUiState.HasData,
            override val orderStatusUiState: OrderStatusUiState.HasData,
            override val paymentInfoUiState: PaymentInfoUiState.HasData,
            override val productListUiState: ProductListUiState.HasData,
            override val shipmentInfoUiState: ShipmentInfoUiState.HasData,
            override val pgRecommendationWidgetUiState: PGRecommendationWidgetUiState.HasData,
            override val orderResolutionUiState: OrderResolutionTicketStatusUiState,
            override val orderInsuranceUiState: OrderInsuranceUiState,
            override val epharmacyInfoUiState: EpharmacyInfoUiState.HasData,
            override val scpRewardsMedalTouchPointWidgetUiState: ScpRewardsMedalTouchPointWidgetUiState.HasData,
            override val savingsWidgetUiState: SavingsWidgetUiState,
            override val brcCsatUiState: WidgetBrcCsatUiState
        ) : HasData

        data class Showing(
            override val actionButtonsUiState: ActionButtonsUiState.HasData,
            override val orderStatusUiState: OrderStatusUiState.HasData,
            override val paymentInfoUiState: PaymentInfoUiState.HasData,
            override val productListUiState: ProductListUiState.HasData,
            override val shipmentInfoUiState: ShipmentInfoUiState.HasData,
            override val pgRecommendationWidgetUiState: PGRecommendationWidgetUiState.HasData,
            override val orderResolutionUiState: OrderResolutionTicketStatusUiState,
            override val orderInsuranceUiState: OrderInsuranceUiState,
            override val epharmacyInfoUiState: EpharmacyInfoUiState.HasData,
            override val scpRewardsMedalTouchPointWidgetUiState: ScpRewardsMedalTouchPointWidgetUiState.HasData,
            override val savingsWidgetUiState: SavingsWidgetUiState,
            override val brcCsatUiState: WidgetBrcCsatUiState
        ) : HasData
    }

    data class Error(val throwable: Throwable?) : BuyerOrderDetailUiState
}
