package com.tokopedia.tokopedianow.recentpurchase.domain.mapper

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.*
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductUiModel

object RepurchaseProductMapper {

    fun List<RepurchaseProduct>.mapToProductListUiModel() = map {
        RepurchaseProductUiModel(it.id, createProductCardModel(it))
    }

    private fun createProductCardModel(product: RepurchaseProduct): ProductCardModel {
        return if (product.isVariant()) {
            ProductCardModel(
                productImageUrl = product.imageUrl,
                productName = product.name,
                discountPercentage = product.getDiscount(),
                slashedPrice = product.slashedPrice,
                formattedPrice = product.price,
                hasAddToCartButton = false,
                labelGroupList = mapLabelGroup(product),
                labelGroupVariantList = mapLabelGroupVariant(product),
                variant = Variant()
            )
        } else {
            ProductCardModel(
                productImageUrl = product.imageUrl,
                productName = product.name,
                discountPercentage = product.getDiscount(),
                slashedPrice = product.slashedPrice,
                formattedPrice = product.price,
                hasAddToCartButton = true,
                labelGroupList = mapLabelGroup(product),
                labelGroupVariantList = mapLabelGroupVariant(product),
                nonVariant = NonVariant(
                    minQuantity = product.minOrder.toInt(),
                    maxQuantity = product.stock.toInt()
                )
            )
        }
    }

    private fun mapLabelGroup(product: RepurchaseProduct): List<LabelGroup> {
        return product.labelGroup.map {
            LabelGroup(it.position, it.title, it.type, it.url)
        }
    }

    private fun mapLabelGroupVariant(product: RepurchaseProduct): List<LabelGroupVariant> {
        return product.labelGroupVariant.map {
            LabelGroupVariant(it.typeVariant, it.title, it.type, it.hexColor)
        }
    }
}
