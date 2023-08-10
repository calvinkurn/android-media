package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel

data class ProductListUiModel(
    val productList: List<ProductUiModel>,
    val productBmgmList: List<ProductBmgmUiModel>,
    val productBundlingList: List<ProductBundlingUiModel>,
    val productUnFulfilledList: List<ProductUiModel>?,
    val productFulfilledHeaderLabel: ProductPofHeaderLabelUiModel?,
    val productUnfulfilledHeaderLabel: ProductPofHeaderLabelUiModel?,
    val productListHeaderUiModel: ProductListHeaderUiModel,
    val addonsListUiModel: AddonsListUiModel?,
    val productListToggleUiModel: ProductListToggleUiModel?,
    val tickerInfo: TickerUiModel?
) {

    fun getAllProduct(): List<ProductUiModel> {
        return productList.plus(productBundlingList.map { it.bundleItemList }.flatten())
    }

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
        val insurance: Insurance? = null,
        val isPof: Boolean = false
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

    data class ProductBmgmUiModel(
        override val bmgmId: String,
        override val bmgmName: String,
        override val bmgmIconUrl: String,
        override val totalPrice: Double,
        override val totalPriceText: String,
        override val totalPriceReductionInfoText: String,
        override val bmgmItemList: List<ProductUiModel>
    ) : ProductBmgmSectionUiModel(
        bmgmId,
        bmgmName,
        bmgmIconUrl,
        totalPrice,
        totalPriceText,
        totalPriceReductionInfoText,
        bmgmItemList
    ), Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
            return typeFactory.type(this)
        }
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

    data class ProductPofHeaderLabelUiModel(
        val title: String,
        val quantity: String,
        val isUnfulfilled: Boolean
    ) : BaseVisitableUiModel {
        override fun shouldShow(context: Context?): Boolean {
            return title.isNotBlank() || quantity.isNotBlank()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }

        override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
            return typeFactory.type(this)
        }
    }
}
