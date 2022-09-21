package com.tokopedia.tokopedianow.repurchase.domain.mapper

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.*
import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

object RepurchaseProductMapper {

    fun List<RepurchaseProduct>.mapToProductListUiModel() = mapIndexed { index, repurchaseProduct ->
        RepurchaseProductUiModel(
            repurchaseProduct.id,
            repurchaseProduct.parentProductId,
            repurchaseProduct.shop.id,
            repurchaseProduct.categoryId,
            repurchaseProduct.category,
            repurchaseProduct.isStockEmpty(),
            createProductCardModel(repurchaseProduct),
            index + ADDITIONAL_POSITION
        )
    }

    private fun createProductCardModel(product: RepurchaseProduct): ProductCardModel {
        return if (product.isVariant()) {
            ProductCardModel(
                productImageUrl = product.imageUrl,
                productName = product.name,
                discountPercentage = product.getDiscount(),
                slashedPrice = product.slashedPrice,
                formattedPrice = product.price,
                hasSimilarProductButton = product.isStockEmpty(),
                labelGroupList = mapLabelGroup(product),
                labelGroupVariantList = mapLabelGroupVariant(product),
                variant = Variant(),
                isOutOfStock = product.isStockEmpty(),
                countSoldRating = product.ratingAverage
            )
        } else {
            ProductCardModel(
                productImageUrl = product.imageUrl,
                productName = product.name,
                discountPercentage = product.getDiscount(),
                slashedPrice = product.slashedPrice,
                formattedPrice = product.price,
                hasSimilarProductButton = product.isStockEmpty(),
                labelGroupList = mapLabelGroup(product),
                labelGroupVariantList = mapLabelGroupVariant(product),
                nonVariant = NonVariant(
                    minQuantity = product.minOrder,
                    maxQuantity = product.maxOrder
                ),
                isOutOfStock = product.isStockEmpty(),
                countSoldRating = product.ratingAverage
            )
        }.run {
            if(product.isStockEmpty()) {
                copy(variant = null, nonVariant = null)
            } else {
                this
            }
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
