package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocTypeFactoryImpl

data class OwocProductListUiModel(
    val productList: List<ProductListUiModel.ProductUiModel>,
    val productBundlingList: List<ProductListUiModel.ProductBundlingUiModel>,
    val productListHeaderUiModel: ProductListHeaderUiModel,
    val addonsListUiModel: AddonsListUiModel?,
    val productListToggleUiModel: ProductListUiModel.ProductListToggleUiModel?,
) {
    data class ProductListHeaderUiModel(
        val shopBadgeUrl: String,
        val fromShopId: String,
        val currentShopId: String,
        val shopName: String,
        val invoiceNumber: String,
        val shopType: Int,
        val orderId: String,
        val orderStatusId: String
    ) : BaseOwocVisitableUiModel {

        override fun shouldShow(context: Context?): Boolean {
            return shopName.isNotBlank()
        }

        override fun type(typeFactory: OwocTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }

    data class ProductUiModel(
        val orderDetailId: String,
        val orderId: String,
        val orderStatusId: String,
        val price: Double,
        val priceText: String,
        val productId: String,
        val productName: String,
        val productThumbnailUrl: String,
        val quantity: Int,
        val totalPrice: String,
        val totalPriceText: String,
        val addonsListUiModel: OwocAddonsListUiModel? = null,
        val isPof: Boolean = false
    ) : BaseOwocVisitableUiModel {

        override fun shouldShow(context: Context?): Boolean {
            return productName.isNotBlank()
        }

        override fun type(typeFactory: OwocTypeFactoryImpl): Int {
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
    ) : BaseOwocVisitableUiModel {

        override fun shouldShow(context: Context?): Boolean {
            return bundleItemList.isNotEmpty()
        }

        override fun type(typeFactory: OwocTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }

    data class ProductListToggleUiModel(
        val collapsed: Boolean,
        val text: StringRes,
        val shopId: String,
    ) : BaseOwocVisitableUiModel {

        override fun shouldShow(context: Context?): Boolean {
            return context?.getString(text.id)?.isNotBlank() == true
        }

        override fun type(typeFactory: OwocTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }
}
