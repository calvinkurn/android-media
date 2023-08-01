package com.tokopedia.order_management_common.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.order_management_common.presentation.factory.BmgmAdapterTypeFactory

data class ProductBmgmSectionUiModel(
    val bmgmId: String,
    val bmgmName: String,
    val bmgmIconUrl: String,
    val totalPrice: Double,
    val totalPriceText: String,
    val totalPriceReductionInfoText: String,
    val bmgmItemList: List<ProductUiModel>
) : Visitable<BmgmAdapterTypeFactory> {
    override fun type(typeFactory: BmgmAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class ProductUiModel(
        val category: String,
        val categoryId: String,
        val orderDetailId: String,
        val orderId: String,
        val orderStatusId: String,
        val price: Double,
        val priceText: String,
        val productId: String,
        val productName: String,
        val productNote: String,
        val productThumbnailUrl: String,
        val quantity: Int,
        val totalPrice: String,
        val totalPriceText: String,
        val addOnSummaryUiModel: AddOnSummaryUiModel? = null,
    )
}
