package com.tokopedia.shop.home.util.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.campaign.WidgetName.FLASH_SALE_TOKO
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopPageHeaderDataUiModel
import com.tokopedia.shop.common.data.model.ShopPageHeaderUiModel
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroupStyle
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.productcard.ShopProductCardColorHelper
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.home.HomeConstant.IS_SHOW_ETALASE_NAME
import com.tokopedia.shop.home.WidgetNameEnum
import com.tokopedia.shop.home.WidgetTypeEnum
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.data.model.ShopPageWidgetRequestModel
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeShowcaseListBaseWidgetViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.GetCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.ShopBmsmWidgetGwpUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCampaignNplTncUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCardDonationUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleTncUiModel
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomePersoProductComparisonUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductBundleListUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopPageLayoutUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.thematicwidget.ThematicWidgetUiModel
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.view.datamodel.LabelGroupUiModel
import com.tokopedia.shop.product.view.datamodel.ShopBadgeUiModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingDetail
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoByShopIdUiModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.Product
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

object ShopPageHomeMapper {
    private const val PRODUCT_RATING_DIVIDER = 20
    private const val ZERO_PRODUCT_DISCOUNT = "0"

    private val productCardColorHelper = ShopProductCardColorHelper()

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
                it.averageRating = stats.averageRating
                it.isFulfillment = ShopUtil.isFulfillmentByGroupLabel(shopProduct.labelGroupList)
                it.warehouseId = shopProduct.warehouseId
                it.shopBadgeList = shopProduct.badge.map { badge ->
                    ShopBadgeUiModel(
                        title = badge.title,
                        imageUrl = badge.imageUrl,
                        show = true
                    )
                }
            }
        }

    private fun mapToLabelGroupViewModel(labelGroup: LabelGroup): LabelGroupUiModel {
        return LabelGroupUiModel(
            position = labelGroup.position,
            title = labelGroup.title,
            type = labelGroup.type,
            url = labelGroup.url,
            styles = labelGroup.styles.map {
                LabelGroupStyle(key = it.key, value = it.value)
            }
        )
    }

    fun mapToProductCardPersonalizationModel(
        shopHomeProductViewModel: ShopHomeProductUiModel,
        isHasATC: Boolean,
        isHasOCCButton: Boolean,
        occButtonText: String = "",
        widgetName: String = "",
        isOverrideTheme: Boolean,
        patternColorType: String,
        backgroundColor: String,
        isFestivity: Boolean,
        atcButtonText: String
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

        val colorMode = productCardColorHelper.determineProductCardColorMode(
            isFestivity = isFestivity,
            shouldOverrideTheme = isOverrideTheme,
            patternColorType = patternColorType,
            backgroundColor = backgroundColor,
            makeProductCardTransparent = true
        )

        val baseProductCardModel = if (isHasOCCButton) {
            ProductCardModel(
                productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
                productName = shopHomeProductViewModel.name,
                discountPercentage = discountPercentage,
                slashedPrice = shopHomeProductViewModel.originalPrice ?: "",
                formattedPrice = shopHomeProductViewModel.displayedPrice,
                hasAddToCartButton = isHasATC,
                addToCartButtonType = UnifyButton.Type.MAIN,
                addToCardText = occButtonText,
                countSoldRating = if (shopHomeProductViewModel.rating != 0.0) shopHomeProductViewModel.rating.toString() else "",
                freeOngkir = freeOngkirObject,
                labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                    mapToProductCardLabelGroup(it)
                },
                shopBadgeList = shopHomeProductViewModel.shopBadgeList.map { badge ->
                    ProductCardModel.ShopBadge(
                        title = badge.title,
                        imageUrl = badge.imageUrl,
                        isShown = badge.show
                    )
                },
                forceLightModeColor = isOverrideTheme,
                colorMode = colorMode
            )
        } else {
            ProductCardModel(
                productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
                productName = shopHomeProductViewModel.name,
                discountPercentage = discountPercentage,
                slashedPrice = shopHomeProductViewModel.originalPrice ?: "",
                formattedPrice = shopHomeProductViewModel.displayedPrice,
                countSoldRating = if (shopHomeProductViewModel.rating != 0.0) shopHomeProductViewModel.rating.toString() else "",
                freeOngkir = freeOngkirObject,
                labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                    mapToProductCardLabelGroup(it)
                },
                shopBadgeList = shopHomeProductViewModel.shopBadgeList.map { badge ->
                    ProductCardModel.ShopBadge(
                        title = badge.title,
                        imageUrl = badge.imageUrl,
                        isShown = badge.show
                    )
                },
                hasAddToCartButton = isHasATC,
                forceLightModeColor = isOverrideTheme,
                colorMode = colorMode
            )
        }
        return if (isShopPersonalizationWidgetEnableDirectPurchase(
                shopHomeProductViewModel.isEnableDirectPurchase,
                widgetName
            ) && isProductCardIsNotSoldOut(shopHomeProductViewModel.isSoldOut)
        ) {
            if (shopHomeProductViewModel.isVariant) {
                createProductCardWithVariantAtcModel(shopHomeProductViewModel, baseProductCardModel, atcButtonText)
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
        return enableDirectPurchase && (widgetName == WidgetNameEnum.RECENT_ACTIVITY.value || widgetName == WidgetNameEnum.REMINDER.value)
    }

    fun mapToProductCardModel(
        isHasAddToCartButton: Boolean,
        hasThreeDots: Boolean,
        shopHomeProductViewModel: ShopHomeProductUiModel,
        isWideContent: Boolean,
        productRating: String,
        isOverrideTheme: Boolean,
        patternColorType: String,
        backgroundColor: String,
        isFestivity: Boolean,
        makeProductCardTransparent: Boolean,
        atcVariantButtonText: String
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

        val badges = shopHomeProductViewModel.shopBadgeList.map {
            ProductCardModel.ShopBadge(
                isShown = true,
                imageUrl = it.imageUrl,
                title = it.title
            )
        }

        val productCardColorMode = productCardColorHelper.determineProductCardColorMode(
            isFestivity = isFestivity,
            shouldOverrideTheme = isOverrideTheme,
            patternColorType = patternColorType,
            backgroundColor = backgroundColor,
            makeProductCardTransparent = makeProductCardTransparent
        )
        val baseProductCardModel = ProductCardModel(
            productImageUrl = shopHomeProductViewModel.imageUrl ?: "",
            productName = shopHomeProductViewModel.name,
            discountPercentage = discountPercentage,
            slashedPrice = shopHomeProductViewModel.originalPrice ?: "",
            formattedPrice = shopHomeProductViewModel.displayedPrice,
            countSoldRating = productRating,
            freeOngkir = freeOngkirObject,
            labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                mapToProductCardLabelGroup(it)
            },
            hasThreeDots = hasThreeDots,
            hasAddToCartButton = isHasAddToCartButton,
            addToCartButtonType = UnifyButton.Type.MAIN,
            isWideContent = isWideContent,
            isWishlisted = shopHomeProductViewModel.isWishList,
            forceLightModeColor = isOverrideTheme,
            colorMode = productCardColorMode,
            shopBadgeList = badges
        )
        return if (shopHomeProductViewModel.isEnableDirectPurchase && isProductCardIsNotSoldOut(
                shopHomeProductViewModel.isSoldOut
            )
        ) {
            val productCardModel = if (shopHomeProductViewModel.isVariant) {
                createProductCardWithVariantAtcModel(shopHomeProductViewModel, baseProductCardModel, atcVariantButtonText)
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
        baseProductCardModel: ProductCardModel,
        atcButtonText: String
    ): ProductCardModel {
        return baseProductCardModel.copy(
            variant = ProductCardModel.Variant(
                shopHomeProductViewModel.productInCart
            ),
            nonVariant = null,
            hasAddToCartButton = false,
            productCardGenericCta = ProductCardModel.ProductCardGenericCta(
                copyWriting = atcButtonText,
                mainButtonVariant = UnifyButton.Variant.GHOST,
                mainButtonType = UnifyButton.Type.MAIN
            )
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
        statusCampaign: String,
        isOverrideTheme: Boolean,
        patternColorType: String,
        backgroundColor: String,
        isFestivity: Boolean,
        makeProductCardTransparent: Boolean = false,
        atcVariantButtonText: String
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
            productName = shopHomeProductViewModel.name,
            discountPercentage = discountPercentage.takeIf { !shopHomeProductViewModel.hideGimmick }
                ?: "",
            slashedPrice = shopHomeProductViewModel.originalPrice.orEmpty()
                .takeIf { !shopHomeProductViewModel.hideGimmick } ?: "",
            formattedPrice = shopHomeProductViewModel.displayedPrice,
            countSoldRating = if (shopHomeProductViewModel.rating != 0.0) shopHomeProductViewModel.rating.toString() else "",
            freeOngkir = freeOngkirObject,
            labelGroupList = shopHomeProductViewModel.labelGroupList.map {
                mapToProductCardLabelGroup(it)
            },
            hasThreeDots = hasThreeDots,
            hasAddToCartButton = isHasAddToCartButton,
            addToCartButtonType = UnifyButton.Type.MAIN,
            stockBarLabel = shopHomeProductViewModel.stockLabel,
            stockBarPercentage = shopHomeProductViewModel.stockSoldPercentage,
            forceLightModeColor = isOverrideTheme,
            shopBadgeList = shopHomeProductViewModel.shopBadgeList.map {
                ProductCardModel.ShopBadge(
                    isShown = false,
                    imageUrl = it.imageUrl,
                    title = it.title
                )
            },
            colorMode = productCardColorHelper.determineProductCardColorMode(
                isFestivity = isFestivity,
                shouldOverrideTheme = isOverrideTheme,
                patternColorType = patternColorType,
                backgroundColor = backgroundColor,
                makeProductCardTransparent = makeProductCardTransparent
            )
        )
        return if (isShopCampaignWidgetEnableDirectPurchase(
                shopHomeProductViewModel.isEnableDirectPurchase,
                widgetName
            ) && isProductCardIsNotSoldOut(shopHomeProductViewModel.isSoldOut) &&
            isStatusCampaignIsOngoing(statusCampaign)
        ) {
            if (shopHomeProductViewModel.isVariant) {
                createProductCardWithVariantAtcModel(shopHomeProductViewModel, baseProductCardModel, atcVariantButtonText)
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
        return enableDirectPurchase && widgetName == WidgetNameEnum.FLASH_SALE_TOKO.value
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
            imageUrl = labelGroupUiModel.url,
            styleList = labelGroupUiModel.styles.map { style ->
                ProductCardModel.LabelGroup.Style(
                    key = style.key,
                    value = style.value
                )
            }
        )
    }

    private fun mapToWidgetUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        isMyOwnProduct: Boolean,
        isLoggedIn: Boolean,
        isEnableDirectPurchase: Boolean,
        shopId: String,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): Visitable<*>? {
        if (widgetResponse.name == WidgetNameEnum.VOUCHER_STATIC.value) {
            return mapToVoucherUiModel(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
        }
        return when (widgetResponse.type.lowercase()) {
            WidgetTypeEnum.DISPLAY.value.lowercase() -> {
                when (widgetResponse.name) {
                    WidgetNameEnum.DISPLAY_SINGLE_COLUMN.value,
                    WidgetNameEnum.DISPLAY_DOUBLE_COLUMN.value,
                    WidgetNameEnum.DISPLAY_TRIPLE_COLUMN.value,
                    WidgetNameEnum.SLIDER_BANNER.value,
                    WidgetNameEnum.SLIDER_SQUARE_BANNER.value,
                    WidgetNameEnum.VIDEO.value,
                    WidgetNameEnum.ADVANCED_SLIDER_BANNER.value -> {
                        mapToDisplayImageWidget(
                            widgetResponse,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema
                        )
                    }

                    WidgetNameEnum.BANNER_TIMER.value -> {
                        ShopPageWidgetMapper.mapToBannerTimerWidget(
                            widgetResponse,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema
                        )
                    }

                    WidgetNameEnum.SHOWCASE_NAVIGATION_BANNER.value -> {
                        ShopPageWidgetMapper.mapToHomeShowcaseNavigationWidget(
                            widgetResponse,
                            isOverrideTheme,
                            widgetLayout,
                            colorSchema
                        )
                    }

                    WidgetNameEnum.REIMAGINE_BANNER_PRODUCT_HOTSPOT.value -> {
                        ShopPageWidgetMapper.mapToBannerProductHotspotWidget(
                            widgetResponse,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema
                        )
                    }

                    else -> null
                }
            }
            // Includes V4 Widgets: Terlaris widget
            WidgetTypeEnum.PRODUCT.value.lowercase() -> {
                when (widgetResponse.name) {
                    WidgetNameEnum.PRODUCT.value -> {
                        mapToProductWidgetUiModel(
                            widgetModel = widgetResponse,
                            isMyOwnProduct = isMyOwnProduct,
                            isEnableDirectPurchase = isEnableDirectPurchase,
                            widgetLayout = widgetLayout,
                            isOverrideTheme = isOverrideTheme,
                            colorSchema = colorSchema
                        )
                    }

                    WidgetNameEnum.PRODUCT_VERTICAL.value -> {
                        mapToProductTerlarisWidgetUiModel(
                            widgetModel = widgetResponse,
                            isMyOwnProduct = isMyOwnProduct,
                            isEnableDirectPurchase = isEnableDirectPurchase,
                            widgetLayout = widgetLayout,
                            isOverrideTheme = isOverrideTheme,
                            colorSchema = colorSchema
                        )
                    }

                    else -> null
                }
            }
            WidgetTypeEnum.CAMPAIGN.value.lowercase() -> {
                when (widgetResponse.name) {
                    WidgetNameEnum.ETALASE_THEMATIC.value -> mapToThematicWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
                    WidgetNameEnum.BIG_CAMPAIGN_THEMATIC.value -> mapToThematicWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
                    FLASH_SALE_TOKO -> mapToFlashSaleUiModel(widgetResponse, isEnableDirectPurchase, widgetLayout, isOverrideTheme, colorSchema)
                    WidgetNameEnum.NEW_PRODUCT_LAUNCH_CAMPAIGN.value -> mapToNewProductLaunchCampaignUiModel(
                        widgetResponse,
                        isLoggedIn,
                        widgetLayout,
                        isOverrideTheme,
                        colorSchema
                    )
                    else -> null
                }
            }
            WidgetTypeEnum.DYNAMIC.value.lowercase() -> mapCarouselPlayWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
            WidgetTypeEnum.REIMAGINE_COMPONENT.value.lowercase() -> {
                when (widgetResponse.name) {
                    WidgetNameEnum.BANNER_PRODUCT_GROUP.value -> ShopPageWidgetMapper.mapToHomeBannerProductGroupWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
                    else -> null
                }
            }

            WidgetTypeEnum.PERSONALIZATION.value.lowercase() -> {
                when (widgetResponse.name) {
                    WidgetNameEnum.BUY_AGAIN.value,
                    WidgetNameEnum.RECENT_ACTIVITY.value,
                    WidgetNameEnum.REMINDER.value,
                    WidgetNameEnum.ADD_ONS.value,
                    WidgetNameEnum.TRENDING.value -> {
                        mapToProductPersonalizationUiModel(
                            widgetResponse,
                            isMyOwnProduct,
                            isEnableDirectPurchase,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema,
                            shopId
                        )
                    }

                    WidgetNameEnum.PERSO_PRODUCT_COMPARISON.value -> {
                        mapToPersoProductComparisonUiModel(
                            widgetResponse,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema
                        )
                    }

                    else -> {
                        null
                    }
                }
            }

            WidgetTypeEnum.SHOWCASE.value.lowercase() -> mapToShowcaseListUiModel(
                widgetResponse,
                widgetLayout,
                isOverrideTheme,
                colorSchema
            )

            WidgetTypeEnum.CARD.value.lowercase() -> mapToCardDonationUiModel(
                widgetResponse,
                widgetLayout,
                isOverrideTheme,
                colorSchema
            )

            WidgetTypeEnum.BUNDLE.value.lowercase() -> mapToProductBundleListUiModel(
                widgetResponse,
                shopId,
                widgetLayout,
                isOverrideTheme,
                colorSchema
            )

            WidgetTypeEnum.REIMAGINE_DIRECT_PURCHASE.value.lowercase() -> mapToDirectPurchaseTypeWidget(
                widgetResponse,
                widgetLayout,
                isOverrideTheme,
                colorSchema
            )

            WidgetTypeEnum.GROUP_OFFERING_PRODUCT.value.lowercase() ->
                when (widgetResponse.name) {
                    WidgetNameEnum.BMSM_GWP_OFFERING_GROUP.value -> {
                        mapToBmsmWidget(
                            widgetResponse,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema,
                            shopId.toLongOrZero()
                        )
                    }

                    WidgetNameEnum.BMSM_PD_OFFERING_GROUP.value -> {
                        mapToBmsmWidget(
                            widgetResponse,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema,
                            shopId.toLongOrZero()
                        )
                    }

                    else -> null
                }

            else -> {
                null
            }
        }
    }

    private fun mapToDirectPurchaseTypeWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): Visitable<*>? {
        return when (widgetResponse.name) {
            WidgetNameEnum.DIRECT_PURCHASED_BY_ETALASE.value -> {
                ShopPageWidgetMapper.mapToDirectPurchaseByEtalase(
                    widgetResponse,
                    widgetLayout,
                    isOverrideTheme,
                    colorSchema
                )
            }

            else -> {
                null
            }
        }
    }

    private fun mapToProductPersonalizationUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        isMyProduct: Boolean,
        isEnableDirectPurchase: Boolean,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema,
        shopId: String
    ) = ShopHomeCarousellProductUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        productList = mapToWidgetProductListPersonalization(widgetResponse, isMyProduct, isEnableDirectPurchase, shopId, widgetResponse.name),
        shopId = shopId
    )

    private fun mapToPersoProductComparisonUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) = ShopHomePersoProductComparisonUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
        isFestivity = widgetLayout?.isFestivity.orFalse()
    )

    private fun mapToShowcaseListUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) = ShopHomeShowcaseListSliderUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        showcaseListItem = mapToShowcaseListItemUiModel(
            widgetResponse.data,
            widgetResponse.name,
            widgetResponse.header
        )
    )

    private fun mapToCardDonationUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ShopHomeCardDonationUiModel {
        val header = widgetResponse.header.copy(
            ctaLink = widgetResponse.header.ctaLink + "&need_login=true"
        )
        return ShopHomeCardDonationUiModel(
            widgetId = widgetResponse.widgetID,
            layoutOrder = widgetResponse.layoutOrder,
            name = widgetResponse.name,
            type = widgetResponse.type,
            header = mapToHeaderModel(header, widgetLayout, isOverrideTheme, colorSchema),
            isFestivity = widgetLayout?.isFestivity.orFalse()
        )
    }

    fun mapToProductBundleListUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        shopId: String,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) = ShopHomeProductBundleListUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
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
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ShopHomeNewProductLaunchCampaignUiModel {
        return ShopHomeNewProductLaunchCampaignUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
            widgetLayout?.isFestivity.orFalse(),
            mapToListNewProductLaunchCampaignItem(widgetResponse.data, isLoggedIn)
        )
    }

    private fun mapToFlashSaleUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        isEnableDirectPurchase: Boolean,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ShopHomeFlashSaleUiModel {
        return ShopHomeFlashSaleUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
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
                if (!isLoggedIn && it.statusCampaign.lowercase() == StatusCampaign.UPCOMING.statusCampaign.lowercase()) {
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
                ShopPageWidgetMapper.mapToDynamicRule(it.dynamicRule),
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

    internal fun mapCampaignListProduct(
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
                if (statusCampaign.lowercase() == StatusCampaign.ONGOING.statusCampaign.lowercase()) {
                    val stockSoldPercentage = it.stockSoldPercentage.toInt()
                    val showStockBar = it.showStockBar
                    stockLabel = it.stockWording.title.takeIf { showStockBar }.orEmpty()
                    this.stockSoldPercentage = stockSoldPercentage
                }
                hideGimmick = it.hideGimmick
                labelGroupList =
                    it.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
                isFulfillment = ShopUtil.isFulfillmentByGroupLabel(it.labelGroups)
                warehouseId = it.warehouseId
            }
        }
    }

    internal fun mapCampaignCarouselListProduct(
        listProduct: List<ShopLayoutWidget.Widget.Data.Product>
    ): List<ShopHomeProductUiModel> {
        return listProduct.map {
            val stockSoldPercentage = it.stockSoldPercentage.toInt()
            val showStockBar = it.showStockBar
            ShopHomeProductUiModel().apply {
                id = it.id
                name = it.name
                displayedPrice = it.discountedPrice
                originalPrice = it.displayedPrice
                discountPercentage = it.discountPercentage
                imageUrl = it.imageUrl
                imageUrl300 = ""
                productUrl = it.urlApps
                stockLabel = it.stockWording.title.takeIf { showStockBar }.orEmpty()
                this.stockSoldPercentage = stockSoldPercentage
                hideGimmick = it.hideGimmick
                labelGroupList =
                    it.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
                isFulfillment = ShopUtil.isFulfillmentByGroupLabel(it.labelGroups)
                warehouseId = it.warehouseId
            }
        }
    }

    private fun mapCampaignFlashSaleListProduct(
        statusCampaign: String,
        listProduct: List<ShopLayoutWidget.Widget.Data.Product>,
        isEnableDirectPurchase: Boolean
    ): List<ShopHomeProductUiModel> {
        return listProduct.map {
            val showStockBar = it.showStockBar
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
                        stockLabel = it.stockWording.title.takeIf { showStockBar }.orEmpty()
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
                isFulfillment = ShopUtil.isFulfillmentByGroupLabel(it.labelGroups)
                warehouseId = it.warehouseId
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
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ShopHomeVoucherUiModel {
        return ShopHomeVoucherUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
            widgetLayout?.isFestivity.orFalse()
        )
    }

    fun mapToDisplayImageWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ShopHomeDisplayWidgetUiModel {
        return ShopHomeDisplayWidgetUiModel(
            widgetResponse.widgetID,
            widgetResponse.layoutOrder,
            widgetResponse.name,
            widgetResponse.type,
            mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
            widgetLayout?.isFestivity.orFalse(),
            mapToListDisplayWidgetItem(widgetResponse.data)
        )
    }

    private fun mapToThematicWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ThematicWidgetUiModel {
        return ThematicWidgetUiModel(
            widgetId = widgetResponse.widgetID,
            layoutOrder = widgetResponse.layoutOrder,
            name = widgetResponse.name,
            type = widgetResponse.type,
            header = mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
            isFestivity = widgetLayout?.isFestivity.orFalse(),
            campaignName = widgetResponse.data.firstOrNull()?.name.orEmpty(),
            campaignSubName = widgetResponse.data.firstOrNull()?.timeDescription.orEmpty(),
            statusCampaign = widgetResponse.data.firstOrNull()?.statusCampaign.orEmpty().lowercase(Locale.getDefault()),
            endDate = widgetResponse.data.firstOrNull()?.endDate.orEmpty(),
            timerCounter = widgetResponse.data.firstOrNull()?.timeCounter.orEmpty(),
            productList = mapThematicWidgetProductList(widgetResponse.data.firstOrNull()?.listProduct.orEmpty()),
            imageBanner = widgetResponse.data.firstOrNull()?.listBanner?.firstOrNull()?.imageUrl.orEmpty(),
            firstBackgroundColor = widgetResponse.data.firstOrNull()?.backgroundGradientColor?.firstColor.orEmpty(),
            secondBackgroundColor = widgetResponse.data.firstOrNull()?.backgroundGradientColor?.secondColor.orEmpty(),
            campaignId = widgetResponse.data.firstOrNull()?.campaignId.orEmpty()
        )
    }

    private fun mapThematicWidgetProductList(listProduct: List<ShopLayoutWidget.Widget.Data.Product>): List<ShopHomeProductUiModel> {
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
                labelGroupList = it.labelGroups.map { labelGroup -> mapToLabelGroupViewModel(labelGroup) }
                rating = it.rating.toDouble()
                isFulfillment = ShopUtil.isFulfillmentByGroupLabel(it.labelGroups)
                warehouseId = it.warehouseId
            }
        }
    }

    private fun mapToListDisplayWidgetItem(
        data: List<ShopLayoutWidget.Widget.Data>
    ): List<ShopHomeDisplayWidgetUiModel.DisplayWidgetItem> {
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
            data.videoUrl,
            data.bannerId,
            isFulfillment = ShopUtil.isFulfillmentByGroupLabel(data.labelGroups),
            warehouseId = data.warehouseID
        )
    }

    private fun mapToProductWidgetUiModel(
        widgetModel: ShopLayoutWidget.Widget,
        isMyOwnProduct: Boolean,
        isEnableDirectPurchase: Boolean,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ShopHomeCarousellProductUiModel {
        return ShopHomeCarousellProductUiModel(
            widgetId = widgetModel.widgetID,
            layoutOrder = widgetModel.layoutOrder,
            name = widgetModel.name,
            type = widgetModel.type,
            header = mapToHeaderModel(widgetModel.header, widgetLayout, isOverrideTheme, colorSchema),
            isFestivity = widgetLayout?.isFestivity.orFalse(),
            productList = mapToWidgetProductListItemViewModel(
                data = widgetModel.data,
                isMyOwnProduct = isMyOwnProduct,
                isEnableDirectPurchase = isEnableDirectPurchase
            )
        )
    }

    private fun mapToProductTerlarisWidgetUiModel(
        widgetModel: ShopLayoutWidget.Widget,
        isMyOwnProduct: Boolean,
        isEnableDirectPurchase: Boolean,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): ShopHomeCarousellProductUiModel {
        return ShopHomeCarousellProductUiModel(
            widgetId = widgetModel.widgetID,
            layoutOrder = widgetModel.layoutOrder,
            name = widgetModel.name,
            type = widgetModel.type,
            header = mapToHeaderModel(widgetModel.header, widgetLayout, isOverrideTheme, colorSchema),
            isFestivity = widgetLayout?.isFestivity.orFalse(),
            productList = mapToWidgetProductListItemViewModel(
                data = widgetModel.data,
                isMyOwnProduct = isMyOwnProduct,
                isEnableDirectPurchase = isEnableDirectPurchase
            )
        )
    }

    fun mapToHeaderModel(
        header: ShopLayoutWidget.Widget.Header,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): BaseShopHomeWidgetUiModel.Header {
        return BaseShopHomeWidgetUiModel.Header(
            header.title,
            header.subtitle,
            header.ctaText,
            header.ctaLink,
            header.cover,
            header.ratio,
            header.isAtc,
            header.etalaseId,
            isOverrideTheme,
            colorSchema,
            mapToHeaderData(widgetLayout)
        )
    }

    private fun mapToHeaderData(data: ShopPageWidgetUiModel?): List<BaseShopHomeWidgetUiModel.Header.Data> {
        return data?.header?.data?.map {
            BaseShopHomeWidgetUiModel.Header.Data(
                it.linkType,
                it.linkID,
                it.link
            )
        }.orEmpty()
    }

    private fun mapToWidgetProductListPersonalization(
        widgets: ShopLayoutWidget.Widget,
        isMyOwnProduct: Boolean,
        isEnableDirectPurchase: Boolean,
        shopId: String,
        widgetName: String
    ): List<ShopHomeProductUiModel> {
        return widgets.let {
            val appLog = it.tracker.appLog
            it.data.map {
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
                    isFulfillment = ShopUtil.isFulfillmentByGroupLabel(it.labelGroups)
                    warehouseId = it.warehouseID
                    this.shopId = shopId
                    this.widgetName = widgetName
                    this.recommendationPageName = it.recommendationPageName
                    this.recParam = it.recParam
                    this.recSessionId = appLog.sessionId
                    this.requestId = appLog.requestId
                    this.shopBadgeList = listOf(
                        ShopBadgeUiModel(
                            title = it.badge.title,
                            imageUrl = it.badge.imageUrl,
                            show = true
                        )
                    )
                }
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
                id = it.linkId
                imageUrl = it.imageUrl
                appLink = it.appLink
                name = it.showcaseName
                viewType = widgetName
                isShowEtalaseName = widgetHeader.isShowEtalaseName == IS_SHOW_ETALASE_NAME
                isFulfilment = ShopUtil.isFulfillmentByGroupLabel(it.labelGroups)
                warehouseId = it.warehouseID
            }
        }
        return if (widgetName == WidgetNameEnum.SHOWCASE_SLIDER_TWO_ROWS.value) {
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
            id = response.productID
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
            isFulfillment = ShopUtil.isFulfillmentByGroupLabel(response.labelGroups)
            warehouseId = response.warehouseID
            shopBadgeList = listOf(
                ShopBadgeUiModel(
                    title = response.badge.title,
                    imageUrl = response.badge.imageUrl,
                    show = response.badge.show
                )
            )
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
    fun mapCarouselPlayWidget(
        model: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) = CarouselPlayWidgetUiModel(
        widgetId = model.widgetID,
        layoutOrder = model.layoutOrder,
        name = model.name,
        type = model.type,
        header = mapToHeaderModel(model.header, widgetLayout, isOverrideTheme, colorSchema),
        widgetLayout?.isFestivity.orFalse()
    )

    fun mapToListShopHomeWidget(
        responseWidgetData: List<ShopLayoutWidget.Widget>,
        myShop: Boolean,
        isLoggedIn: Boolean,
        isEnableDirectPurchase: Boolean,
        shopId: String,
        listWidgetLayout: List<ShopPageWidgetUiModel>,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            responseWidgetData.filter { it.data.isNotEmpty() || it.type.equals(WidgetTypeEnum.DYNAMIC.value, ignoreCase = true) || it.name == WidgetNameEnum.VOUCHER_STATIC.value || it.type.equals(WidgetTypeEnum.CARD.value, ignoreCase = true) }.onEach {
                when (val widgetUiModel = mapToWidgetUiModel(it, myShop, isLoggedIn, isEnableDirectPurchase, shopId, listWidgetLayout.firstOrNull { widgetLayout -> it.widgetID == widgetLayout.widgetId }, isOverrideTheme, colorSchema)) {
                    is BaseShopHomeWidgetUiModel -> {
                        widgetUiModel.let { model ->
                            model.widgetMasterId = it.widgetMasterID
                            add(model)
                        }
                    }
                }
            }
        }
    }

    fun mapToShopHomeWidgetLayoutData(response: HomeLayoutData): ShopPageLayoutUiModel {
        return ShopPageLayoutUiModel(
            layoutId = response.layoutId,
            masterLayoutId = response.masterLayoutId.toIntOrZero().toString(),
            listWidgetLayout = response.widgetIdList.filter {
                checkIfWidgetNameRegistered(it.widgetName)
            }.map {
                ShopPageWidgetUiModel(
                    it.widgetId,
                    it.widgetMasterId,
                    it.header.title,
                    it.widgetType,
                    it.widgetName,
                    it.isFestivity,
                    ShopPageHeaderUiModel(
                        title = it.header.title,
                        subtitle = it.header.subtitle,
                        ctaText = it.header.ctaText,
                        ctaLink = it.header.ctaLink,
                        isAtc = it.header.isAtc,
                        etalaseId = it.header.etalaseId,
                        isShowEtalaseName = it.header.isShowEtalaseName,
                        data = it.header.data.map {
                            ShopPageHeaderDataUiModel(
                                linkType = it.linkType,
                                linkID = it.linkID,
                                link = it.link
                            )
                        },
                        widgetStyle = it.header.widgetStyle
                    ),
                    it.options.map { option ->
                        ShopPageWidgetRequestModel.Option(
                            key = option.key,
                            value = option.value
                        )
                    }
                )
            }
        )
    }

    private fun checkIfWidgetNameRegistered(widgetName: String): Boolean {
        return WidgetNameEnum.values().map { it.value }.contains(widgetName)
    }

    fun mapShopHomeWidgetLayoutToListShopHomeWidget(
        listWidgetLayout: List<ShopPageWidgetUiModel>,
        myShop: Boolean,
        isLoggedIn: Boolean,
        isEnableDirectPurchase: Boolean,
        shopId: String,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            listWidgetLayout.onEach {
                mapToWidgetUiModel(
                    ShopLayoutWidget.Widget(
                        widgetID = it.widgetId,
                        type = it.widgetType,
                        name = it.widgetName,
                        header = ShopLayoutWidget.Widget.Header(
                            title = it.widgetTitle,
                            widgetStyle = it.header.widgetStyle
                        ),
                        data = it.header.data.map { headerDataUiModel ->
                            ShopLayoutWidget.Widget.Data(bundleGroupId = headerDataUiModel.linkID.toString())
                        }
                    ),
                    myShop,
                    isLoggedIn,
                    isEnableDirectPurchase,
                    shopId,
                    it,
                    isOverrideTheme,
                    colorSchema
                )?.let { resModel ->
                    when (resModel) {
                        is BaseShopHomeWidgetUiModel -> {
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

    private fun mapToBmsmWidget(
        response: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema,
        shopId: Long
    ) = ShopBmsmWidgetGwpUiModel(
        widgetId = response.widgetID,
        layoutOrder = response.layoutOrder,
        name = response.name,
        type = response.type,
        header = mapToHeaderModel(response.header, widgetLayout, isOverrideTheme, colorSchema),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        data = mapToBmsmItem(response.data, shopId)
    )

    private fun mapToBmsmItem(
        listData: List<ShopLayoutWidget.Widget.Data>,
        shopId: Long
    ): List<OfferingInfoByShopIdUiModel> {
        return listData.map {
            OfferingInfoByShopIdUiModel(
                offerId = it.offerId,
                shopId = shopId,
                offerType = it.offerType,
                offerName = it.offerName,
                warehouseIds = it.warehouseIds,
                thumbnails = it.thumbnails,
                offeringDetail = OfferingDetail(
                    termAndConditions = it.offeringDetail.termAndConditions,
                    startDate = it.offeringDetail.startDate,
                    endDate = it.offeringDetail.endDate,
                    tierList = it.offeringDetail.tierList.map { tier ->
                        OfferingDetail.Tier(
                            tierId = tier.tierId,
                            level = tier.level,
                            tierWording = tier.tierWording,
                            rules = tier.rules.map { rule ->
                                OfferingDetail.Tier.Rule(
                                    typeId = rule.typeId,
                                    operation = rule.operation,
                                    value = rule.value
                                )
                            },
                            benefits = tier.benefits.map { benefit ->
                                OfferingDetail.Tier.Benefit(
                                    typeId = benefit.typeId,
                                    value = benefit.value
                                )
                            }
                        )
                    }
                ),
                products = it.bmsmListProduct.map { product ->
                    Product(
                        parentId = product.parentId.toLongOrZero(),
                        productId = product.id.toLongOrZero(),
                        warehouseId = product.warehouseId.toLongOrZero(),
                        productUrl = product.url,
                        imageUrl = product.imageUrl,
                        name = product.name,
                        price = product.displayedPrice,
                        rating = product.rating,
                        soldCount = product.countSold,
                        stock = product.stock,
                        isVbs = product.isVbs,
                        minOrder = product.minimumOrder,
                        discountedPrice = product.discountedPrice,
                        discountedPercentage = product.discountPercentage,
                        labelGroups = product.labelGroups.map { label ->
                            Product.LabelGroup(
                                position = label.position,
                                title = label.title,
                                type = label.type,
                                url = label.url
                            )
                        }
                    )
                },
                totalProduct = it.bmsmTotalProduct
            )
        }
    }

    fun ProductItemType.toProductCardModel(
        isOverrideTheme: Boolean,
        patternColorType: String,
        backgroundColor: String,
        isFestivity: Boolean
    ): ProductCardModel {
        return ProductCardModel(
            productImageUrl = imageUrl,
            productName = name,
            discountPercentage = slashedPricePercent.toString(),
            slashedPrice = slashedPrice,
            formattedPrice = price,
            labelGroupList = labelGroups.map { labelGroup ->
                ProductCardModel.LabelGroup(
                    position = labelGroup.position,
                    title = labelGroup.title,
                    imageUrl = labelGroup.url,
                    styleList = labelGroup.styles.map { style ->
                        ProductCardModel.LabelGroup.Style(key = style.key, value = style.value)
                    },
                    type = labelGroup.type
                )
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                isActive = freeOngkir.isActive,
                imageUrl = freeOngkir.imgUrl
            ),
            hasThreeDots = false,
            hasAddToCartButton = false,
            isWishlisted = false,
            forceLightModeColor = false,
            shopBadgeList = badges.map { badge ->
                ProductCardModel.ShopBadge(
                    imageUrl = badge.imageUrl,
                    title = badge.title
                )
            },
            countSoldRating = rating,
            colorMode = productCardColorHelper.determineProductCardColorMode(
                isFestivity = isFestivity,
                shouldOverrideTheme = isOverrideTheme,
                patternColorType = patternColorType,
                backgroundColor = backgroundColor,
                makeProductCardTransparent = true
            )
        )
    }
}
