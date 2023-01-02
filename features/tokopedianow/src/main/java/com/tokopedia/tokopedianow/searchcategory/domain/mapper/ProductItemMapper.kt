package com.tokopedia.tokopedianow.searchcategory.domain.mapper

import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView

object ProductItemMapper {
    private const val DEFAULT_MAX_ORDER = 0
    private const val ADDITIONAL_POSITION = 1

    private fun mapAceSearchProductToProductCard(
        product: AceSearchProductModel.Product,
        cartService: CartService,
    ): TokoNowProductCardViewUiModel = TokoNowProductCardViewUiModel(
        productId = product.id,
        imageUrl = product.imageUrl300,
        minOrder = product.minOrder,
        maxOrder = product.maxOrder,
        availableStock = product.stock,
        orderQuantity = cartService.getProductQuantity(product.id, product.parentId),
        price = product.price,
        discountInt = product.discountPercentage,
        slashPrice = product.originalPrice,
        name = product.name,
        rating = product.ratingAverage,
        hasBeenWishlist = product.isWishlist,
        isWishlistShown = true,
        isSimilarProductShown = true,
        isVariant = product.childs.isNotEmpty(),
        needToShowQuantityEditor = product.minOrder <= product.maxOrder && product.maxOrder != DEFAULT_MAX_ORDER,
        labelGroupList = product.labelGroupList.map {
            LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        }
    )

    fun mapResponseToProductItem(
        index: Int,
        product: AceSearchProductModel.Product,
        cartService: CartService
    ): ProductItemDataView = ProductItemDataView(
        parentId = product.parentId,
        boosterList = product.boosterList,
        sourceEngine = product.sourceEngine,
        shop = ProductItemDataView.Shop(
            id = product.shop.id,
            name = product.shop.name,
        ),
        position = index + ADDITIONAL_POSITION,
        productCardModel = mapAceSearchProductToProductCard(
            product = product,
            cartService = cartService
        )
    )
}
