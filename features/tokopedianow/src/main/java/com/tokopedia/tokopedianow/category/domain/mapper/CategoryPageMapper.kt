package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.domain.mapper.MiniCartMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

internal object CategoryPageMapper {

    private const val MAX_SHOWCASE_PRODUCT = 6

    private fun mapAceSearchProductToProductCard(
        product: AceSearchProductModel.Product,
        miniCartData:  MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ): ProductCardCompactUiModel = ProductCardCompactUiModel(
        productId = product.id,
        imageUrl = product.imageUrl300,
        minOrder = product.minOrder,
        maxOrder = product.maxOrder,
        availableStock = product.stock,
        orderQuantity = miniCartData.getAddToCartQuantity(
            productId = product.id
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
        },
        hasBlockedAddToCart = hasBlockedAddToCart
    )

    fun mapToShowcaseProductCard(
        totalData: Int,
        productList: List<AceSearchProductModel.Product> = listOf(),
        categoryIdL2: String,
        title: String,
        seeAllAppLink: String,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean,
        @TokoNowLayoutState state: Int
    ): CategoryShowcaseUiModel = CategoryShowcaseUiModel(
        id = categoryIdL2,
        productListUiModels = productList.take(MAX_SHOWCASE_PRODUCT).mapIndexed { index, product ->
            CategoryShowcaseItemUiModel(
                index = index,
                productCardModel = mapAceSearchProductToProductCard(
                    product = product,
                    miniCartData = miniCartData,
                    hasBlockedAddToCart = hasBlockedAddToCart
                ),
                parentProductId = product.parentId,
                headerName = title,
                shopId = product.shop.id
            )
        },
        title = title,
        seeAllAppLink = if (totalData > MAX_SHOWCASE_PRODUCT) seeAllAppLink else String.EMPTY,
        state = state
    )
}
