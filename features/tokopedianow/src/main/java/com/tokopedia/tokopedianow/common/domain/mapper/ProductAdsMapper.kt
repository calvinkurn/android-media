package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.domain.mapper.AddToCartMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.common.domain.mapper.AddToCartMapper.removeProductAtcQuantity
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.updateProductCarouselItem
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.Product
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.Shop
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper

object ProductAdsMapper {

    private const val DEFAULT_PARENT_PRODUCT_ID = "0"

    private const val SHOP_TYPE_GOLD = "gold"
    private const val SHOP_TYPE_OS = "os"
    private const val SHOP_TYPE_PM = "pm"

    private const val ADDITIONAL_POSITION = 1

    fun MutableList<Visitable<*>>.addProductAdsCarousel(
        id: String = PRODUCT_ADS_CAROUSEL
    ) {
        add(
            TokoNowAdsCarouselUiModel(
                id = id,
                items = emptyList(),
                state = TokoNowLayoutState.LOADING
            )
        )
    }

    fun mapProductAdsCarousel(
        id: String,
        response: ProductAdsResponse,
        miniCartData: MiniCartSimplifiedData?,
        hasBlockedAddToCart: Boolean
    ): TokoNowAdsCarouselUiModel {
        val productList = response.productList.mapIndexed { index, item ->
            val shop = item.shop
            val product = item.product
            ProductCardCompactCarouselItemUiModel(
                position = index + ADDITIONAL_POSITION,
                productCardModel = mapProductCardCompactUiModel(
                    product,
                    miniCartData,
                    hasBlockedAddToCart
                ),
                shopId = shop.id,
                shopName = shop.name,
                shopType = getShopType(shop),
                appLink = item.applinks,
                parentId = product.parentId,
                categoryBreadcrumbs = product.categoryBreadcrumb
            )
        }

        return TokoNowAdsCarouselUiModel(
            id = id,
            items = productList,
            state = TokoNowLayoutState.LOADED
        )
    }

    fun TokoNowAdsCarouselUiModel.updateProductAdsQuantity(
        productId: String,
        quantity: Int
    ): TokoNowAdsCarouselUiModel {
        val carouseList = items.toMutableList()
        val carouselItem = carouseList.firstOrNull { it.getProductId() == productId }
        val productUiModel = carouselItem?.productCardModel

        productUiModel?.apply {
            if (orderQuantity != quantity) {
                val index = items.indexOf(carouselItem)

                productUiModel.copy(orderQuantity = quantity).let {
                    items[index].copy(productCardModel = it)
                }.let {
                    carouseList[index] = it
                }
            }
        }

        return copy(items = carouseList)
    }

    fun MutableList<Visitable<*>>.findAdsProductCarousel(
        productId: String
    ): ProductCardCompactCarouselItemUiModel? {
        var product: ProductCardCompactCarouselItemUiModel? = null
        filterIsInstance<TokoNowAdsCarouselUiModel>().forEach { carousel ->
            carousel.items.firstOrNull { it.getProductId() == productId }?.let {
                product = it
            }
        }
        return product
    }

    fun MutableList<Visitable<*>>.updateProductAdsQuantity(
        cartProductIds: List<String>,
        miniCartData: MiniCartSimplifiedData,
        hasBlockedAddToCart: Boolean
    ) {
        filterIsInstance<TokoNowAdsCarouselUiModel>().forEach { adsCarousel ->
            updateItemById(adsCarousel.id) {
                val items = adsCarousel.items.map {
                    val productId = it.getProductId()
                    var updatedProductItem = it

                    if (productId in cartProductIds) {
                        val orderQuantity = miniCartData.getAddToCartQuantity(it.getProductId())
                        updatedProductItem = it.updateProductCarouselItem(
                            orderQuantity,
                            hasBlockedAddToCart
                        )
                    } else {
                        removeProductAtcQuantity(productId, it.parentId, miniCartData) { _, quantity ->
                            updatedProductItem = it.updateProductCarouselItem(
                                quantity,
                                hasBlockedAddToCart
                            )
                        }
                    }

                    updatedProductItem
                }
                adsCarousel.copy(items = items)
            }
        }
    }

    fun MutableList<Visitable<*>>.updateProductAdsQuantity(
        productId: String,
        quantity: Int,
        hasBlockedAddToCart: Boolean
    ) {
        filterIsInstance<TokoNowAdsCarouselUiModel>().forEach { adsCarousel ->
            updateItemById(adsCarousel.id) {
                val items = adsCarousel.items.toMutableList()
                val item = items.first { it.getProductId() == productId }
                val index = items.indexOf(item)
                items[index] = item.updateProductCarouselItem(
                    quantity,
                    hasBlockedAddToCart
                )
                adsCarousel.copy(items = items)
            }
        }
    }

    private fun ProductCardCompactCarouselItemUiModel.updateProductCarouselItem(
        orderQuantity: Int,
        hasBlockedAddToCart: Boolean
    ): ProductCardCompactCarouselItemUiModel {
        val productCard = productCardModel.copy(
            orderQuantity = orderQuantity,
            hasBlockedAddToCart = hasBlockedAddToCart
        )
        return copy(productCardModel = productCard)
    }

    private fun mapProductCardCompactUiModel(
        product: Product,
        miniCartData: MiniCartSimplifiedData? = null,
        hasBlockedAddToCart: Boolean
    ): ProductCardCompactUiModel = ProductCardCompactUiModel(
        productId = product.id,
        imageUrl = product.image.mEcs,
        minOrder = product.productMinimumOrder,
        maxOrder = product.maxOrder,
        availableStock = product.stock,
        orderQuantity = getAddToCartQuantity(product.id, miniCartData),
        price = product.priceFormat,
        discount = product.campaign.getDiscount(),
        slashPrice = product.campaign.originalPrice,
        name = product.name,
        rating = product.productRatingFormat,
        isVariant = product.parentId != DEFAULT_PARENT_PRODUCT_ID && product.parentId.isNotBlank(),
        needToShowQuantityEditor = true,
        labelGroupList = product.labelGroup.map {
            ProductCardCompactUiModel.LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        },
        hasBlockedAddToCart = hasBlockedAddToCart,
        usePreDraw = true
    )

    private fun getAddToCartQuantity(
        productId: String,
        miniCartData: MiniCartSimplifiedData?
    ): Int {
        return miniCartData?.run {
            val miniCartItem = miniCartData.miniCartItems.getMiniCartItemProduct(productId)
            val productParentId = miniCartItem?.productParentId ?: DEFAULT_PARENT_PRODUCT_ID

            return if (productParentId != DEFAULT_PARENT_PRODUCT_ID) {
                miniCartItems.getMiniCartItemParentProduct(productParentId)?.totalQuantity.orZero()
            } else {
                miniCartItem?.quantity.orZero()
            }
        } ?: HomeLayoutMapper.DEFAULT_QUANTITY
    }

    private fun getShopType(shop: Shop): String {
        return if (shop.goldShop) {
            SHOP_TYPE_GOLD
        } else if (shop.shopIsOfficial) {
            SHOP_TYPE_OS
        } else {
            SHOP_TYPE_PM
        }
    }

    private fun MutableList<Visitable<*>>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.getVisitableId() == visitableId }?.let { indexOf(it) }
    }

    private fun MutableList<Visitable<*>>.updateItemById(id: String?, block: () -> Visitable<*>?) {
        getItemIndex(id)?.let { index ->
            block.invoke()?.let { item ->
                removeAt(index)
                add(index, item)
            }
        }
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is TokoNowAdsCarouselUiModel -> id
            else -> null
        }
    }
}
