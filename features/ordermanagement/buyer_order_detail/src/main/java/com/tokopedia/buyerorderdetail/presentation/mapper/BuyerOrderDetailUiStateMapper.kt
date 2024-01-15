package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.EpharmacyInfoUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderInsuranceUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.SavingsWidgetUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ScpRewardsMedalTouchPointWidgetUiState
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
        orderInsuranceUiState: OrderInsuranceUiState,
        epharmacyInfoUiState: EpharmacyInfoUiState,
        scpRewardsMedalTouchPointWidgetUiState: ScpRewardsMedalTouchPointWidgetUiState,
        savingsWidgetUiState: SavingsWidgetUiState
    ): BuyerOrderDetailUiState {
        logBreadcrumb(
            actionButtonsUiState,
            orderStatusUiState,
            paymentInfoUiState,
            productListUiState,
            shipmentInfoUiState,
            pgRecommendationWidgetUiState,
            orderResolutionTicketStatusUiState,
            orderInsuranceUiState,
            epharmacyInfoUiState,
            scpRewardsMedalTouchPointWidgetUiState,
            savingsWidgetUiState
        )
        return if (
            actionButtonsUiState is ActionButtonsUiState.HasData &&
            orderStatusUiState is OrderStatusUiState.HasData &&
            paymentInfoUiState is PaymentInfoUiState.HasData &&
            productListUiState is ProductListUiState.HasData &&
            shipmentInfoUiState is ShipmentInfoUiState.HasData &&
            pgRecommendationWidgetUiState is PGRecommendationWidgetUiState.HasData &&
            epharmacyInfoUiState is EpharmacyInfoUiState.HasData &&
            orderResolutionTicketStatusUiState !is OrderResolutionTicketStatusUiState.Loading &&
            scpRewardsMedalTouchPointWidgetUiState is ScpRewardsMedalTouchPointWidgetUiState.HasData
        ) {
            if (
                actionButtonsUiState is ActionButtonsUiState.HasData.Reloading ||
                orderStatusUiState is OrderStatusUiState.HasData.Reloading ||
                paymentInfoUiState is PaymentInfoUiState.HasData.Reloading ||
                productListUiState is ProductListUiState.HasData.Reloading ||
                shipmentInfoUiState is ShipmentInfoUiState.HasData.Reloading ||
                pgRecommendationWidgetUiState is PGRecommendationWidgetUiState.HasData.Reloading ||
                orderResolutionTicketStatusUiState is OrderResolutionTicketStatusUiState.HasData.Reloading ||
                orderInsuranceUiState is OrderInsuranceUiState.HasData.Reloading ||
                epharmacyInfoUiState is EpharmacyInfoUiState.HasData.Reloading
            ) {
                BuyerOrderDetailUiState.HasData.PullRefreshLoading(
                    actionButtonsUiState,
                    orderStatusUiState,
                    paymentInfoUiState,
                    productListUiState,
                    shipmentInfoUiState,
                    pgRecommendationWidgetUiState,
                    orderResolutionTicketStatusUiState,
                    orderInsuranceUiState,
                    epharmacyInfoUiState,
                    scpRewardsMedalTouchPointWidgetUiState,
                    savingsWidgetUiState
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
                    orderInsuranceUiState,
                    epharmacyInfoUiState,
                    scpRewardsMedalTouchPointWidgetUiState,
                    savingsWidgetUiState
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
        } else if (epharmacyInfoUiState is EpharmacyInfoUiState.Error) {
            BuyerOrderDetailUiState.Error(epharmacyInfoUiState.throwable)
        } else if (pgRecommendationWidgetUiState is PGRecommendationWidgetUiState.Error) {
            BuyerOrderDetailUiState.Error(pgRecommendationWidgetUiState.throwable)
        } else {
            BuyerOrderDetailUiState.FullscreenLoading
        }.logBreadcrumb()
    }

    private fun logBreadcrumb(
        actionButtonsUiState: ActionButtonsUiState,
        orderStatusUiState: OrderStatusUiState,
        paymentInfoUiState: PaymentInfoUiState,
        productListUiState: ProductListUiState,
        shipmentInfoUiState: ShipmentInfoUiState,
        pgRecommendationWidgetUiState: PGRecommendationWidgetUiState,
        orderResolutionTicketStatusUiState: OrderResolutionTicketStatusUiState,
        orderInsuranceUiState: OrderInsuranceUiState,
        epharmacyInfoUiState: EpharmacyInfoUiState,
        scpRewardsMedalTouchPointWidgetUiState: ScpRewardsMedalTouchPointWidgetUiState,
        savingsWidgetUiState: SavingsWidgetUiState
    ) {
        runCatching {
            EmbraceMonitoring.logBreadcrumb("Mapping UI states into BOM Ui State")
            EmbraceMonitoring.logBreadcrumb(actionButtonsUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(orderStatusUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(paymentInfoUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(productListUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(shipmentInfoUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(pgRecommendationWidgetUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(orderResolutionTicketStatusUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(orderInsuranceUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(epharmacyInfoUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(scpRewardsMedalTouchPointWidgetUiState::class.java.name)
            EmbraceMonitoring.logBreadcrumb(savingsWidgetUiState::class.java.name)
        }
    }

    private fun BuyerOrderDetailUiState.logBreadcrumb() = also {
        runCatching { EmbraceMonitoring.logBreadcrumb("Finish mapping UI states into ${this::class.java.name}") }
    }
}
