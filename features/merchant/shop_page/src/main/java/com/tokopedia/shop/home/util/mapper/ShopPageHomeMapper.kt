package com.tokopedia.shop.home.util.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherAmountTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.WidgetType.CAMPAIGN
import com.tokopedia.shop.home.WidgetType.DISPLAY
import com.tokopedia.shop.home.WidgetType.DYNAMIC
import com.tokopedia.shop.home.WidgetType.VOUCHER
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.view.datamodel.LabelGroupViewModel
import kotlin.math.roundToInt

object ShopPageHomeMapper {

    fun mapToShopPageHomeLayoutUiModel(
            response: ShopLayoutWidget,
            myShop: Boolean,
            isLoggedIn: Boolean
    ): ShopPageHomeLayoutUiModel {
        return ShopPageHomeLayoutUiModel(
                response.layoutId,
                response.masterLayoutID,
                response.merchantTierID,
                response.status,
                response.maxWidgets,
                response.publishDate,
                mapToListWidgetUiModel(response.listWidget, myShop, isLoggedIn)
        )
    }

    fun mapToHomeProductViewModelForAllProduct(
            shopProduct: ShopProduct,
            isMyOwnProduct: Boolean
    ): ShopHomeProductViewModel =
            with(shopProduct) {
                ShopHomeProductViewModel().also {
                    it.id = productId
                    it.name = name
                    it.displayedPrice = price.textIdr
                    it.originalPrice = campaign.originalPriceFmt
                    it.discountPercentage = campaign.discountedPercentage
                    it.imageUrl = primaryImage.original
                    it.imageUrl300 = primaryImage.resize300
                    it.totalReview = stats.reviewCount.toString()
                    it.rating = (stats.rating.toDouble() / 20).roundToInt().toDouble()
                    if (cashback.cashbackPercent > 0) {
                        it.cashback = cashback.cashbackPercent.toDouble()
                    }
                    it.isWholesale = flags.isWholesale
                    it.isPo = flags.isPreorder
                    it.isFreeReturn = flags.isFreereturn
                    it.isWishList = flags.isWishlist
                    it.productUrl = productUrl
                    it.isSoldOut = flags.isSold
                    it.isShowWishList = !isMyOwnProduct
                    it.isShowFreeOngkir = freeOngkir.isActive
                    it.freeOngkirPromoIcon = freeOngkir.imgUrl
                    it.labelGroupList = labelGroupList.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
                }
            }

    private fun mapToLabelGroupViewModel(labelGroup: LabelGroup): LabelGroupViewModel {
        return LabelGroupViewModel(
                position = labelGroup.position,
                title = labelGroup.title,
                type = labelGroup.type
        )
    }


    fun mapToProductCardModel(isHasAddToCartButton: Boolean, hasThreeDots: Boolean, shopHomeProductViewModel: ShopHomeProductViewModel): ProductCardModel {
        val totalReview = shopHomeProductViewModel.totalReview.toIntOrZero()
        val discountWithoutPercentageString = shopHomeProductViewModel.discountPercentage?.replace("%", "")
                ?: ""
        val discountPercentage = if (discountWithoutPercentageString == "0") {
            ""
        } else {
            "$discountWithoutPercentageString%"
        }

        val freeOngkirObject = ProductCardModel.FreeOngkir(shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon
                ?: "")
        return ProductCardModel(
                productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
                productName = shopHomeProductViewModel.name ?: "",
                discountPercentage = discountPercentage,
                slashedPrice = shopHomeProductViewModel.originalPrice ?: "",
                formattedPrice = shopHomeProductViewModel.displayedPrice ?: "",
                ratingCount = shopHomeProductViewModel.rating.toInt(),
                reviewCount = totalReview,
                freeOngkir = freeOngkirObject,
                labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                    mapToProductCardLabelGroup(it)
                },
                hasThreeDots = hasThreeDots,
                hasAddToCartButton = isHasAddToCartButton
        )
    }

    fun mapToProductCardCampaignModel(isHasAddToCartButton: Boolean, hasThreeDots: Boolean, shopHomeProductViewModel: ShopHomeProductViewModel): ProductCardModel {
        val discountWithoutPercentageString = shopHomeProductViewModel.discountPercentage?.replace("%", "")
                ?: ""
        val discountPercentage = if (discountWithoutPercentageString == "0") {
            ""
        } else {
            "$discountWithoutPercentageString%"
        }

        val freeOngkirObject = ProductCardModel.FreeOngkir(shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon
                ?: "")
        return ProductCardModel(
                productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
                productName = shopHomeProductViewModel.name ?: "",
                discountPercentage = discountPercentage.takeIf { !shopHomeProductViewModel.hideGimmick } ?: "",
                slashedPrice = shopHomeProductViewModel.originalPrice.orEmpty().takeIf { !shopHomeProductViewModel.hideGimmick } ?: "",
                formattedPrice = shopHomeProductViewModel.displayedPrice ?: "",
                ratingCount = shopHomeProductViewModel.rating.toInt(),
                freeOngkir = freeOngkirObject,
                labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                    mapToProductCardLabelGroup(it)
                },
                hasThreeDots = hasThreeDots,
                hasAddToCartButton = isHasAddToCartButton,
                stockBarLabel = shopHomeProductViewModel.stockLabel,
                stockBarPercentage = shopHomeProductViewModel.stockSoldPercentage
        )
    }

    fun mapToShopHomeCampaignNplTncUiModel(model: ShopHomeCampaignNplTncModel): ShopHomeCampaignNplTncUiModel {
        return ShopHomeCampaignNplTncUiModel(
                model.title,
                model.listMessage
        )
    }

    private fun mapToProductCardLabelGroup(labelGroupViewModel: LabelGroupViewModel): ProductCardModel.LabelGroup {
        return ProductCardModel.LabelGroup(
                position = labelGroupViewModel.position,
                title = labelGroupViewModel.title,
                type = labelGroupViewModel.type
        )
    }

    private fun mapToListWidgetUiModel(
            shopLayoutWidgetResponse: List<ShopLayoutWidget.Widget>,
            isMyOwnProduct: Boolean,
            isLoggedIn: Boolean
    ): List<BaseShopHomeWidgetUiModel> {
        return mutableListOf<BaseShopHomeWidgetUiModel>().apply {
            shopLayoutWidgetResponse.filter { it.data.isNotEmpty() || it.type.toLowerCase() == DYNAMIC.toLowerCase() }.onEach {
                val widgetUiModel = mapToWidgetUiModel(it, isMyOwnProduct, isLoggedIn)
                widgetUiModel?.let { model ->
                    add(model)
                }
            }
        }
    }

    private fun mapToWidgetUiModel(
            widgetResponse: ShopLayoutWidget.Widget,
            isMyOwnProduct: Boolean,
            isLoggedIn: Boolean
    ): BaseShopHomeWidgetUiModel? {
        return when (widgetResponse.type.toLowerCase()) {
            DISPLAY.toLowerCase() -> {
                mapToDisplayWidget(widgetResponse)
            }
            PRODUCT.toLowerCase() -> {
                mapToProductWidgetUiModel(widgetResponse, isMyOwnProduct)
            }
            VOUCHER.toLowerCase() -> {
                mapToVoucherUiModel(widgetResponse)
            }
            DYNAMIC.toLowerCase()-> {
                mapToPlayWidgetUiModel(widgetResponse, isMyOwnProduct)
            }
            CAMPAIGN.toLowerCase() -> {
                mapToNewProductLaunchCampaignUiModel(widgetResponse, isLoggedIn)
            }
            else -> {
                null
            }
        }
    }

    private fun mapToNewProductLaunchCampaignUiModel(
            widgetResponse: ShopLayoutWidget.Widget,
            isLoggedIn: Boolean
    ): ShopHomeNewProductLaunchCampaignUiModel {
        return ShopHomeNewProductLaunchCampaignUiModel(
                widgetResponse.widgetID,
                widgetResponse.layoutOrder,
                widgetResponse.name,
                widgetResponse.type,
                mapToHeaderModel(widgetResponse.header),
                mapToListNewProductLaunchCampaignItem(widgetResponse.data, isLoggedIn)
        )
    }

    private fun mapToListNewProductLaunchCampaignItem(
            data: List<ShopLayoutWidget.Widget.Data>,
            isLoggedIn: Boolean
    ): List<ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem> {
        return data.map {
            val isRemindMe = if (!isLoggedIn && it.statusCampaign.toLowerCase() == StatusCampaign.UPCOMING.statusCampaign.toLowerCase())
                false
            else
                null
            ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem(
                    it.campaignId,
                    it.name,
                    it.description,
                    it.startDate,
                    it.endDate,
                    it.statusCampaign,
                    it.timeDescription,
                    it.timeCounter,
                    it.totalNotify,
                    it.totalNotifyWording,
                    mapCampaignListBanner(it.listBanner),
                    mapCampaignListProduct(it.statusCampaign, it.listProduct),
                    isRemindMe
            )
        }
    }

    private fun mapCampaignListProduct(
            statusCampaign: String ,
            listProduct: List<ShopLayoutWidget.Widget.Data.Product>
    ): List<ShopHomeProductViewModel> {
        return listProduct.map {
            ShopHomeProductViewModel().apply {
                id = it.id.toString()
                name = it.name
                displayedPrice = it.discountedPrice
                originalPrice = it.price
                discountPercentage = it.discountPercentage
                imageUrl = it.imageUrl
                imageUrl300 = ""
                productUrl = it.urlApps
                if(statusCampaign.toLowerCase() == StatusCampaign.ONGOING.statusCampaign.toLowerCase()){
                    stockLabel = it.stockWording.title
                    stockSoldPercentage =  it.stockSoldPercentage.toInt()
                }
                hideGimmick = it.hideGimmick
                labelGroupList  = it.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
            }
        }
    }

    private fun mapCampaignListBanner(listBanner: List<ShopLayoutWidget.Widget.Data.Banner>): List<ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.BannerItem> {
        return listBanner.map {
            ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.BannerItem(
                    it.imageId,
                    it.imageUrl,
                    it.bannerType,
                    it.device
            )
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
                add(mapToVoucherItem(it))
            }
        }
    }

    private fun mapToVoucherItem(data: ShopLayoutWidget.Widget.Data): MerchantVoucherViewModel {
        return MerchantVoucherViewModel().apply {
            voucherId = data.voucherID
            voucherName = data.name
            voucherCode = data.voucherCode
            merchantVoucherType = data.voucherType.voucherType.takeIf { it != -1 }
                    ?: MerchantVoucherTypeDef.TYPE_FREE_ONGKIR
            merchantVoucherAmountType = data.amount.amountType.takeIf { it != -1 }
                    ?: MerchantVoucherAmountTypeDef.TYPE_FIXED
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

    private fun mapToProductWidgetUiModel(
            widgetModel: ShopLayoutWidget.Widget,
            isMyOwnProduct: Boolean
    ): ShopHomeCarousellProductUiModel {
        return ShopHomeCarousellProductUiModel(
                widgetModel.widgetID,
                widgetModel.layoutOrder,
                widgetModel.name,
                widgetModel.type,
                mapToHeaderModel(widgetModel.header),
                mapToWidgetProductListItemViewModel(widgetModel.data, isMyOwnProduct)
        )
    }

    private fun mapToPlayWidgetUiModel(widgetModel: ShopLayoutWidget.Widget, isMyOwnProduct: Boolean): ShopHomePlayCarouselUiModel? {
        if(isMyOwnProduct) return null
        return ShopHomePlayCarouselUiModel(
                widgetId = widgetModel.widgetID,
                layoutOrder = widgetModel.layoutOrder,
                name = widgetModel.name,
                type = widgetModel.type,
                header = mapToHeaderModel(widgetModel.header)
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

    private fun mapToWidgetProductListItemViewModel(
            data: List<ShopLayoutWidget.Widget.Data>,
            isMyOwnProduct: Boolean
    ): List<ShopHomeProductViewModel> {
        return mutableListOf<ShopHomeProductViewModel>().apply {
            data.onEach {
                add(mapToWidgetProductItem(it, isMyOwnProduct))
            }
        }.toList()
    }

    private fun mapToWidgetProductItem(
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

    fun mapToGetCampaignNotifyMeUiModel(model: GetCampaignNotifyMeModel): GetCampaignNotifyMeUiModel {
        return GetCampaignNotifyMeUiModel(
                model.campaignId,
                model.success,
                model.message,
                model.errorMessage,
                model.isAvailable
        )
    }
}