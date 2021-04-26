package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class ProductListUiModel(
        val productListHeaderUiModel: ProductListHeaderUiModel,
        val productList: List<ProductUiModel>
) {
    data class ProductListHeaderUiModel(
            val header: String,
            val shopBadge: Int,
            val shopName: String,
            val shopId: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class ProductUiModel(
            val productThumbnailUrl: String,
            val productName: String,
            val productQuantityAndPrice: String,
            val productNote: String,
            val totalPrice: String,
            val showBuyAgainButton: Boolean,
            val showClaimInsurance: Boolean,
            val orderId: String,
            val orderDetailId: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }
}