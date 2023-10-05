package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl

data class OwocProductListUiModel(
    val productListHeaderUiModel: ProductListHeaderUiModel,
    val productList: List<ProductListUiModel.ProductUiModel>,
    val productBundlingList: List<ProductListUiModel.ProductBundlingUiModel>,
    val addonsListUiModel: AddonsListUiModel?,
    val productListToggleUiModel: ProductListUiModel.ProductListToggleUiModel?,
) {

    data class ProductListHeaderUiModel(
        val shopBadgeUrl: String,
        val currentShopId: String,
        val shopName: String,
        val invoiceNumber: String,
        val orderId: String,
        val owocActionButtonUiModel: OwocActionButtonUiModel
    ) : BaseOwocVisitableUiModel {

        data class OwocActionButtonUiModel(
            val key: String,
            val displayName: String,
            val variant: String,
            val type: String,
            val url: String
        )

        override fun shouldShow(context: Context?): Boolean {
            return shopName.isNotBlank()
        }

        override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }

    data class ProductUiModel(
        val orderDetailId: String,
        val orderId: String,
        val priceText: String,
        val productId: String,
        val productName: String,
        val productThumbnailUrl: String,
        val quantity: Int,
        val addonsListUiModel: OwocAddonsListUiModel? = null,
        val isPof: Boolean = false
    ) : BaseOwocVisitableUiModel {

        override fun shouldShow(context: Context?): Boolean {
            return productName.isNotBlank()
        }

        override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }

    data class ProductBundlingUiModel(
        val bundleId: String,
        val bundleName: String,
        val bundleIconUrl: String,
        val bundleItemList: List<ProductUiModel>
    ) : BaseOwocVisitableUiModel {

        override fun shouldShow(context: Context?): Boolean {
            return bundleItemList.isNotEmpty()
        }

        override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }

    data class ProductListToggleUiModel(
        val isExpanded: Boolean,
        val remainingProductList: List<BaseOwocVisitableUiModel>
    ) : BaseOwocVisitableUiModel {

        override fun shouldShow(context: Context?): Boolean {
            return remainingProductList.isNotEmpty()
        }

        override fun type(typeFactory: OwocProductListTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }
}
