package com.tokopedia.shop.home.util.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopPageHeaderDataUiModel
import com.tokopedia.shop.common.data.model.ShopPageHeaderUiModel
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.home.WidgetName.ADD_ONS
import com.tokopedia.shop.home.WidgetName.BANNER_PRODUCT_HOTSPOT
import com.tokopedia.shop.home.WidgetName.BANNER_TIMER
import com.tokopedia.shop.home.WidgetName.BIG_CAMPAIGN_THEMATIC
import com.tokopedia.shop.home.WidgetName.BUY_AGAIN
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.ETALASE_THEMATIC
import com.tokopedia.shop.home.WidgetName.FLASH_SALE_TOKO
import com.tokopedia.shop.home.WidgetName.IS_SHOW_ETALASE_NAME
import com.tokopedia.shop.home.WidgetName.NEW_PRODUCT_LAUNCH_CAMPAIGN
import com.tokopedia.shop.home.WidgetName.PERSO_PRODUCT_COMPARISON
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.RECENT_ACTIVITY
import com.tokopedia.shop.home.WidgetName.REMINDER
import com.tokopedia.shop.home.WidgetName.SHOWCASE_NAVIGATION_BANNER
import com.tokopedia.shop.home.WidgetName.SHOWCASE_SLIDER_TWO_ROWS
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.TRENDING
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
                it.averageRating = stats.averageRating
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
        isWideContent: Boolean,
        productRating: String
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
            countSoldRating = productRating,
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
        widgetLayout: ShopPageWidgetUiModel?,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): Visitable<*>? {
        if (widgetResponse.name == VOUCHER_STATIC) {
            return mapToVoucherUiModel(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
        }
        return when (widgetResponse.type.toLowerCase()) {
            DISPLAY.toLowerCase() -> {
                when (widgetResponse.name) {
                    DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN, SLIDER_BANNER, SLIDER_SQUARE_BANNER, VIDEO -> {
                        mapToDisplayImageWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
                    }
                    BANNER_TIMER -> {
                        ShopPageWidgetMapper.mapToBannerTimerWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
                    }
                    SHOWCASE_NAVIGATION_BANNER -> {
                        ShopPageWidgetMapper.mapToHomeShowcaseWidget(widgetResponse)
                    }

                    BANNER_PRODUCT_HOTSPOT -> {
                        ShopPageWidgetMapper.mapToBannerProductHotspotWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
                    }
                    else -> null
                }
            }
            // Includes V4 Widgets: Terlaris widget
            PRODUCT.toLowerCase() -> {
//                // =========== For testing Purpose =========== //
//                mapToProducTerlaristWidgetUiModel(
//                    widgetModel = widgetResponse,
//                    isMyOwnProduct = isMyOwnProduct,
//                    isEnableDirectPurchase = isEnableDirectPurchase,
//                    widgetLayout = widgetLayout
//                )
//                // =========== For testing Purpose =========== //
                // TODO: Enable codes below to dynamically map data from BE
                mapToProductWidgetUiModel(
                    widgetModel = widgetResponse,
                    isMyOwnProduct = isMyOwnProduct,
                    isEnableDirectPurchase = isEnableDirectPurchase,
                    widgetLayout = widgetLayout,
                    isOverrideTheme = isOverrideTheme,
                    colorSchema = colorSchema
                )
            }
            CAMPAIGN.toLowerCase() -> {
                if (isThematicWidgetShown) {
                    when (widgetResponse.name) {
                        ETALASE_THEMATIC -> mapToThematicWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
                        BIG_CAMPAIGN_THEMATIC -> mapToThematicWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
                        FLASH_SALE_TOKO -> mapToFlashSaleUiModel(widgetResponse, isEnableDirectPurchase, widgetLayout, isOverrideTheme, colorSchema)
                        NEW_PRODUCT_LAUNCH_CAMPAIGN -> mapToNewProductLaunchCampaignUiModel(
                            widgetResponse,
                            isLoggedIn,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema
                        )
                        else -> null
                    }
                } else {
                    when (widgetResponse.name) {
                        FLASH_SALE_TOKO -> mapToFlashSaleUiModel(
                            widgetResponse,
                            isEnableDirectPurchase,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema
                        )
                        NEW_PRODUCT_LAUNCH_CAMPAIGN -> mapToNewProductLaunchCampaignUiModel(
                            widgetResponse,
                            isLoggedIn,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema
                        )
                        else -> null
                    }
                }
            }
            DYNAMIC.toLowerCase(Locale.getDefault()) -> mapCarouselPlayWidget(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
            PERSONALIZATION.toLowerCase(Locale.getDefault()) -> {
                when (widgetResponse.name) {
                    BUY_AGAIN, RECENT_ACTIVITY, REMINDER, ADD_ONS, TRENDING -> {
                        mapToProductPersonalizationUiModel(
                            widgetResponse,
                            isMyOwnProduct,
                            isEnableDirectPurchase,
                            widgetLayout,
                            isOverrideTheme,
                            colorSchema
                        )
                    }
                    PERSO_PRODUCT_COMPARISON -> {
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
            SHOWCASE.toLowerCase(Locale.getDefault()) -> mapToShowcaseListUiModel(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
            CARD.lowercase() -> mapToCardDonationUiModel(widgetResponse, widgetLayout, isOverrideTheme, colorSchema)
            BUNDLE.toLowerCase(Locale.getDefault()) -> mapToProductBundleListUiModel(widgetResponse, shopId, widgetLayout, isOverrideTheme, colorSchema)
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
        colorSchema: ShopPageColorSchema
    ) = ShopHomeCarousellProductUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = mapToHeaderModel(widgetResponse.header, widgetLayout, isOverrideTheme, colorSchema),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        productList = mapToWidgetProductListPersonalization(widgetResponse.data, isMyProduct, isEnableDirectPurchase)
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
            isFestivity = widgetLayout?.isFestivity.orFalse(),
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
            header = DynamicHeaderUiModel(
                title = widgetResponse.data.firstOrNull()?.name.orEmpty(),
                subTitle = widgetResponse.data.firstOrNull()?.timeDescription.orEmpty(),
                ctaText = widgetResponse.header.ctaText,
                ctaTextLink = widgetResponse.header.ctaLink,
                statusCampaign = widgetResponse.data.firstOrNull()?.statusCampaign.orEmpty().lowercase(Locale.getDefault()),
                endDate = widgetResponse.data.firstOrNull()?.endDate.orEmpty(),
                timerCounter = widgetResponse.data.firstOrNull()?.timeCounter.orEmpty(),
                isOverrideTheme = isOverrideTheme,
                colorSchema = colorSchema
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

    fun mapToProducTerlaristWidgetUiModel(
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
            name = "terlaris",
            type = widgetModel.type,
            header = mapToHeaderModel(widgetModel.header, widgetLayout, isOverrideTheme, colorSchema),
            isFestivity = widgetLayout?.isFestivity.orFalse(),
            productList = getDummyData()
        )
    }

    private fun getDummyData(): List<ShopHomeProductUiModel> {
        val _data = listOf(
            ShopHomeProductUiModel().apply {
                id = "11014116628"
                name = "TRESemme Shampoo 170ml + Conditioner 170ml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 29.000"
                originalPrice = "Rp 50.000"
                discountPercentage = "5%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/8/4/72dc5b20-0d22-46c0-81bb-de65f689e2b2.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "11416628"
                name = "TRESemme Shampoo 170ml + Conditio70ml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 129.000"
                originalPrice = "Rp 760.000"
                discountPercentage = "15%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2023/7/25/4bd26fb3-4828-4e7f-8b87-e3af30a77f8c.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "110628"
                name = "TRESemme Shampoo 170mlml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 19.000"
                originalPrice = "Rp 20.000"
                discountPercentage = "5%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/7/31/a9928989-80ba-4ad6-87ed-5c9ea5f9ac26.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "110141628"
                name = "TRESemme Shampoo 170ml +ml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 39.000"
                originalPrice = "Rp 40.000"
                discountPercentage = "2%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/7/31/553dcf16-dfe8-4a0f-abc3-e29fc319f779.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "11416628"
                name = "TRESemme Shampoo 170ml + Conditio70ml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 129.000"
                originalPrice = "Rp 760.000"
                discountPercentage = "15%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2023/7/25/4bd26fb3-4828-4e7f-8b87-e3af30a77f8c.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "110628"
                name = "TRESemme Shampoo 170mlml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 19.000"
                originalPrice = "Rp 20.000"
                discountPercentage = "5%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/7/31/a9928989-80ba-4ad6-87ed-5c9ea5f9ac26.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "110141628"
                name = "TRESemme Shampoo 170ml +ml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 39.000"
                originalPrice = "Rp 40.000"
                discountPercentage = "2%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/7/31/553dcf16-dfe8-4a0f-abc3-e29fc319f779.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "11416628"
                name = "TRESemme Shampoo 170ml + Conditio70ml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 129.000"
                originalPrice = "Rp 760.000"
                discountPercentage = "15%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/hDjmkQ/2023/7/25/4bd26fb3-4828-4e7f-8b87-e3af30a77f8c.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "110628"
                name = "TRESemme Shampoo 170mlml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 19.000"
                originalPrice = "Rp 20.000"
                discountPercentage = "5%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/7/31/a9928989-80ba-4ad6-87ed-5c9ea5f9ac26.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            },
            ShopHomeProductUiModel().apply {
                id = "110141628"
                name = "TRESemme Shampoo 170ml +ml + Spray 236ml FREE Serum"
                displayedPrice = "Rp 39.000"
                originalPrice = "Rp 40.000"
                discountPercentage = "2%"
                imageUrl = "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/7/31/553dcf16-dfe8-4a0f-abc3-e29fc319f779.jpg"
                productUrl = "https://www.tokopedia.com/unilever/tresemme-shampoo-170ml-conditioner-170ml-spray-236ml-free-serum?extParam=whid%3D13056835"
                this.isEnableDirectPurchase = false
            }
        )
        return _data
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
        isThematicWidgetShown: Boolean,
        isEnableDirectPurchase: Boolean,
        shopId: String,
        listWidgetLayout: List<ShopPageWidgetUiModel>,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            responseWidgetData.filter { it.data.isNotEmpty() || it.type.equals(DYNAMIC, ignoreCase = true) || it.name == VOUCHER_STATIC || it.type.equals(CARD, ignoreCase = true) }.onEach {
                when (val widgetUiModel = mapToWidgetUiModel(it, myShop, isLoggedIn, isThematicWidgetShown, isEnableDirectPurchase, shopId, listWidgetLayout.firstOrNull { widgetLayout -> it.widgetID == widgetLayout.widgetId }, isOverrideTheme, colorSchema)) {
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

    fun mapToShopHomeWidgetLayoutData(response: HomeLayoutData): ShopPageLayoutUiModel {
        return ShopPageLayoutUiModel(
            layoutId = response.layoutId,
            masterLayoutId = response.masterLayoutId.toIntOrZero().toString(),
            listWidgetLayout = response.widgetIdList.map {
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
                        }
                    )
                )
            }
        )
    }

    fun mapShopHomeWidgetLayoutToListShopHomeWidget(
        listWidgetLayout: List<ShopPageWidgetUiModel>,
        myShop: Boolean,
        isLoggedIn: Boolean,
        isThematicWidgetShown: Boolean,
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
                    it,
                    isOverrideTheme,
                    colorSchema
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
