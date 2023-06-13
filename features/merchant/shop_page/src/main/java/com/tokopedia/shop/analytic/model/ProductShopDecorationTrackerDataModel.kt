package com.tokopedia.shop.analytic.model

data class ProductShopDecorationTrackerDataModel(
    val shopId: String = "",
    val userId: String = "",
    val productName: String = "",
    val productId: String = "",
    val productDisplayedPrice: String = "",
    val verticalPosition: Int = 0,
    val horizontalPosition: Int = 0,
    val widgetName: String = "",
    val widgetOption: Int = 0,
    val widgetMasterId: String = "",
    val isFestivity: Boolean = false
)
