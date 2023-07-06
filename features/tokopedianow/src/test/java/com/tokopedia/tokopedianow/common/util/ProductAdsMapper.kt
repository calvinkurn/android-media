package com.tokopedia.tokopedianow.common.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
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

    fun createProductAdsCarousel(): TokoNowAdsCarouselUiModel {
        return TokoNowAdsCarouselUiModel(
            id = PRODUCT_ADS_CAROUSEL,
            items = emptyList(),
            state = TokoNowLayoutState.LOADING
        )
    }

    fun mapProductAdsCarousel(
        response: ProductAdsResponse,
        miniCartData: MiniCartSimplifiedData? = null,
        hasBlockedAddToCart: Boolean = false
    ): TokoNowAdsCarouselUiModel {
        val productList = response.productList.mapIndexed { index, item ->
            val shop = item.shop
            val product = item.product
            ProductCardCompactCarouselItemUiModel(
                position = index + 1,
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
            id = PRODUCT_ADS_CAROUSEL,
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

    fun MutableList<Visitable<*>>.findAdsProduct(productId: String): ProductCardCompactCarouselItemUiModel? {
        var product: ProductCardCompactCarouselItemUiModel? = null
        filterIsInstance<TokoNowAdsCarouselUiModel>().forEach { carousel ->
            carousel.items.firstOrNull { it.getProductId() == productId }?.let {
                product = it
            }
        }
        return product
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
}
