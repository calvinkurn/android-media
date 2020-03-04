package com.tokopedia.shop.home.util.mapper

import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetType.DISPLAY
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.product.data.model.ShopProduct
import kotlin.math.roundToInt

object ShopPageHomeMapper {

    fun mapToListWidgetUiModel(
            shopLayoutWidgetResponse: ShopLayoutWidget,
            isMyOwnProduct: Boolean
    ): List<BaseShopHomeWidgetUiModel> {
        return mutableListOf<BaseShopHomeWidgetUiModel>().apply {
            shopLayoutWidgetResponse.listWidget.onEach {
                val widgetUiModel = mapToWidgetUiModel(it, isMyOwnProduct)
                widgetUiModel?.let { model ->
                    add(model)
                }
            }
        }
    }

    private fun mapToWidgetUiModel(
            widgetResponse: ShopLayoutWidget.Widget,
            isMyOwnProduct: Boolean
    ): BaseShopHomeWidgetUiModel? {
        return when (widgetResponse.type.toLowerCase()) {
            DISPLAY.toLowerCase() -> {
                mapToDisplayWidget(widgetResponse)
            }
            PRODUCT.toLowerCase() -> {
                mapToProductUiModel(widgetResponse, isMyOwnProduct)
            }
//            VOUCHER.toLowerCase() -> {
//                mapToVoucherUiModel(widgetResponse)
//            }
            else -> {
                null
            }
        }
    }

    private fun mapToDisplayWidget(widgetResponse: ShopLayoutWidget.Widget): ShopHomeDisplayWidgetUiModel {
        return ShopHomeDisplayWidgetUiModel(
                widgetResponse.widgetID,
                widgetResponse.layoutOrder,
                widgetResponse.name,
                widgetResponse.type,
                mapToHeaderModel(widgetResponse.header),
                mapToListDisplayWidgetItem(widgetResponse.data)
        )
    }

    private fun mapToListDisplayWidgetItem(
            data: List<ShopLayoutWidget.Widget.Data>
    ): List<ShopHomeDisplayWidgetUiModel.DisplayWidgetItem>? {
        return mutableListOf<ShopHomeDisplayWidgetUiModel.DisplayWidgetItem>().apply {
            data.onEach {
                add(mapToDisplayWidgetItem(it))
            }
        }
    }

    private fun mapToDisplayWidgetItem(data: ShopLayoutWidget.Widget.Data): ShopHomeDisplayWidgetUiModel.DisplayWidgetItem {
        return ShopHomeDisplayWidgetUiModel.DisplayWidgetItem(
                data.imageUrl,
                data.appLink,
                data.webLink,
                data.videoUrl
        )
    }

    private fun mapToProductUiModel(
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