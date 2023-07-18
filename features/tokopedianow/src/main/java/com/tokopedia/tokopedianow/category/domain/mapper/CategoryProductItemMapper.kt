package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView

object CategoryProductItemMapper {

    private const val ADDITIONAL_POSITION = 1

    private fun mapAceSearchProductToProductCard(
        product: AceSearchProductModel.Product
    ): ProductCardCompactUiModel = ProductCardCompactUiModel(
        productId = product.id,
        imageUrl = product.imageUrl300,
        minOrder = product.minOrder,
        maxOrder = product.maxOrder,
        availableStock = product.stock,
        orderQuantity = 0,
        price = product.price,
        discountInt = product.discountPercentage,
        slashPrice = product.originalPrice,
        name = product.name,
        rating = product.ratingAverage,
        hasBeenWishlist = product.isWishlist,
        isWishlistShown = true,
        isSimilarProductShown = true,
        isVariant = product.childs.isNotEmpty(),
        needToShowQuantityEditor = true,
        labelGroupList = product.labelGroupList.map {
            ProductCardCompactUiModel.LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        },
        hasBlockedAddToCart = false
    )

    fun mapResponseToProductItem(
        index: Int,
        product: AceSearchProductModel.Product,
    ): ProductItemDataView = ProductItemDataView(
        parentId = product.parentId,
        boosterList = product.boosterList,
        sourceEngine = product.sourceEngine,
        shop = ProductItemDataView.Shop(
            id = product.shop.id,
            name = product.shop.name,
        ),
        position = index + ADDITIONAL_POSITION,
        productCardModel = mapAceSearchProductToProductCard(product)
    )
}
