package com.tokopedia.buyerorderdetail.presentation.model

data class BuyerOrderDetailUiModel(
        val actionButtonsUiModel: ActionButtonsUiModel,
        val orderStatusUiModel: OrderStatusUiModel,
        val paymentInfoUiModel: PaymentInfoUiModel,
        val productListUiModel: ProductListUiModel,
        val shipmentInfoUiModel: ShipmentInfoUiModel
)