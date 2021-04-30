package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class ProductListUiModel(
        val productList: List<ProductUiModel>,
        val productListHeaderUiModel: ProductListHeaderUiModel
) {
    data class ProductListHeaderUiModel(
            val header: String,
            val shopBadge: Int,
            val shopId: String,
            val shopName: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class ProductUiModel(
            val button: ActionButtonsUiModel.ActionButton,
            val orderDetailId: String,
            val price: String,
            val priceText: String,
            val productId: String,
            val productName: String,
            val productNote: String,
            val productThumbnailUrl: String,
            val quantity: Int,
            val showClaimInsurance: Boolean,
            val totalPrice: String,
            val totalPriceText: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }
}