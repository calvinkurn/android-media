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
        val orderDetailId: String,
        val productId: Long,
        val productName: String,
        val productPrice: String,
        val productPriceTotal: String,
        val quantity: Int,
        val productSkuId: String,
        val productWeight: String,
        val productNotes: String,
        val productCashbackLabel: String,
        val productThumbnailUrl: String,
        val totalPriceText: String,
        val totalQty: Int,
        val addOnSummaryUiModel: AddOnSummaryUiModel? = null,
    )
}
