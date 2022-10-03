package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import java.io.Serializable

data class BuyerOrderDetailUiModel(
    val actionButtonsUiModel: ActionButtonsUiModel,
    val orderStatusUiModel: OrderStatusUiModel,
    val paymentInfoUiModel: PaymentInfoUiModel,
    val productListUiModel: ProductListUiModel,
    val shipmentInfoUiModel: ShipmentInfoUiModel,
    val pgRecommendationWidgetUiModel: PGRecommendationWidgetUiModel,
    val orderResolutionUiState: OrderResolutionTicketStatusUiState
) : Serializable
