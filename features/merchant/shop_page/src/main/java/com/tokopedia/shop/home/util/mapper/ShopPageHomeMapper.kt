package com.tokopedia.shop.home.util.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopPageHeaderDataUiModel
import com.tokopedia.shop.common.data.model.ShopPageHeaderUiModel
import com.tokopedia.shop.common.data.model.ShopPageWidgetLayoutUiModel
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.home.WidgetName.BANNER_TIMER
import com.tokopedia.shop.home.WidgetName.BIG_CAMPAIGN_THEMATIC
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.ETALASE_THEMATIC
import com.tokopedia.shop.home.WidgetName.FLASH_SALE_TOKO
import com.tokopedia.shop.home.WidgetName.IS_SHOW_ETALASE_NAME
import com.tokopedia.shop.home.WidgetName.NEW_PRODUCT_LAUNCH_CAMPAIGN
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.RECENT_ACTIVITY
import com.tokopedia.shop.home.WidgetName.REMINDER
import com.tokopedia.shop.home.WidgetName.SHOWCASE_SLIDER_TWO_ROWS
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER_STATIC
import com.tokopedia.shop.home.WidgetType.BUNDLE
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
import com.tokopedia.shop.home.view.model.ShopHomeDisplayBannerItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleTncUiModel
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductBundleListUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopPageHomeWidgetLayoutUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.view.datamodel.LabelGroupUiModel
import com.tokopedia.shop_widget.common.uimodel.DynamicHeaderUiModel
import com.tokopedia.shop_widget.common.util.ProductCardMapper.LABEL_POSITION_INTEGRITY
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

object ShopPageHomeMapper {
    private const val PRODUCT_RATING_DIVIDER = 20
    private const val ZERO_PRODUCT_DISCOUNT = "0"

    fun mapToHomeProductViewModelForAllProduct(
        shopProduct: ShopProduct,
        isMyOwnProduct: Boolean,
        isEnableDirectPurchase: Boolean
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
                it.productUrl = appLink
                it.isSoldOut = flags.isSold
                it.isShowWishList = !isMyOwnProduct
                it.isShowFreeOngkir = freeOngkir.isActive
                it.freeOngkirPromoIcon = freeOngkir.imgUrl
                it.labelGroupList =
                    labelGroupList.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
                it.minimumOrder = minimumOrder
                it.maximumOrder = getMaximumOrderForGetShopProduct(shopProduct)
                it.stock = stock
                it.isEnableDirectPurchase = isEnableDirectPurchase
                it.isVariant = hasVariant
                it.parentId = parentId
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
        widgetName: String = ""
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
            shopHomeProductViewModel.isShowFreeOngkir,
            shopHomeProductViewModel.freeOngkirPromoIcon
                ?: ""
        )

        val baseProductCardModel = if (isHasOCCButton) {
            ProductCardModel(
                productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
                productName = shopHomeProductViewModel.name ?: "",
                discountPercentage = discountPercentage,
                slashedPrice = shopHomeProductViewModel.originalPrice ?: "",
                formattedPrice = shopHomeProductViewModel.displayedPrice ?: "",
                hasAddToCartButton = isHasATC,
                addToCartButtonType = UnifyButton.Type.MAIN,
                addToCardText = occButtonText,
                countSoldRating = if (shopHomeProductViewModel.rating != 0.0) shopHomeProductViewModel.rating.toString() else "",
                freeOngkir = freeOngkirObject,
                labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                    mapToProductCardLabelGroup(it)
                }
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
        return if (isShopPersonalizationWidgetEnableDirectPurchase(
                shopHomeProductViewModel.isEnableDirectPurchase,
                widgetName
            ) && isProductCardIsNotSoldOut(shopHomeProductViewModel.isSoldOut)
        ) {
            if (shopHomeProductViewModel.isVariant) {
                createProductCardWithVariantAtcModel(shopHomeProductViewModel, baseProductCardModel)
            } else {
                if (shopHomeProductViewModel.productInCart.isZero()) {
                    createProductCardWithDefaultAddToCardModel(baseProductCardModel)
                } else {
                    createProductCardWithNonVariantAtcModel(
                        shopHomeProductViewModel,
                        baseProductCardModel
                    )
                }
            }
        } else {
            baseProductCardModel
        }
    }

    private fun isProductCardIsNotSoldOut(isProductSoldOut: Boolean): Boolean {
        return !isProductSoldOut
    }

    private fun isShopPersonalizationWidgetEnableDirectPurchase(
        enableDirectPurchase: Boolean,
        widgetName: String
    ): Boolean {
        return enableDirectPurchase && (widgetName == RECENT_ACTIVITY || widgetName == REMINDER)
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
            shopHomeProductViewModel.isShowFreeOngkir,
            shopHomeProductViewModel.freeOngkirPromoIcon
                ?: ""
        )
        val baseProductCardModel = ProductCardModel(
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
        return if (shopHomeProductViewModel.isEnableDirectPurchase && isProductCardIsNotSoldOut(
                shopHomeProductViewModel.isSoldOut
            )
        ) {
            val productCardModel = if (shopHomeProductViewModel.isVariant) {
                createProductCardWithVariantAtcModel(shopHomeProductViewModel, baseProductCardModel)
            } else {
                if (shopHomeProductViewModel.productInCart.isZero()) {
                    createProductCardWithDefaultAddToCardModel(baseProductCardModel)
                } else {
                    createProductCardWithNonVariantAtcModel(
                        shopHomeProductViewModel,
                        baseProductCardModel
                    )
                }
            }
            productCardModel.copy(
                hasThreeDots = false
            )
        } else {
            baseProductCardModel.copy(
                hasThreeDots = hasThreeDots
            )
        }
    }

    private fun createProductCardWithDefaultAddToCardModel(baseProductCardModel: ProductCardModel): ProductCardModel {
        return baseProductCardModel.copy(
            variant = null,
            nonVariant = null,
            hasAddToCartButton = true
        )
    }

    private fun createProductCardWithVariantAtcModel(
        shopHomeProductViewModel: ShopHomeProductUiModel,
        baseProductCardModel: ProductCardModel
    ): ProductCardModel {
        return baseProductCardModel.copy(
            variant = ProductCardModel.Variant(
                shopHomeProductViewModel.productInCart
            ),
            nonVariant = null,
            hasAddToCartButton = false
        )
    }

    private fun createProductCardWithNonVariantAtcModel(
        shopHomeProductViewModel: ShopHomeProductUiModel,
        baseProductCardModel: ProductCardModel
    ): ProductCardModel {
        return baseProductCardModel.copy(
            nonVariant = ProductCardModel.NonVariant(
                quantity = shopHomeProductViewModel.productInCart,
                minQuantity = shopHomeProductViewModel.minimumOrder,
                maxQuantity = shopHomeProductViewModel.maximumOrder
            ),
            variant = null,
            hasAddToCartButton = false
        )
    }

    fun mapToProductCardCampaignModel(
        isHasAddToCartButton: Boolean,
        hasThreeDots: Boolean,
        shopHomeProductViewModel: ShopHomeProductUiModel,
        widgetName: String,
        statusCampaign: String
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
            shopHomeProductViewModel.isShowFreeOngkir,
            shopHomeProductViewModel.freeOngkirPromoIcon
                ?: ""
        )
        val baseProductCardModel = ProductCardModel(
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
        return if (isShopCampaignWidgetEnableDirectPurchase(
                shopHomeProductViewModel.isEnableDirectPurchase,
                widgetName
            ) && isProductCardIsNotSoldOut(shopHomeProductViewModel.isSoldOut) &&
            isStatusCampaignIsOngoing(statusCampaign)
        ) {
            if (shopHomeProductViewModel.isVariant) {
                createProductCardWithVariantAtcModel(shopHomeProductViewModel, baseProductCardModel)
            } else {
                if (shopHomeProductViewModel.productInCart.isZero()) {
                    createProductCardWithDefaultAddToCardModel(baseProductCardModel)
                } else {
                    createProductCardWithNonVariantAtcModel(
                        shopHomeProductViewModel,
                        baseProductCardModel
                    )
                }
            }
        } else {
            baseProductCardModel
        }
    }

    private fun isStatusCampaignIsOngoing(statusCampaign: String): Boolean {
        return statusCampaign.equals(StatusCampaign.ONGOING.statusCampaign, true)
    }

    private fun isShopCampaignWidgetEnableDirectPurchase(
        enableDirectPurchase: Boolean,
        widgetName: String
    ): Boolean {
        return enableDirectPurchase && widgetName == FLASH_SALE_TOKO
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
        isLoggedIn: Boolean,
        isThematicWidgetShown: Boolean,
        isEnableDirectPurchase: Boolean,
        shopId: String,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): Visitable<*>? {
        if (widgetResponse.name == VOUCHER_STATIC) {
            return mapToVoucherUiModel(widgetResponse, widgetLayout)
        }
        return when (widgetResponse.type.toLowerCase()) {
            DISPLAY.toLowerCase() -> {
                when(widgetResponse.name){
                    DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN, SLIDER_BANNER, SLIDER_SQUARE_BANNER, VIDEO -> {
                        mapToDisplayImageWidget(widgetResponse, widgetLayout)
                    }
                    BANNER_TIMER -> {
                        mapToBannerTimerWidget(widgetResponse, widgetLayout)
                    }
                    else -> null
                }

            }
            PRODUCT.toLowerCase() -> {
                mapToProductWidgetUiModel(widgetResponse, isMyOwnProduct, isEnableDirectPurchase, widgetLayout)
            }
            CAMPAIGN.toLowerCase() -> {
                if (isThematicWidgetShown) {
                    when (widgetResponse.name) {
                        ETALASE_THEMATIC -> mapToThematicWidget(widgetResponse, widgetLayout)
                        BIG_CAMPAIGN_THEMATIC -> mapToThematicWidget(widgetResponse, widgetLayout)
                        FLASH_SALE_TOKO -> mapToFlashSaleUiModel(widgetResponse, isEnableDirectPurchase, widgetLayout)
                        NEW_PRODUCT_LAUNCH_CAMPAIGN -> mapToNewProductLaunchCampaignUiModel(
                            widgetResponse,
                            isLoggedIn,
                            widgetLayout
                        )
                        else -> null
                    }
                } else {
                    when (widgetResponse.name) {
                        FLASH_SALE_TOKO -> mapToFlashSaleUiModel(
                            widgetResponse,
                            isEnableDirectPurchase,
                            widgetLayout
                        )
                        NEW_PRODUCT_LAUNCH_CAMPAIGN -> mapToNewProductLaunchCampaignUiModel(
                            widgetResponse,
                            isLoggedIn,
                            widgetLayout
                        )
                        else -> null
                    }
                }
            }
            DYNAMIC.toLowerCase(Locale.getDefault()) -> mapCarouselPlayWidget(widgetResponse, widgetLayout)
            PERSONALIZATION.toLowerCase(Locale.getDefault()) -> mapToProductPersonalizationUiModel(
                widgetResponse,
                isMyOwnProduct,
                isEnableDirectPurchase,
                widgetLayout
            )
            SHOWCASE.toLowerCase(Locale.getDefault()) -> mapToShowcaseListUiModel(widgetResponse, widgetLayout)
            CARD.lowercase() -> mapToCardDonationUiModel(widgetResponse, widgetLayout)
            BUNDLE.toLowerCase(Locale.getDefault()) -> mapToProductBundleListUiModel(widgetResponse, shopId, widgetLayout)
            else -> {
                null
            }
        }
    }

    private fun mapToBannerTimerWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    )= ShopHomeDisplayBannerItemUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        data = mapToBannerItemWidget(widgetResponse.data.firstOrNull())
    )

    private fun mapToProductPersonalizationUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        isMyProduct: Boolean,
        isEnableDirectPurchase: Boolean,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ) = ShopHomeCarousellProductUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        productList = mapToWidgetProductListPersonalization(widgetResponse.data, isMyProduct, isEnableDirectPurchase)
    )

    private fun mapToShowcaseListUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ) = ShopHomeShowcaseListSliderUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        showcaseListItem = mapToShowcaseListItemUiModel(
            widgetResponse.data,
            widgetResponse.name,
            widgetResponse.header
        )
    )

    private fun mapToCardDonationUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): ShopHomeCardDonationUiModel {
        val header = widgetResponse.header.copy(
            ctaLink = widgetResponse.header.ctaLink + "&need_login=true"
        )
        return ShopHomeCardDonationUiModel(
            widgetId = widgetResponse.widgetID,
            layoutOrder = widgetResponse.layoutOrder,
            name = widgetResponse.name,
            type = widgetResponse.type,
            header = mapToHeaderModel(header),
            isFestivity = widgetLayout?.isFestivity.orFalse(),
        )
    }

    private fun mapToProductBundleListUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        shopId: String,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ) = ShopHomeProductBundleListUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        productBundleList = mapToProductBundleListItemUiModel(widgetResponse.data, widgetResponse.name, shopId)
    )

    private fun mapToProductBundleListItemUiModel(
        widgetData: List<ShopLayoutWidget.Widget.Data>,
        widgetName: String,
        shopId: String
    ): List<ShopHomeProductBundleItemUiModel> {
        return widgetData.map {
            ShopHomeProductBundleItemUiModel().apply {
                bundleGroupId = it.bundleGroupId
                bundleType = widgetName
                bundleName = it.bundleName
                bundleDetails = it.bundleDetails.map { bundleDetail ->
                    ShopHomeProductBundleDetailUiModel().apply {
                        bundleId = bundleDetail.bundleId
                        originalPrice = bundleDetail.originalPrice
                        displayPrice = bundleDetail.displayPrice
                        displayPriceRaw = bundleDetail.displayPriceRaw
                        discountPercentage = bundleDetail.discountPercentage
                        isPreOrder = bundleDetail.isPO
                        isProductsHaveVariant = bundleDetail.isProductsHaveVariant
                        preOrderInfo = bundleDetail.preorderInfo
                        savingAmountWording = bundleDetail.savingAmountWording
                        minOrder = bundleDetail.minOrder
                        minOrderWording = bundleDetail.minOrderWording
                    }
                }
                bundleProducts = it.bundleProducts.map { bundleProduct ->
                    ShopHomeBundleProductUiModel().apply {
                        productId = bundleProduct.productId
                        productName = bundleProduct.productName
                        productImageUrl = bundleProduct.productImageUrl
                        productAppLink = bundleProduct.productAppLink
                    }
                }
                this.shopId = shopId
            }
        }
    }

    private fun mapToNewProductLaunchCampaignUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        isLoggedIn: Boolean,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): ShopHomeNewProductLaunchCampaignUiModel {
        return ShopHomeNewProductLaunchCampaignUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header),
            widgetLayout?.isFestivity.orFalse(),
            mapToListNewProductLaunchCampaignItem(widgetResponse.data, isLoggedIn)
        )
    }

    private fun mapToFlashSaleUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        isEnableDirectPurchase: Boolean,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): ShopHomeFlashSaleUiModel {
        return ShopHomeFlashSaleUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header),
            widgetLayout?.isFestivity.orFalse(),
            mapToFlashSaleUiModelList(widgetResponse.data, isEnableDirectPurchase)
        )
    }

    private fun mapToListNewProductLaunchCampaignItem(
        data: List<ShopLayoutWidget.Widget.Data>,
        isLoggedIn: Boolean
    ): List<ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem> {
        return data.map {
            val isRemindMe =
                if (!isLoggedIn && it.statusCampaign.toLowerCase() == StatusCampaign.UPCOMING.statusCampaign.toLowerCase()) {
                    false
                } else {
                    null
                }
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
                it.voucherWording,
                mapToDynamicRule(it.dynamicRule),
                mapCampaignListBanner(it.listBanner),
                mapCampaignListProduct(it.statusCampaign, it.listProduct),
                isRemindMe
            )
        }
    }

    private fun mapToFlashSaleUiModelList(
        data: List<ShopLayoutWidget.Widget.Data>,
        isEnableDirectPurchase: Boolean
    ): List<ShopHomeFlashSaleUiModel.FlashSaleItem> {
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
                mapCampaignFlashSaleListProduct(it.statusCampaign, it.listProduct, isEnableDirectPurchase),
                false,
                it.backgroundGradientColor.firstColor,
                it.backgroundGradientColor.secondColor
            )
        }
    }

    private fun mapToDynamicRule(dynamicRule: ShopLayoutWidget.Widget.Data.DynamicRule): ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.DynamicRule {
        return ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.DynamicRule(
            dynamicRule.descriptionHeader,
            dynamicRule.dynamicRoleData.map {
                ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem.DynamicRule.DynamicRoleData(
                    ruleID = it.ruleID,
                    isActive = it.isActive
                )
            }
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
                    val stockSoldPercentage = it.stockSoldPercentage.toInt()
                    val showStockBar = it.showStockBar
                    stockLabel = it.stockWording.title.takeIf { showStockBar }.orEmpty()
                    this.stockSoldPercentage = stockSoldPercentage
                }
                hideGimmick = it.hideGimmick
                labelGroupList =
                    it.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
            }
        }
    }

    private fun mapCampaignFlashSaleListProduct(
        statusCampaign: String,
        listProduct: List<ShopLayoutWidget.Widget.Data.Product>,
        isEnableDirectPurchase: Boolean
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
                isSoldOut = it.stock.isZero()
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
                minimumOrder = it.minimumOrder
                maximumOrder = getMaximumOrder(it.stock, it.maximumOrder)
                this.stock = it.stock
                this.isEnableDirectPurchase = isEnableDirectPurchase
                this.isVariant = it.listChildId.isNotEmpty()
                this.listChildId = it.listChildId
                this.parentId = it.parentId
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

    private fun mapToVoucherUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): ShopHomeVoucherUiModel {
        return ShopHomeVoucherUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header),
            widgetLayout?.isFestivity.orFalse()
        )
    }

    private fun mapToDisplayImageWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): ShopHomeDisplayWidgetUiModel {
        return ShopHomeDisplayWidgetUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header),
            widgetLayout?.isFestivity.orFalse(),
            mapToListDisplayWidgetItem(widgetResponse.data)
        )
    }

    private fun mapToThematicWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): ThematicWidgetUiModel {
        return ThematicWidgetUiModel(
            widgetId = widgetResponse.widgetID,
            layoutOrder = widgetResponse.layoutOrder,
            name = widgetResponse.name,
            type = widgetResponse.type,
            header = DynamicHeaderUiModel(
                title = widgetResponse.data.firstOrNull()?.name.orEmpty(),
                subTitle = widgetResponse.data.firstOrNull()?.timeDescription.orEmpty(),
                ctaText = widgetResponse.header.ctaText,
                ctaTextLink = widgetResponse.header.ctaLink,
                statusCampaign = widgetResponse.data.firstOrNull()?.statusCampaign.orEmpty().lowercase(Locale.getDefault()),
                endDate = widgetResponse.data.firstOrNull()?.endDate.orEmpty(),
                timerCounter = widgetResponse.data.firstOrNull()?.timeCounter.orEmpty()
            ),
            widgetMasterId = widgetResponse.widgetMasterID,
            productList = widgetResponse.data.firstOrNull()?.listProduct?.map {
                val labelGroups = it.labelGroups.filter { labelGroup -> labelGroup.position == LABEL_POSITION_INTEGRITY }
                ProductCardUiModel(
                    id = it.id,
                    name = it.name,
                    displayedPrice = it.discountedPrice,
                    originalPrice = it.displayedPrice,
                    discountPercentage = it.discountPercentage,
                    imageUrl = it.imageUrl,
                    imageUrl300 = "",
                    productUrl = it.urlApps,
                    hideGimmick = it.hideGimmick,
                    labelGroupList = labelGroups.map { labelGroup ->
                        com.tokopedia.shop_widget.thematicwidget.uimodel.LabelGroupUiModel(
                            position = labelGroup.position,
                            title = labelGroup.title,
                            url = labelGroup.url,
                            type = labelGroup.type
                        )
                    },
                    rating = it.rating.toDoubleOrZero()
                )
            } ?: listOf(),
            imageBanner = widgetResponse.data.firstOrNull()?.listBanner?.firstOrNull()?.imageUrl.orEmpty(),
            firstBackgroundColor = widgetResponse.data.firstOrNull()?.backgroundGradientColor?.firstColor.orEmpty(),
            secondBackgroundColor = widgetResponse.data.firstOrNull()?.backgroundGradientColor?.secondColor.orEmpty(),
            campaignId = widgetResponse.data.firstOrNull()?.campaignId.orEmpty(),
            isFestivity = widgetLayout?.isFestivity.orFalse()
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
        isMyOwnProduct: Boolean,
        isEnableDirectPurchase: Boolean,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): ShopHomeCarousellProductUiModel {
        return ShopHomeCarousellProductUiModel(
            widgetModel.widgetID,
            widgetModel.layoutOrder,
            widgetModel.name,
            widgetModel.type,
            mapToHeaderModel(widgetModel.header),
            widgetLayout?.isFestivity.orFalse(),
            mapToWidgetProductListItemViewModel(widgetModel.data, isMyOwnProduct, isEnableDirectPurchase)
        )
    }

    private fun mapToHeaderModel(header: ShopLayoutWidget.Widget.Header): BaseShopHomeWidgetUiModel.Header {
        return BaseShopHomeWidgetUiModel.Header(
            header.title,
            header.subtitle,
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
        isMyOwnProduct: Boolean,
        isEnableDirectPurchase: Boolean
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
                categoryBreadcrumbs = it.categoryBreadcrumbs
                labelGroupList = it.labelGroups.map { mapToLabelGroupViewModel(it) }
                minimumOrder = it.minimumOrder
                maximumOrder = getMaximumOrder(it.stock, it.maximumOrder)
                this.stock = it.stock
                this.isEnableDirectPurchase = isEnableDirectPurchase
                this.isVariant = !it.parentId.toLongOrZero().isZero()
                this.listChildId = it.listChildId
                this.parentId = it.parentId
            }
        }
    }

    private fun mapToBannerItemWidget(
        data: ShopLayoutWidget.Widget.Data?,
    ): ShopHomeDisplayBannerItemUiModel.Data {
        return ShopHomeDisplayBannerItemUiModel.Data(
            appLink =  data?.appLink.orEmpty(),
            imageUrl =  data?.imageUrl.orEmpty(),
            linkType =  data?.linkType.orEmpty(),
            campaignId = data?.campaignId.orEmpty(),
            timeDescription = data?.timeInfo?.timeDescription.orEmpty(),
            timeCounter = data?.timeInfo?.timeCounter.orZero(),
            startDate = data?.timeInfo?.startDate.orEmpty(),
            endDate = data?.timeInfo?.endDate.orEmpty(),
            bgColor = data?.timeInfo?.bgColor.orEmpty(),
            textColor = data?.timeInfo?.textColor.orEmpty(),
            status = data?.timeInfo?.status ?: -1,
            totalNotify = data?.totalNotify.orZero(),
            totalNotifyWording = data?.totalNotifyWording.orEmpty()
        )
    }

    private fun mapToShowcaseListItemUiModel(
        data: List<ShopLayoutWidget.Widget.Data>,
        widgetName: String,
        widgetHeader: ShopLayoutWidget.Widget.Header
    ): List<ShopHomeShowcaseListItemUiModel> {
        val uiModelData = data.map {
            ShopHomeShowcaseListItemUiModel().apply {
                id = it.linkId
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
        isMyOwnProduct: Boolean,
        isEnableDirectPurchase: Boolean
    ): List<ShopHomeProductUiModel> {
        return mutableListOf<ShopHomeProductUiModel>().apply {
            data.onEach {
                add(mapToWidgetProductItem(it, isMyOwnProduct, isEnableDirectPurchase))
            }
        }.toList()
    }

    private fun mapToWidgetProductItem(
        response: ShopLayoutWidget.Widget.Data,
        isMyOwnProduct: Boolean,
        isEnableDirectPurchase: Boolean
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
            this.minimumOrder = response.minimumOrder
            maximumOrder = getMaximumOrder(response.stock, response.maximumOrder)
            this.stock = response.stock
            this.isEnableDirectPurchase = isEnableDirectPurchase
            this.isVariant = response.listChildId.isNotEmpty()
            this.listChildId = response.listChildId
            this.parentId = response.parentId
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
    private fun mapCarouselPlayWidget(
        model: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ) = CarouselPlayWidgetUiModel(
        widgetId = model.widgetID,
        layoutOrder = model.layoutOrder,
        name = model.name,
        type = model.type,
        header = mapToHeaderModel(model.header),
        widgetLayout?.isFestivity.orFalse()
    )

    fun mapToListShopHomeWidget(
        responseWidgetData: List<ShopLayoutWidget.Widget>,
        myShop: Boolean,
        isLoggedIn: Boolean,
        isThematicWidgetShown: Boolean,
        isEnableDirectPurchase: Boolean,
        shopId: String,
        listWidgetLayout: List<ShopPageWidgetLayoutUiModel>
    ): List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            responseWidgetData.filter { it.data.isNotEmpty() || it.type.equals(DYNAMIC, ignoreCase = true) || it.name == VOUCHER_STATIC || it.type.equals(CARD, ignoreCase = true) }.onEach {
                when (val widgetUiModel = mapToWidgetUiModel(it, myShop, isLoggedIn, isThematicWidgetShown, isEnableDirectPurchase, shopId, listWidgetLayout.firstOrNull { widgetLayout -> it.widgetID == widgetLayout.widgetId })) {
                    is BaseShopHomeWidgetUiModel -> {
                        widgetUiModel.let { model ->
                            model.widgetMasterId = it.widgetMasterID
                            add(model)
                        }
                    }
                    is ThematicWidgetUiModel -> {
                        widgetUiModel.let { model ->
                            model.widgetMasterId = it.widgetMasterID
                            add(model)
                        }
                    }
                }
            }
        }
    }

    fun mapToShopHomeWidgetLayoutData(response: HomeLayoutData): ShopPageHomeWidgetLayoutUiModel {
        return ShopPageHomeWidgetLayoutUiModel(
            layoutId = response.layoutId,
            masterLayoutId = response.masterLayoutId.toIntOrZero().toString(),
            listWidgetLayout = response.widgetIdList.map {
                ShopPageWidgetLayoutUiModel(
                    it.widgetId,
                    it.widgetMasterId,
                    it.header.title,
                    it.widgetType,
                    it.widgetName,
                    it.isFestivity,
                    ShopPageHeaderUiModel(
                        data = it.header.data.map {
                            ShopPageHeaderDataUiModel(
                                linkType = it.linkType,
                                linkID = it.linkID
                            )
                        }
                    )
                )
            }
        )
    }

    fun mapShopHomeWidgetLayoutToListShopHomeWidget(
        listWidgetLayout: List<ShopPageWidgetLayoutUiModel>,
        myShop: Boolean,
        isLoggedIn: Boolean,
        isThematicWidgetShown: Boolean,
        isEnableDirectPurchase: Boolean,
        shopId: String
    ): List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            listWidgetLayout.onEach {
                mapToWidgetUiModel(
                    ShopLayoutWidget.Widget(
                        widgetID = it.widgetId,
                        type = it.widgetType,
                        name = it.widgetName,
                        header = ShopLayoutWidget.Widget.Header(
                            title = it.widgetTitle
                        ),
                        data = it.header.data.map { headerDataUiModel ->
                            ShopLayoutWidget.Widget.Data(bundleGroupId = headerDataUiModel.linkID.toString())
                        }
                    ),
                    myShop,
                    isLoggedIn,
                    isThematicWidgetShown,
                    isEnableDirectPurchase,
                    shopId,
                    it
                )?.let { resModel ->
                    when (resModel) {
                        is BaseShopHomeWidgetUiModel -> {
                            resModel.widgetMasterId = it.widgetMasterId
                            add(resModel)
                        }
                        is ThematicWidgetUiModel -> {
                            resModel.widgetMasterId = it.widgetMasterId
                            add(resModel)
                        }
                    }
                }
            }
        }
    }

    private fun getMaximumOrder(stock: Int, maximumOrder: Int): Int {
        return maximumOrder.takeIf { !it.isZero() } ?: stock
    }

    private fun getMaximumOrderForGetShopProduct(shopProductResponse: ShopProduct): Int {
        return shopProductResponse.campaign.maxOrder.takeIf { !it.isZero() }
            ?: shopProductResponse.maximumOrder.takeIf { !it.isZero() }
            ?: shopProductResponse.campaign.customStock.toIntOrZero().takeIf { !it.isZero() }
            ?: shopProductResponse.stock
    }

    fun mapToShopHomeProductBundleDetailUiModel(
        bundleDetailUiModel: BundleDetailUiModel,
        bundleListUiModel: ShopHomeProductBundleListUiModel?
    ): ShopHomeProductBundleDetailUiModel {
        return ShopHomeProductBundleDetailUiModel(
            bundleId = bundleDetailUiModel.bundleId,
            originalPrice = bundleDetailUiModel.originalPrice,
            displayPrice = bundleDetailUiModel.displayPrice,
            displayPriceRaw = bundleDetailUiModel.displayPriceRaw,
            discountPercentage = bundleDetailUiModel.discountPercentage,
            isPreOrder = bundleDetailUiModel.isPreOrder,
            isProductsHaveVariant = bundleDetailUiModel.products.firstOrNull()?.hasVariant ?: false,
            preOrderInfo = bundleDetailUiModel.preOrderInfo,
            savingAmountWording = bundleDetailUiModel.savingAmountWording,
            minOrder = bundleDetailUiModel.minOrder,
            minOrderWording = bundleDetailUiModel.minOrderWording,
            isSelected = bundleDetailUiModel.isSelected,
            isFestivity = bundleListUiModel?.isFestivity.orFalse(),
            widgetId = bundleListUiModel?.widgetId.orEmpty(),
            widgetMasterId = bundleListUiModel?.widgetMasterId.orEmpty()
        )
    }

    fun mapToShopHomeBundleProductUiModel(bundleProductUiModel: BundleProductUiModel): ShopHomeBundleProductUiModel {
        return ShopHomeBundleProductUiModel(
            productId = bundleProductUiModel.productId,
            productName = bundleProductUiModel.productName,
            productImageUrl = bundleProductUiModel.productImageUrl,
            productAppLink = bundleProductUiModel.productAppLink
        )
    }
}
