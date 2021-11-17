package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero

data class ProductListUiModel(
        val productList: List<ProductUiModel>,
        val productBundlingList: List<ProductBundlingUiModel>,
        val productListHeaderUiModel: ProductListHeaderUiModel
) {
    data class ProductListHeaderUiModel(
            val header: String,
            val shopBadgeUrl: String,
            val shopId: String,
            val shopName: String,
            val shopType: Int,
            val orderId: String,
            val orderStatusId: String
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(): Boolean {
            return header.isNotBlank()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }

    data class ProductUiModel(
            val button: ActionButtonsUiModel.ActionButton,
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
            val isProcessing: Boolean = false
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(): Boolean {
            return true
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }

    data class ProductBundlingUiModel(
            val bundleName: String,
            val bundleIconUrl: String,
            val totalPrice: Double,
            val totalPriceText: String,
            val bundleItemList: List<ProductUiModel>
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

}