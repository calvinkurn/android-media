package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero

data class ProductListUiModel(
    val productList: List<ProductUiModel>,
    val productBundlingList: List<ProductBundlingUiModel>,
    val productListHeaderUiModel: ProductListHeaderUiModel,
    val addonsListUiModel: AddonsListUiModel?,
    val productListToggleUiModel: ProductListToggleUiModel?
) {
    data class ProductListHeaderUiModel(
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

        override fun shouldShow(context: Context?): Boolean {
            return true
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
        val isProcessing: Boolean = false,
        val addonsListUiModel: AddonsListUiModel? = null,
        val insurance: Insurance? = null
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(context: Context?): Boolean {
            return true
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }

        data class Insurance(
            val logoUrl: String,
            val label: String
        )
    }

    data class ProductBundlingUiModel(
        val bundleId: String,
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

    data class ProductListToggleUiModel(
        val collapsed: Boolean,
        val text: StringRes
    ) : BaseVisitableUiModel {
        override fun shouldShow(context: Context?): Boolean {
            return true
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }

        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }
}
