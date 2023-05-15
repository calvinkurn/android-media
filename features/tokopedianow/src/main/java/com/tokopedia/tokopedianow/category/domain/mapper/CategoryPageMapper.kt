package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.category.domain.response.CategoryModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

internal object CategoryPageMapper {
    fun CategoryModel.mapToShowcaseProductCard(): CategoryShowcaseUiModel = CategoryShowcaseUiModel(
        categoryListUiModel = this.searchProduct.data.productList.map {
            CategoryShowcaseItemUiModel(
                productCardModel = mapAceSearchProductToProductCard(
                    product = it
                )
            )
        }
    )

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
        }
    )
}
