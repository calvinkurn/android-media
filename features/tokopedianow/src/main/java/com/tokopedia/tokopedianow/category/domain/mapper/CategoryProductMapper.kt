package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.common.domain.mapper.AddToCartMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.common.domain.mapper.AddToCartMapper.removeProductAtcQuantity
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView

object CategoryProductMapper {

    private const val ADDITIONAL_POSITION = 1
    private const val SHOP_TYPE_GOLD = "gold"
    private const val SHOP_TYPE_OS = "os"
    private const val SHOP_TYPE_PM = "pm"

    private fun mapAceSearchProductToProductCard(
        product: AceSearchProductModel.Product,
        miniCart: MiniCartSimplifiedData?,
        hasBlockedAddToCard: Boolean
    ): ProductCardCompactUiModel = ProductCardCompactUiModel(
        productId = product.id,
        imageUrl = product.imageUrl300,
        minOrder = product.minOrder,
        maxOrder = product.maxOrder,
        availableStock = product.stock,
        orderQuantity = miniCart.getAddToCartQuantity(product.id),
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
        hasBlockedAddToCart = hasBlockedAddToCard,
        warehouseId = product.warehouseIdDefault
    )

    fun mapResponseToProductItem(
        index: Int,
        product: AceSearchProductModel.Product,
        miniCart: MiniCartSimplifiedData?,
        hasBlockedAddToCard: Boolean
    ): ProductItemDataView = ProductItemDataView(
        parentId = product.parentId,
        boosterList = product.boosterList,
        sourceEngine = product.sourceEngine,
        shop = ProductItemDataView.Shop(
            id = product.shop.id,
            name = product.shop.name
        ),
        shopType = getShopType(product.shop),
        categoryBreadcrumbs = product.categoryBreadcrumb,
        position = index + ADDITIONAL_POSITION,
        productCardModel = mapAceSearchProductToProductCard(product, miniCart, hasBlockedAddToCard),
    )

    fun MutableList<Visitable<*>>.updateProductCardItems(
        cartProductIds: List<String>,
        miniCartData: MiniCartSimplifiedData,
        hasBlockedAddToCart: Boolean
    ) {
        filterIsInstance<ProductItemDataView>().forEach { product ->
            val productCardModel = product.productCardModel
            val productId = productCardModel.productId
            val index = indexOf(product)

            if (productId in cartProductIds) {
                // Update product card add to cart quantity
                val updatedProduct = product.updateProductCardModel(
                    orderQuantity = miniCartData.getAddToCartQuantity(productId),
                    hasBlockedAddToCart = hasBlockedAddToCart
                )
                set(index, updatedProduct)
            } else {
                // Remove product card add to cart quantity
                removeProductAtcQuantity(productId, product.parentId, miniCartData) { _, quantity ->
                    val updatedProduct = product.updateProductCardModel(
                        orderQuantity = quantity,
                        hasBlockedAddToCart = hasBlockedAddToCart
                    )
                    set(index, updatedProduct)
                }
            }
        }
    }

    fun MutableList<Visitable<*>>.updateProductCardItems(
        productId: String,
        quantity: Int,
        hasBlockedAddToCart: Boolean
    ) {
        filterIsInstance<ProductItemDataView>().find {
            it.productCardModel.productId == productId
        }?.let { product ->
            val index = indexOf(product)
            val updatedProduct = product.updateProductCardModel(
                orderQuantity = quantity,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
            set(index, updatedProduct)
        }
    }

    private fun ProductItemDataView.updateProductCardModel(
        orderQuantity: Int,
        hasBlockedAddToCart: Boolean
    ) = copy(
        productCardModel = productCardModel.copy(
            orderQuantity = orderQuantity,
            hasBlockedAddToCart = hasBlockedAddToCart
        )
    )

    private fun getShopType(shop: AceSearchProductModel.ProductShop): String {
        return if (shop.isOfficial) {
            SHOP_TYPE_OS
        } else if (shop.isPowerBadge) {
            SHOP_TYPE_PM
        } else {
            SHOP_TYPE_GOLD
        }
    }
}
