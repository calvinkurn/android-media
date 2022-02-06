package com.tokopedia.shop.home.util.mapper

import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.home.WidgetName.FLASH_SALE_TOKO
import com.tokopedia.shop.home.WidgetName.INFO_CARD
import com.tokopedia.shop.home.WidgetName.IS_SHOW_ETALASE_NAME
import com.tokopedia.shop.home.WidgetName.NEW_PRODUCT_LAUNCH_CAMPAIGN
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.SHOWCASE_SLIDER_TWO_ROWS
import com.tokopedia.shop.home.WidgetName.VOUCHER_STATIC
import com.tokopedia.shop.home.WidgetType.CAMPAIGN
import com.tokopedia.shop.home.WidgetType.CARD
import com.tokopedia.shop.home.WidgetType.DISPLAY
import com.tokopedia.shop.home.WidgetType.DYNAMIC
import com.tokopedia.shop.home.WidgetType.PERSONALIZATION
import com.tokopedia.shop.home.WidgetType.SHOWCASE
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeShowcaseListBaseWidgetViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.GetCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCampaignNplTncUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCardDonationUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleTncUiModel
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopPageHomeWidgetLayoutUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.view.datamodel.LabelGroupUiModel
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

object ShopPageHomeMapper {
    private const val PRODUCT_RATING_DIVIDER = 20
    private const val ZERO_PRODUCT_DISCOUNT = "0"

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
                it.rating = stats.rating.toDouble() / PRODUCT_RATING_DIVIDER
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
                it.labelGroupList =
                    labelGroupList.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
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
    ): ProductCardModel {
        val discountWithoutPercentageString =
            shopHomeProductViewModel.discountPercentage?.replace("%", "")
                ?: ""
        val discountPercentage = if (discountWithoutPercentageString == "0") {
            ""
        } else {
            "$discountWithoutPercentageString%"
        }

        val freeOngkirObject = ProductCardModel.FreeOngkir(
            shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon
                ?: ""
        )

        return if (isHasOCCButton) {
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

    fun mapToProductCardModel(
        isHasAddToCartButton: Boolean,
        hasThreeDots: Boolean,
        shopHomeProductViewModel: ShopHomeProductUiModel,
        isWideContent: Boolean
    ): ProductCardModel {
        val discountWithoutPercentageString =
            shopHomeProductViewModel.discountPercentage?.replace("%", "")
                ?: ""
        val discountPercentage = if (discountWithoutPercentageString == "0") {
            ""
        } else {
            "$discountWithoutPercentageString%"
        }

        val freeOngkirObject = ProductCardModel.FreeOngkir(
            shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon
                ?: ""
        )
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

    fun mapToProductCardCampaignModel(
        isHasAddToCartButton: Boolean,
        hasThreeDots: Boolean,
        shopHomeProductViewModel: ShopHomeProductUiModel
    ): ProductCardModel {
        val discountWithoutPercentageString =
            shopHomeProductViewModel.discountPercentage?.replace("%", "")
                ?: ""
        val discountPercentage = if (discountWithoutPercentageString == "0") {
            ""
        } else {
            "$discountWithoutPercentageString%"
        }

        val freeOngkirObject = ProductCardModel.FreeOngkir(
            shopHomeProductViewModel.isShowFreeOngkir, shopHomeProductViewModel.freeOngkirPromoIcon
                ?: ""
        )
        return ProductCardModel(
            productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
            productName = shopHomeProductViewModel.name ?: "",
            discountPercentage = discountPercentage.takeIf { !shopHomeProductViewModel.hideGimmick }
                ?: "",
            slashedPrice = shopHomeProductViewModel.originalPrice.orEmpty()
                .takeIf { !shopHomeProductViewModel.hideGimmick } ?: "",
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

    fun mapToShopHomeFlashSaleTncUiModel(model: ShopHomeCampaignNplTncModel): ShopHomeFlashSaleTncUiModel {
        return ShopHomeFlashSaleTncUiModel(
            title = model.title,
            listMessage = model.listMessage
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
                when (widgetResponse.name) {
                    FLASH_SALE_TOKO -> mapToFlashSaleUiModel(widgetResponse)
                    NEW_PRODUCT_LAUNCH_CAMPAIGN -> mapToNewProductLaunchCampaignUiModel(
                        widgetResponse,
                        isLoggedIn
                    )
                    else -> null
                }
            }
            DYNAMIC.toLowerCase(Locale.getDefault()) -> mapCarouselPlayWidget(widgetResponse)
            PERSONALIZATION.toLowerCase(Locale.getDefault()) -> mapToProductPersonalizationUiModel(
                widgetResponse,
                isMyOwnProduct
            )
            SHOWCASE.toLowerCase(Locale.getDefault()) -> mapToShowcaseListUiModel(widgetResponse)
            CARD.lowercase() -> mapToCardDonationUiModel(widgetResponse)
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

    private fun mapToShowcaseListUiModel(
        widgetResponse: ShopLayoutWidget.Widget
    ) = ShopHomeShowcaseListSliderUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header),
        showcaseListItem = mapToShowcaseListItemUiModel(
            widgetResponse.data,
            widgetResponse.name,
            widgetResponse.header
        )
    )

    private fun mapToCardDonationUiModel(
        widgetResponse: ShopLayoutWidget.Widget
    ) = ShopHomeCardDonationUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header)
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

    private fun mapToFlashSaleUiModel(
        widgetResponse: ShopLayoutWidget.Widget
    ): ShopHomeFlashSaleUiModel {
        return ShopHomeFlashSaleUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header),
            mapToFlashSaleUiModelList(widgetResponse.data)
        )
    }

    private fun mapToListNewProductLaunchCampaignItem(
        data: List<ShopLayoutWidget.Widget.Data>,
        isLoggedIn: Boolean
    ): List<ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem> {
        return data.map {
            val isRemindMe =
                if (!isLoggedIn && it.statusCampaign.toLowerCase() == StatusCampaign.UPCOMING.statusCampaign.toLowerCase())
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

    private fun mapToFlashSaleUiModelList(data: List<ShopLayoutWidget.Widget.Data>): List<ShopHomeFlashSaleUiModel.FlashSaleItem> {
        return data.map {
            ShopHomeFlashSaleUiModel.FlashSaleItem(
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
                it.totalProduct,
                it.totalProductWording,
                mapCampaignFlashSaleListProduct(it.statusCampaign, it.listProduct),
                false,
                it.backgroundGradientColor.firstColor,
                it.backgroundGradientColor.secondColor
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
        statusCampaign: String,
        listProduct: List<ShopLayoutWidget.Widget.Data.Product>
    ): List<ShopHomeProductUiModel> {
        return listProduct.map {
            ShopHomeProductUiModel().apply {
                id = it.id
                name = it.name
                displayedPrice = it.discountedPrice
                originalPrice = it.displayedPrice
                discountPercentage = it.discountPercentage
                imageUrl = it.imageUrl
                imageUrl300 = ""
                productUrl = it.urlApps
                if (statusCampaign.toLowerCase() == StatusCampaign.ONGOING.statusCampaign.toLowerCase()) {
                    stockLabel = it.stockWording.title
                    stockSoldPercentage = it.stockSoldPercentage.toInt()
                }
                hideGimmick = it.hideGimmick
                labelGroupList =
                    it.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
            }
        }
    }

    private fun mapCampaignFlashSaleListProduct(
        statusCampaign: String,
        listProduct: List<ShopLayoutWidget.Widget.Data.Product>
    ): List<ShopHomeProductUiModel> {
        return listProduct.map {
            ShopHomeProductUiModel().apply {
                id = it.id
                name = it.name
                displayedPrice = it.discountedPrice
                originalPrice = it.displayedPrice
                discountPercentage = it.discountPercentage
                imageUrl = it.imageUrl
                imageUrl300 = ""
                productUrl = it.urlApps
                hideGimmick = it.hideGimmick
                when (statusCampaign.lowercase(Locale.getDefault())) {
                    StatusCampaign.ONGOING.statusCampaign -> {
                        stockLabel = it.stockWording.title
                        stockSoldPercentage = it.stockSoldPercentage.toInt()
                    }
                    StatusCampaign.UPCOMING.statusCampaign -> {
                        // hide discount percentage for upcoming flash sale product
                        discountPercentage = ZERO_PRODUCT_DISCOUNT
                    }
                }
                labelGroupList =
                    it.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
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
    ): List<ShopHomeProductUiModel> {
        return data.map {
            ShopHomeProductUiModel().apply {
                id = it.productID
                name = it.name
                displayedPrice = it.displayPrice
                originalPrice = it.originalPrice
                discountPercentage = it.discountPercentage
                imageUrl = it.imageUrl
                rating = it.rating.toDoubleOrZero()
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

    private fun mapToShowcaseListItemUiModel(
        data: List<ShopLayoutWidget.Widget.Data>,
        widgetName: String,
        widgetHeader: ShopLayoutWidget.Widget.Header
    ): List<ShopHomeShowcaseListItemUiModel> {
        val uiModelData = data.map {
            ShopHomeShowcaseListItemUiModel().apply {
                id = it.linkId.toString()
                imageUrl = it.imageUrl
                appLink = it.appLink
                name = it.showcaseName
                viewType = widgetName
                isShowEtalaseName = widgetHeader.isShowEtalaseName == IS_SHOW_ETALASE_NAME
            }
        }
        return if (widgetName == SHOWCASE_SLIDER_TWO_ROWS) {
            ShopHomeShowcaseListBaseWidgetViewHolder.getReorderShowcasePositionForTwoRowsSlider(
                uiModelData
            )
        } else {
            uiModelData
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
            rating = (response.rating.toDoubleOrZero() / PRODUCT_RATING_DIVIDER)
            isPo = response.isPO
            isWishList = false
            productUrl = response.productUrl
            isSoldOut = response.isSoldOut
            isShowWishList = !isMyOwnProduct
            isShowFreeOngkir = response.isShowFreeOngkir
            freeOngkirPromoIcon = response.freeOngkirPromoIcon
            labelGroupList =
                response.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
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

    fun mapToListShopHomeWidget(
        responseWidgetData: List<ShopLayoutWidget.Widget>,
        myShop: Boolean,
        isLoggedIn: Boolean
    ): List<BaseShopHomeWidgetUiModel> {
        return mutableListOf<BaseShopHomeWidgetUiModel>().apply {
            responseWidgetData.filter {
                it.data.isNotEmpty() || it.type.equals(
                    DYNAMIC,
                    ignoreCase = true
                ) || it.name == VOUCHER_STATIC || it.type.equals(CARD, ignoreCase = true)
            }.onEach {
                val widgetUiModel = mapToWidgetUiModel(it, myShop, isLoggedIn)
                widgetUiModel?.let { model ->
                    model.widgetMasterId = it.widgetMasterID
                    add(model)
                }
            }

            // testing donation
            add(
                0,
                mapToWidgetUiModel(
                    ShopLayoutWidget.Widget(
                        widgetID = "99",
                        widgetMasterID = "34",
                        layoutOrder = 0,
                        name = INFO_CARD,
                        type = CARD,
                        header = ShopLayoutWidget.Widget.Header(
                            title = "Selama Ramadan, sebagian keuntungan toko akan didonasikan.",
                            ctaText = "Selengkapnya",
                            ctaLink = "tokopedia://recharge/home?platform_id=30",
                            cover = "https://images.tokopedia.net/img/jJtrdn/2021/12/20/22a0f0ad-20e9-438d-b3e8-eec2ca3cb01e.jpg?b=UBO4%3BzI%3Bys%24%25Q%2CR%2AtSniClxFVDWqpdniR5o%7D",
                            ratio = "2:1",
                            isShowEtalaseName = 0
                        )
                    ),
                    myShop,
                    isLoggedIn
                )!!
            )
        }
    }

    fun mapToShopHomeWidgetLayoutData(response: ShopPageGetHomeType.HomeLayoutData): ShopPageHomeWidgetLayoutUiModel {
        val listWidget: ArrayList<ShopPageHomeWidgetLayoutUiModel.WidgetLayout> = arrayListOf()
        listWidget.add(
            ShopPageHomeWidgetLayoutUiModel.WidgetLayout(
                "99",
                "34",
                CARD,
                INFO_CARD
            )
        )
        listWidget.addAll(response.widgetIdList.map {
            ShopPageHomeWidgetLayoutUiModel.WidgetLayout(
                it.widgetId,
                it.widgetMasterId,
                it.widgetType,
                it.widgetName
            )
        })

        return ShopPageHomeWidgetLayoutUiModel(
            layoutId = response.layoutId,
            masterLayoutId = response.masterLayoutId.toIntOrZero().toString(),
            listWidgetLayout = listWidget
//            listWidgetLayout = response.widgetIdList.map {
//                ShopPageHomeWidgetLayoutUiModel.WidgetLayout(
//                    it.widgetId,
//                    it.widgetMasterId,
//                    it.widgetType,
//                    it.widgetName
//                )
//            }
        )
    }

    fun mapShopHomeWidgetLayoutToListShopHomeWidget(
        listWidgetLayout: List<ShopPageHomeWidgetLayoutUiModel.WidgetLayout>,
        myShop: Boolean,
        isLoggedIn: Boolean
    ): List<BaseShopHomeWidgetUiModel> {
        return mutableListOf<BaseShopHomeWidgetUiModel>().apply {
            listWidgetLayout.onEach {
                mapToWidgetUiModel(
                    ShopLayoutWidget.Widget(
                        widgetID = it.widgetId,
                        type = it.widgetType,
                        name = it.widgetName
                    ), myShop, isLoggedIn
                )?.let { resModel ->
                    resModel.widgetMasterId = it.widgetMasterId
                    add(resModel)
                }
            }
        }
    }

}