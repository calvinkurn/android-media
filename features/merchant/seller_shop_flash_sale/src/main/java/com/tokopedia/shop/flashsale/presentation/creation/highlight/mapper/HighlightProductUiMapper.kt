package com.tokopedia.shop.flashsale.presentation.creation.highlight.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import javax.inject.Inject

class HighlightProductUiMapper @Inject constructor() {

    companion object {
        private const val OFFSET_BY_ONE = 1
    }

    fun map(
        products: SellerCampaignProductList
    ): List<HighlightableProduct> {
        return products.productList
            .mapIndexed { index, product ->
                HighlightableProduct(
                    product.productId.toLongOrZero(),
                    product.parentId.toLongOrZero(),
                    product.productName,
                    product.imageUrl.img200,
                    product.productMapData.originalPrice,
                    product.productMapData.discountedPrice,
                    product.productMapData.discountPercentage,
                    product.productMapData.customStock,
                    product.toWarehouse(),
                    product.productMapData.maxOrder,
                    disabled = false,
                    isSelected = false,
                    index + OFFSET_BY_ONE,
                    HighlightableProduct.DisabledReason.NOT_DISABLED,
                    product.highlightProductWording
                )
            }
    }

    private fun SellerCampaignProductList.Product.toWarehouse(): List<HighlightableProduct.Warehouse> {
        return this.warehouseList.map {
            HighlightableProduct.Warehouse(
                it.warehouseId.toLongOrZero(),
                it.stock.toLong(),
                it.chosenWarehouse
            )
        }
    }
}