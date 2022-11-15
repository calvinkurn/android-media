package com.tokopedia.tokopedianow.repurchase.domain.mapper

import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

object RepurchaseProductMapper {
    private const val DEFAULT_MAX_ORDER = 0

    private fun mapRepurchaseProductToProductCard(
        product: RepurchaseProduct
    ): TokoNowProductCardViewUiModel = TokoNowProductCardViewUiModel(
        productId = product.id,
        imageUrl = product.imageUrl,
        minOrder = product.minOrder,
        maxOrder = product.maxOrder,
        availableStock = product.stock,
        price = product.price,
        discount = product.discountPercentage,
        slashPrice = product.slashedPrice,
        name = product.name,
        rating = product.ratingAverage,
        hasBeenWishlist = false,
        isWishlistShown = false,
        isSimilarProductShown = true,
        isVariant = product.parentProductId.isNotBlank() && product.parentProductId != "0",
        needToShowQuantityEditor = product.minOrder <= product.maxOrder && product.maxOrder != DEFAULT_MAX_ORDER,
        labelGroupList = product.labelGroup.map {
            com.tokopedia.tokopedianow.common.model.LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        }
    )

    fun List<RepurchaseProduct>.mapToProductListUiModel() = mapIndexed { index, repurchaseProduct ->
        RepurchaseProductUiModel(
            id = repurchaseProduct.id,
            parentId = repurchaseProduct.parentProductId,
            shopId = repurchaseProduct.shop.id,
            categoryId = repurchaseProduct.categoryId,
            category = repurchaseProduct.category,
            isStockEmpty = repurchaseProduct.isStockEmpty(),
            position = index + ADDITIONAL_POSITION,
            productCardModel = mapRepurchaseProductToProductCard(repurchaseProduct)
        )
    }
}
