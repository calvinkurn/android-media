package com.tokopedia.tokopedianow.repurchase.domain.mapper

import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

object RepurchaseProductMapper {
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
        hasBeenWishlist = product.wishlist,
        isWishlistShown = true,
        isSimilarProductShown = true,
        isVariant = product.parentProductId.isNotBlank() && product.parentProductId != "0",
        needToShowQuantityEditor = true,
        labelGroupList = product.labelGroup.map {
            LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        }
    )

    fun List<RepurchaseProduct>.mapToProductListUiModel() = mapIndexed { index, repurchaseProduct ->
        RepurchaseProductUiModel(
            parentId = repurchaseProduct.parentProductId,
            shopId = repurchaseProduct.shop.id,
            categoryId = repurchaseProduct.categoryId,
            category = repurchaseProduct.category,
            position = index + ADDITIONAL_POSITION,
            productCardModel = mapRepurchaseProductToProductCard(repurchaseProduct)
        )
    }
}
