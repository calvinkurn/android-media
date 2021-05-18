package com.tokopedia.shop.home.util.mapper

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.VOUCHER_STATIC
import com.tokopedia.shop.home.WidgetType.CAMPAIGN
import com.tokopedia.shop.home.WidgetType.DISPLAY
import com.tokopedia.shop.home.WidgetType.DYNAMIC
import com.tokopedia.shop.home.WidgetType.PERSONALIZATION
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.view.datamodel.LabelGroupUiModel
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*
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
    ): ShopHomeProductUiModel =
            with(shopProduct) {
                ShopHomeProductUiModel().also {
                    it.id = productId
                    it.name = name
                    it.displayedPrice = price.textIdr
                    it.originalPrice = campaign.originalPriceFmt
                    it.discountPercentage = campaign.discountedPercentage
                    it.imageUrl = primaryImage.original
                    it.imageUrl300 = primaryImage.resize300
                    it.totalReview = stats.reviewCount.toString()
                    it.rating = stats.rating.toDouble() / 20
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

    private fun mapToLabelGroupViewModel(labelGroup: LabelGroup): LabelGroupUiModel {
        return LabelGroupUiModel(
                position = labelGroup.position,
                title = labelGroup.title,
                type = labelGroup.type,
                url = labelGroup.url
        )
    }

    fun mapToProductCardPersonalizationModel(
            shopHomeProductViewModel: ShopHomeProductUiModel,
            isHasATC: Boolean,
            isHasOCCButton: Boolean,
            occButtonText: String = "",
    ) : ProductCardModel {
        val discountWithoutPercentageString = shopHomeProductViewModel.discountPercentage?.replace("%", "")
                ?: ""
        val discountPercentage = if (discountWithoutPercentageString == "0") {
            ""
        } else {
            "$discountWithoutPercentageString%"
        }

        val freeOngkirObject = ProductCardModel.FreeOngkir(shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon
                ?: "")

        return if(isHasOCCButton) {
            ProductCardModel(
                    productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
                    productName = shopHomeProductViewModel.name ?: "",
                    discountPercentage = discountPercentage,
                    slashedPrice = shopHomeProductViewModel.originalPrice ?: "",
                    formattedPrice = shopHomeProductViewModel.displayedPrice ?: "",
                    hasAddToCartButton = isHasATC,
                    addToCartButtonType = UnifyButton.Type.MAIN,
                    addToCardText = occButtonText
            )
        } else {
            ProductCardModel(
                    productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
                    productName = shopHomeProductViewModel.name ?: "",
                    discountPercentage = discountPercentage,
                    slashedPrice = shopHomeProductViewModel.originalPrice ?: "",
                    formattedPrice = shopHomeProductViewModel.displayedPrice ?: "",
                    countSoldRating = if (shopHomeProductViewModel.rating != 0.0) shopHomeProductViewModel.rating.toString() else "",
                    freeOngkir = freeOngkirObject,
                    labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                        mapToProductCardLabelGroup(it)
                    },
                    hasAddToCartButton = isHasATC
            )
        }
    }

    fun mapToProductCardModel(isHasAddToCartButton: Boolean, hasThreeDots: Boolean, shopHomeProductViewModel: ShopHomeProductUiModel, isWideContent: Boolean): ProductCardModel {
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
                countSoldRating = if (shopHomeProductViewModel.rating != 0.0) shopHomeProductViewModel.rating.toString() else "",
                freeOngkir = freeOngkirObject,
                labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                    mapToProductCardLabelGroup(it)
                },
                hasThreeDots = hasThreeDots,
                hasAddToCartButton = isHasAddToCartButton,
                addToCartButtonType = UnifyButton.Type.MAIN,
                isWideContent = isWideContent
        )
    }

    fun mapToProductCardCampaignModel(isHasAddToCartButton: Boolean, hasThreeDots: Boolean, shopHomeProductViewModel: ShopHomeProductUiModel): ProductCardModel {
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
                countSoldRating = if (shopHomeProductViewModel.rating != 0.0) shopHomeProductViewModel.rating.toString() else "",
                freeOngkir = freeOngkirObject,
                labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                    mapToProductCardLabelGroup(it)
                },
                hasThreeDots = hasThreeDots,
                hasAddToCartButton = isHasAddToCartButton,
                addToCartButtonType = UnifyButton.Type.MAIN,
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

    private fun mapToProductCardLabelGroup(labelGroupUiModel: LabelGroupUiModel): ProductCardModel.LabelGroup {
        return ProductCardModel.LabelGroup(
                position = labelGroupUiModel.position,
                title = labelGroupUiModel.title,
                type = labelGroupUiModel.type,
                imageUrl = labelGroupUiModel.url
        )
    }

    private fun mapToListWidgetUiModel(
            shopLayoutWidgetResponse: List<ShopLayoutWidget.Widget>,
            isMyOwnProduct: Boolean,
            isLoggedIn: Boolean
    ): List<BaseShopHomeWidgetUiModel> {
        return mutableListOf<BaseShopHomeWidgetUiModel>().apply {
            shopLayoutWidgetResponse.filter { it.data.isNotEmpty() || it.type.toLowerCase() == DYNAMIC.toLowerCase() || it.name == VOUCHER_STATIC}.onEach {
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
        if (widgetResponse.name == VOUCHER_STATIC) {
            return mapToVoucherUiModel(widgetResponse)
        }
        return when (widgetResponse.type.toLowerCase()) {
            DISPLAY.toLowerCase() -> {
                mapToDisplayWidget(widgetResponse)
            }
            PRODUCT.toLowerCase() -> {
                mapToProductWidgetUiModel(widgetResponse, isMyOwnProduct)
            }
            CAMPAIGN.toLowerCase() -> {
                mapToNewProductLaunchCampaignUiModel(widgetResponse, isLoggedIn)
            }
            DYNAMIC.toLowerCase(Locale.getDefault()) -> mapCarouselPlayWidget(widgetResponse)
            PERSONALIZATION.toLowerCase(Locale.getDefault()) -> mapToProductPersonalizationUiModel(widgetResponse, isMyOwnProduct)
            else -> {
                null
            }
        }
    }

    private fun mapToProductPersonalizationUiModel(
            widgetResponse: ShopLayoutWidget.Widget,
            isMyProduct: Boolean
    ) = ShopHomeCarousellProductUiModel(
            widgetId = widgetResponse.widgetID,
            layoutOrder = widgetResponse.layoutOrder,
            name = widgetResponse.name,
            type = widgetResponse.type,
            header = mapToHeaderModel(widgetResponse.header),
            productList = mapToWidgetProductListPersonalization(widgetResponse.data, isMyProduct)
    )

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
                    mapToDynamicRule(it.dynamicRule),
                    mapCampaignListBanner(it.listBanner),
                    mapCampaignListProduct(it.statusCampaign, it.listProduct),
                    isRemindMe
            )
        }
    }

    private fun mapToDynamicRule(dynamicRule: ShopLayoutWidget.Widget.Data.DynamicRule): ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.DynamicRule {
        return ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.DynamicRule(
                dynamicRule.descriptionHeader,
                ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.DynamicRule.DynamicRoleData(
                        dynamicRule.dynamicRoleData.firstOrNull()?.ruleID.orEmpty()
                )
        )
    }

    private fun mapCampaignListProduct(
            statusCampaign: String ,
            listProduct: List<ShopLayoutWidget.Widget.Data.Product>
    ): List<ShopHomeProductUiModel> {
        return listProduct.map {
            ShopHomeProductUiModel().apply {
                id = it.id
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
                mapToHeaderModel(widgetResponse.header)
        )
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

    private fun mapToHeaderModel(header: ShopLayoutWidget.Widget.Header): BaseShopHomeWidgetUiModel.Header {
        return BaseShopHomeWidgetUiModel.Header(
                header.title,
                header.ctaText,
                header.ctaLink,
                header.cover,
                header.ratio,
                header.isAtc,
                header.etalaseId
        )
    }

    private fun mapToWidgetProductListPersonalization(
            data: List<ShopLayoutWidget.Widget.Data>,
            isMyOwnProduct: Boolean
    ) : List<ShopHomeProductUiModel> {
        return data.map {
            ShopHomeProductUiModel().apply {
                id = it.productID
                name = it.name
                displayedPrice = it.displayPrice
                originalPrice = it.originalPrice
                discountPercentage = it.discountPercentage
                imageUrl = it.imageUrl
                rating = it.rating
                isPo = it.isPO
                isWishList = false
                productUrl = it.productUrl
                isSoldOut = it.isSoldOut
                isShowWishList = !isMyOwnProduct
                isShowFreeOngkir = it.isShowFreeOngkir
                freeOngkirPromoIcon = it.freeOngkirPromoIcon
                recommendationType = it.recommendationType
                labelGroupList = it.labelGroups.map { mapToLabelGroupViewModel(it) }
                minimumOrder = it.minimumOrder ?: 1
            }
        }
    }

    private fun mapToWidgetProductListItemViewModel(
            data: List<ShopLayoutWidget.Widget.Data>,
            isMyOwnProduct: Boolean
    ): List<ShopHomeProductUiModel> {
        return mutableListOf<ShopHomeProductUiModel>().apply {
            data.onEach {
                add(mapToWidgetProductItem(it, isMyOwnProduct))
            }
        }.toList()
    }

    private fun mapToWidgetProductItem(
            response: ShopLayoutWidget.Widget.Data,
            isMyOwnProduct: Boolean
    ): ShopHomeProductUiModel =
            ShopHomeProductUiModel().apply {
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
                labelGroupList = response.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
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

    /*
     * Play widget
     */
    private fun mapCarouselPlayWidget(model: ShopLayoutWidget.Widget) = CarouselPlayWidgetUiModel(
            widgetId = model.widgetID,
            layoutOrder = model.layoutOrder,
            name = model.name,
            type = model.type,
            header = mapToHeaderModel(model.header)
    )
}