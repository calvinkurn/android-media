package com.tokopedia.buyerorderdetail.presentation.model

data class BuyerOrderDetailUiModel(
        val actionButtons: ActionButtonsUiModel,
        val buyProtectionUiModel: BuyProtectionUiModel,
        val shipmentInfoUiModel: ShipmentInfoUiModel,
        val paymentInfoItems: PaymentInfoUiModel,
        val productListUiModel: ProductListUiModel,
        val orderStatusUiModel: OrderStatusUiModel
)