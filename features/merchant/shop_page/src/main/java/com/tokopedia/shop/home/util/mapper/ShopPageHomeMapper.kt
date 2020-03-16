package com.tokopedia.shop.home.util.mapper

import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherAmountTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetType.DISPLAY
import com.tokopedia.shop.home.WidgetType.VOUCHER
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.data.model.ShopProduct
import kotlin.math.roundToInt

object ShopPageHomeMapper {

    fun mapToShopPageHomeLayoutUiModel(
            response: ShopLayoutWidget,
            myShop: Boolean
    ): ShopPageHomeLayoutUiModel {
        return ShopPageHomeLayoutUiModel(
                response.layoutId,
                response.masterLayoutID,
                response.merchantTierID,
                response.status,
                response.maxWidgets,
                response.publishDate,
                mapToListWidgetUiModel(response.listWidget, myShop)
        )
    }

    private fun mapToListWidgetUiModel(
            shopLayoutWidgetResponse: List<ShopLayoutWidget.Widget>,
            isMyOwnProduct: Boolean
    ): List<BaseShopHomeWidgetUiModel> {
        return mutableListOf<BaseShopHomeWidgetUiModel>().apply {
            shopLayoutWidgetResponse.filter { it.data.isNotEmpty() }.onEach {
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
            VOUCHER.toLowerCase() -> {
                mapToVoucherUiModel(widgetResponse)
            }
            else -> {
                null
            }
        }
    }

    private fun mapToVoucherUiModel(widgetResponse: ShopLayoutWidget.Widget): ShopHomeVoucherUiModel {
        return ShopHomeVoucherUiModel(
                widgetResponse.widgetID,
                widgetResponse.layoutOrder,
                widgetResponse.name,
                widgetResponse.type,
                mapToHeaderModel(widgetResponse.header),
                mapToListVoucher(widgetResponse.data)
        )
    }

    private fun mapToListVoucher(
            data: List<ShopLayoutWidget.Widget.Data>
    ): List<MerchantVoucherViewModel>? {
        return mutableListOf<MerchantVoucherViewModel>().apply {
            data.onEach {
                add(mapToMerchantViewModel(it))
            }
        }
    }

    private fun mapToMerchantViewModel(data: ShopLayoutWidget.Widget.Data): MerchantVoucherViewModel {
        return MerchantVoucherViewModel().apply {
            voucherId = data.voucherID
            voucherName = data.name
            voucherCode = data.voucherCode
            merchantVoucherType = data.voucherType.voucherType.takeIf { it != -1 }?: MerchantVoucherTypeDef.TYPE_FREE_ONGKIR
            merchantVoucherAmountType = data.amount.amountType.takeIf { it != -1 }  ?: MerchantVoucherAmountTypeDef.TYPE_FIXED
            merchantVoucherAmount = data.amount.amount.toFloat()
            minimumSpend = data.minimumSpend
            ownerId = data.owner.ownerId
            validThru = data.validThru.toLong()
            tnc = data.tnc
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
                mapToListHomeProductViewModel(widgetModel.data, isMyOwnProduct)
        )
    }

    private fun mapToHeaderModel(header: ShopLayoutWidget.Widget.Header): BaseShopHomeWidgetUiModel.Header {
        return BaseShopHomeWidgetUiModel.Header(
                header.title,
                header.ctaText,
                header.ctaLink,
                header.cover,
                header.ratio,
                header.isAtc
        )
    }

    private fun mapToListHomeProductViewModel(
            data: List<ShopLayoutWidget.Widget.Data>,
            isMyOwnProduct: Boolean
    ): List<ShopHomeProductViewModel> {
        return mutableListOf<ShopHomeProductViewModel>().apply {
            data.onEach {
                add(mapToHomeProductViewModel(it, isMyOwnProduct))
            }
        }.toList()
    }

    fun mapToHomeProductViewModelForAllProduct(
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

    private fun mapToHomeProductViewModel(
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
                isWishList = false
                productUrl = response.productUrl
                isSoldOut = response.isSoldOut
                isShowWishList = !isMyOwnProduct
                isShowFreeOngkir = response.isShowFreeOngkir
                freeOngkirPromoIcon = response.freeOngkirPromoIcon
            }
}