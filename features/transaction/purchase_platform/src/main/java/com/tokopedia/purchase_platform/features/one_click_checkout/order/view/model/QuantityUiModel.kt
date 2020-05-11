package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

data class QuantityUiModel(
        var stockWording: String = "",
        var maxOrderQuantity: Int = 0,
        var minOrderQuantity: Int = 0,
        var orderQuantity: Int = 0,
        var errorFieldBetween: String = "",
        var errorFieldMaxChar: String = "",
        var errorFieldRequired: String = "",
        var errorProductAvailableStock: String = "",
        var errorProductAvailableStockDetail: String = "",
        var errorProductMaxQuantity: String = "",
        var errorProductMinQuantity: String = "",
        var isStateError: Boolean = false,
        var stockFromWarehouse: Int = 0,
        var stockWordingFromWarehouse: String = ""
)