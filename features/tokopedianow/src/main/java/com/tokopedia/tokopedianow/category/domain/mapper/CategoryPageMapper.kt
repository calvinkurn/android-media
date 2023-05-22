package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.domain.mapper.MiniCartMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import kotlin.math.min

internal object CategoryPageMapper {

    private const val MAX_SHOWCASE_PRODUCT = 6

    private fun mapAceSearchProductToProductCard(
        product: AceSearchProductModel.Product,
        miniCartData:  MiniCartSimplifiedData?
    ): ProductCardCompactUiModel = ProductCardCompactUiModel(
        productId = product.id,
        imageUrl = product.imageUrl300,
        minOrder = product.minOrder,
        maxOrder = product.maxOrder,
        availableStock = product.stock,
        orderQuantity = getAddToCartQuantity(
            productId = product.id,
            miniCartData = miniCartData
        ),
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

    fun AceSearchProductModel.mapToShowcaseProductCard(
        categoryIdL2: String,
        title: String,
        seeAllAppLink: String,
        miniCartData: MiniCartSimplifiedData?,
        @TokoNowLayoutState state: Int
    ): CategoryShowcaseUiModel = CategoryShowcaseUiModel(
        id = categoryIdL2,
        productListUiModels = this.searchProduct.data.productList.take(MAX_SHOWCASE_PRODUCT).map {
            CategoryShowcaseItemUiModel(
                productCardModel = mapAceSearchProductToProductCard(
                    product = it,
                    miniCartData = miniCartData
                ),
                parentProductId = it.parentId
            )
        },
        title = title,
        seeAllAppLink = seeAllAppLink,
        state = state
    )
}
