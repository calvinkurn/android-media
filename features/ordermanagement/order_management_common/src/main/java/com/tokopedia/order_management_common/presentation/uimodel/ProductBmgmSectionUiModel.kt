package com.tokopedia.order_management_common.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.order_management_common.presentation.typefactory.BuyMoreGetMoreTypeFactory

data class ProductBmgmSectionUiModel(
    val bmgmId: String,
    val bmgmName: String,
    val bmgmIconUrl: String,
    val totalPrice: Double,
    val totalPriceText: String,
    val totalPriceReductionInfoText: String,
    val bmgmItemList: List<ProductUiModel>
) : Visitable<BuyMoreGetMoreTypeFactory> {

    override fun type(typeFactory: BuyMoreGetMoreTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class ProductUiModel(
        val orderId: String,
        val orderStatusId: String = String.EMPTY,
        val categoryId: String = String.EMPTY,
        val category: String = String.EMPTY,
        val orderDetailId: String,
        val productId: String = String.EMPTY,
        val productName: String,
        val thumbnailUrl: String,
        val productPriceText: String,
        val price: Double,
        val totalPrice: Double = 0.0,
        val totalPriceText: String = String.EMPTY,
        val quantity: Int,
        val productNote: String,
        val addOnSummaryUiModel: AddOnSummaryUiModel? = null,
        val button: ActionButtonsUiModel.ActionButton? = null
    )
}
