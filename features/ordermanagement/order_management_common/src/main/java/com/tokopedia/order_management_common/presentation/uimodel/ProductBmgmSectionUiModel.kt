package com.tokopedia.order_management_common.presentation.uimodel


abstract class ProductBmgmSectionUiModel(
    open val bmgmId: String,
    open val bmgmName: String,
    open val bmgmIconUrl: String,
    open val totalPrice: Double,
    open val totalPriceText: String,
    open val totalPriceReductionInfoText: String,
    open val bmgmItemList: List<ProductUiModel>
) {

    data class ProductUiModel(
        val orderId: String,
        val categoryId: String,
        val category: String,
        val orderDetailId: String,
        val productId: String,
        val productName: String,
        val thumbnailUrl: String,
        val productPriceText: String,
        val price: Double,
        val totalPrice: Double = 0.0,
        val totalPriceText: String = "",
        val quantity: Int,
        val productNote: String,
        val addOnSummaryUiModel: AddOnSummaryUiModel? = null,
        val button: ActionButtonsUiModel.ActionButton? = null,
    )
}
