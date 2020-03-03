package com.tokopedia.shop.home.util.mapper

import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.product.data.model.ShopProduct
import kotlin.math.roundToInt

object ShopPageHomeMapper {

    fun mapToShopHomeWidgetModel(
            shopLayoutWidgetResponse: ShopLayoutWidget,
            isMyOwnProduct: Boolean
    ): List<BaseShopHomeWidgetUiModel> {
        return mutableListOf<BaseShopHomeWidgetUiModel>().apply {
            shopLayoutWidgetResponse.listWidget.onEach {
                when (it.name.toLowerCase()) {
                    PRODUCT.toLowerCase() -> {
                        add(mapToShopHomeCarouselProductUiModel(it, isMyOwnProduct))
                    }
                }
            }
        }
    }

    private fun mapToShopHomeCarouselProductUiModel(
            widgetModel: ShopLayoutWidget.Widget,
            isMyOwnProduct: Boolean
    ): ShopHomeCarousellProductUiModel {
        return ShopHomeCarousellProductUiModel(
                widgetModel.widgetID,
                widgetModel.layoutOrder,
                widgetModel.name,
                widgetModel.type,
                mapToHeaderModel(widgetModel.header),
                mapToProductViewModel(widgetModel.data, isMyOwnProduct)
        )
    }

    private fun mapToHeaderModel(header: ShopLayoutWidget.Widget.Header): BaseShopHomeWidgetUiModel.Header {
        return BaseShopHomeWidgetUiModel.Header(
                header.title,
                header.ctaText,
                header.ctaLink,
                header.cover
        )
    }

    private fun mapToProductViewModel(
            data: List<ShopLayoutWidget.Widget.Data>,
            isMyOwnProduct: Boolean
    ): List<ShopHomeProductViewModel> {
        return mutableListOf<ShopHomeProductViewModel>().apply {
            data.onEach {
                add(mapFeaturedProductToProductViewModel(it, isMyOwnProduct))
            }
        }.toList()
    }

    fun mapShopProductToProductViewModel(
            shopProduct: ShopProduct,
            isMyOwnProduct: Boolean
    ): ShopHomeProductViewModel =
            ShopHomeProductViewModel().apply {
                id = shopProduct.productId
                name = shopProduct.name
                displayedPrice = shopProduct.price.textIdr
                originalPrice = shopProduct.campaign.originalPriceFmt
                discountPercentage = shopProduct.campaign.discountedPercentage
                imageUrl = shopProduct.primaryImage.original
                imageUrl300 = shopProduct.primaryImage.resize300
                totalReview = shopProduct.stats.reviewCount.toString()
                rating = (shopProduct.stats.rating.toDouble() / 20).roundToInt().toDouble()
                if (shopProduct.cashback.cashbackPercent > 0) {
                    cashback = shopProduct.cashback.cashbackPercent.toDouble()
                }
                isWholesale = shopProduct.flags.isWholesale
                isPo = shopProduct.flags.isPreorder
                isFreeReturn = shopProduct.flags.isFreereturn
                isWishList = shopProduct.flags.isWishlist
                productUrl = shopProduct.productUrl
                isSoldOut = shopProduct.flags.isSold
                isShowWishList = !isMyOwnProduct
                isShowFreeOngkir = shopProduct.freeOngkir.isActive
                freeOngkirPromoIcon = shopProduct.freeOngkir.imgUrl
            }

    private fun mapFeaturedProductToProductViewModel(
            response: ShopLayoutWidget.Widget.Data,
            isMyOwnProduct: Boolean
    ): ShopHomeProductViewModel =
            ShopHomeProductViewModel().apply {
                id = response.productID.toString()
                name = response.name
                displayedPrice = response.displayPrice
                originalPrice = response.originalPrice
                discountPercentage = response.discountPercentage
                imageUrl = response.imageUrl
                imageUrl300 = ""
                totalReview = response.totalReview
                rating = (response.rating / 20).roundToInt().toDouble()
                isPo = response.isPO
                //todo change this
                isWishList = false
                productUrl = response.productUrl
                isSoldOut = response.isSoldOut
                isShowWishList = !isMyOwnProduct
                isShowFreeOngkir = response.isShowFreeOngkir
                freeOngkirPromoIcon = response.freeOngkirPromoIcon
            }
}