package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.data.DynamicHomeChannelCommon
import com.tokopedia.home_component.mapper.DynamicChannelComponentMapper
import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelBenefit
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelGridBadges
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelShop
import com.tokopedia.home_component.model.ChannelViewAllCard
import com.tokopedia.home_component.model.LabelGroup
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleTimerDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleErrorDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleProductGridShimmerDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.domain.model.DynamicHomeChannel
import com.tokopedia.unifycomponents.CardUnify2

object ShopFlashSaleMapper {

    private const val VIEW_ALL_CARD_TITLE_BELOW_THRESHOLD = "Ada produk menarik lain di toko ini!"
    private const val VIEW_ALL_CARD_TITLE_ABOVE_THRESHOLD = "Masih ada promo lainnya di toko ini!"

    /**
     *  Map Dynamic Channel data to Shop Flash Sale model
     */
    fun mapShopFlashSaleWidgetDataModel(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int,
    ): ShopFlashSaleWidgetDataModel {
        return ShopFlashSaleWidgetDataModel(
            id = channel.id,
            channelHeader = channel.header.mapToHomeComponentHeader(),
            channelModel = mapChannelToComponent(channel, verticalPosition),
            tabList = channel.grids.mapIndexed { index, grid ->
                ShopFlashSaleTabDataModel(
                    grid.mapToChannelGrid(index),
                    channel.mapToTrackingAttributionModel(verticalPosition),
                    index == 0
                )
            },
            timer = ShopFlashSaleTimerDataModel(isLoading = true),
            itemList = ShopFlashSaleProductGridShimmerDataModel.getAsList(),
        )
    }

    /**
     * Inserting items list and timer into Shop Flash Sale model.
     */
    fun mapShopFlashSaleItemList(
        currentDataModel: ShopFlashSaleWidgetDataModel,
        recomData: List<RecommendationWidget>,
    ): ShopFlashSaleWidgetDataModel {
        return if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
            val recomWidget = recomData.first()
            val trackingModel = currentDataModel.channelModel.trackingAttributionModel
            val carouselList = mutableListOf<Visitable<CommonCarouselProductCardTypeFactory>>().apply {
                addAll(getProducts(recomWidget, trackingModel))
                add(getViewAllCard(trackingModel, recomWidget.seeMoreAppLink, currentDataModel))
            }
            currentDataModel.copy(
                itemList = carouselList,
                timer = ShopFlashSaleTimerDataModel(recomWidget.endDate, isLoading = false),
            )
        } else {
            getLoadingShopFlashSale(currentDataModel)
        }
    }

    fun getLoadingShopFlashSale(currentDataModel: ShopFlashSaleWidgetDataModel): ShopFlashSaleWidgetDataModel {
        return currentDataModel.copy(
            itemList = ShopFlashSaleProductGridShimmerDataModel.getAsList(),
            timer = ShopFlashSaleTimerDataModel(isLoading = true)
        )
    }

    fun getErrorShopFlashSale(currentDataModel: ShopFlashSaleWidgetDataModel): ShopFlashSaleWidgetDataModel {
        return currentDataModel.copy(
            itemList = ShopFlashSaleErrorDataModel.getAsList(),
            timer = null
        )
    }

    private fun getProducts(
        recomWidget: RecommendationWidget,
        trackingModel: TrackingAttributionModel,
    ): List<CarouselProductCardDataModel> {
        return recomWidget.recommendationItemList.mapIndexed { index, item ->
            CarouselProductCardDataModel(
                productModel = item.toProductCardModel(
                    cardType = CardUnify2.TYPE_BORDER,
                ),
                grid = item.mapToChannelGrid(index),
                trackingAttributionModel = trackingModel,
                applink = item.appUrl,
            )
        }
    }

    private fun getViewAllCard(
        trackingModel: TrackingAttributionModel,
        seeMoreAppLink: String,
        currentDataModel: ShopFlashSaleWidgetDataModel,
    ): CarouselViewAllCardDataModel {
        val viewAllCardTitle = if(seeMoreAppLink.isEmpty())
            VIEW_ALL_CARD_TITLE_BELOW_THRESHOLD
        else VIEW_ALL_CARD_TITLE_ABOVE_THRESHOLD
        return CarouselViewAllCardDataModel(
            trackingAttributionModel = trackingModel,
            applink = currentDataModel.tabList.firstOrNull { it.isActivated }?.channelGrid?.applink.orEmpty(),
            channelViewAllCard = ChannelViewAllCard(
                title = viewAllCardTitle,
            ),
            animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE,
        )
    }

    private fun mapChannelToComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int): ChannelModel {
        return ChannelModel(
            id = channel.id,
            groupId = channel.groupId,
            type = channel.type,
            layout = channel.layout,
            verticalPosition = verticalPosition,
            channelHeader = ChannelHeader(
                channel.header.id,
                channel.header.name,
                channel.header.subtitle,
                channel.header.expiredTime,
                channel.header.serverTimeUnix,
                channel.header.applink,
                channel.header.url,
                channel.header.backColor,
                channel.header.backImage,
                channel.header.textColor
            ),
            channelBanner = ChannelBanner(
                id = channel.banner.id,
                title = channel.banner.title,
                description = channel.banner.description,
                backColor = channel.banner.backColor,
                url = channel.banner.url,
                applink = channel.banner.applink,
                textColor = channel.banner.textColor,
                imageUrl = channel.banner.imageUrl,
                attribution = channel.banner.attribution,
                cta = ChannelCtaData(
                    channel.banner.cta.type,
                    channel.banner.cta.mode,
                    channel.banner.cta.text,
                    channel.banner.cta.couponCode
                ),
                gradientColor = channel.banner.gradientColor
            ),
            channelConfig = ChannelConfig(
                channel.layout,
                channel.showPromoBadge,
                channel.hasCloseButton,
                ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(channel.header.serverTimeUnix),
                channel.timestamp,
                channel.isAutoRefreshAfterExpired
            ),
            trackingAttributionModel = TrackingAttributionModel(
                galaxyAttribution = channel.galaxyAttribution,
                persona = channel.persona,
                brandId = channel.brandId,
                categoryPersona = channel.categoryPersona,
                categoryId = channel.categoryID,
                persoType = channel.persoType,
                campaignCode = channel.campaignCode,
                homeAttribution = channel.homeAttribution,
                bannerId = channel.banner.id,
                headerName = channel.header.name,
                channelId = channel.id,
                parentPosition = (verticalPosition + 1).toString(),
            ),
            channelGrids = channel.grids.mapIndexed { index, it ->
                ChannelGrid(
                    id = it.id,
                    warehouseId = it.warehouseId,
                    minOrder = it.minOrder,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    name = it.name,
                    applink = it.applink,
                    url = it.url,
                    discount = it.discount,
                    slashedPrice = it.slashedPrice,
                    label = it.label,
                    soldPercentage = it.soldPercentage,
                    attribution = it.attribution,
                    impression = it.impression,
                    cashback = it.cashback,
                    productClickUrl = it.productClickUrl,
                    isTopads = it.isTopads,
                    productViewCountFormatted = it.productViewCountFormatted,
                    isOutOfStock = it.isOutOfStock,
                    isFreeOngkirActive = it.freeOngkir.isActive,
                    freeOngkirImageUrl = it.freeOngkir.imageUrl,
                    shopId = it.shop.shopId,
                    hasBuyButton = it.hasBuyButton,
                    labelGroup = it.labelGroup.map { label ->
                        LabelGroup(
                            title = label.title,
                            position = label.position,
                            type = label.type
                        )
                    },
                    rating = it.rating,
                    ratingFloat = it.ratingFloat,
                    countReview = it.countReview,
                    backColor = it.backColor,
                    benefit = ChannelBenefit(
                        it.benefit.type,
                        it.benefit.value
                    ),
                    textColor = it.textColor,
                    position = index
                )
            }
        )
    }
}

fun DynamicHomeChannel.Channels.mapChannelToComponent(verticalPosition: Int): ChannelModel {
    return ChannelModel(
        id = this.id,
        groupId = this.groupId,
        type = this.type,
        layout = this.layout,
        verticalPosition = verticalPosition,
        channelHeader = ChannelHeader(
            this.header.id,
            this.header.name,
            this.header.subtitle,
            this.header.expiredTime,
            this.header.serverTimeUnix,
            this.header.applink,
            this.header.url,
            this.header.backColor,
            this.header.backImage,
            this.header.textColor
        ),
        channelBanner = ChannelBanner(
            id = this.banner.id,
            title = this.banner.title,
            description = this.banner.description,
            backColor = this.banner.backColor,
            url = this.banner.url,
            applink = this.banner.applink,
            textColor = this.banner.textColor,
            imageUrl = this.banner.imageUrl,
            attribution = this.banner.attribution,
            cta = ChannelCtaData(
                this.banner.cta.type,
                this.banner.cta.mode,
                this.banner.cta.text,
                this.banner.cta.couponCode
            ),
            gradientColor = this.banner.gradientColor
        ),
        channelConfig = ChannelConfig(
            this.layout,
            this.showPromoBadge,
            this.hasCloseButton,
            ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(this.header.serverTimeUnix),
            this.timestamp,
            this.isAutoRefreshAfterExpired
        ),
        trackingAttributionModel = TrackingAttributionModel(
            galaxyAttribution = this.galaxyAttribution,
            persona = this.persona,
            brandId = this.brandId,
            categoryPersona = this.categoryPersona,
            categoryId = this.categoryID,
            persoType = this.persoType,
            campaignCode = this.campaignCode,
            homeAttribution = this.homeAttribution,
            bannerId = this.banner.id,
            headerName = this.header.name,
            channelId = this.id,
            parentPosition = (verticalPosition + 1).toString(),
        ),
        channelGrids = this.grids.mapIndexed { index, it ->
            ChannelGrid(
                id = it.id,
                warehouseId = it.warehouseId,
                minOrder = it.minOrder,
                price = it.price,
                imageUrl = it.imageUrl,
                name = it.name,
                applink = it.applink,
                url = it.url,
                discount = it.discount,
                slashedPrice = it.slashedPrice,
                label = it.label,
                soldPercentage = it.soldPercentage,
                attribution = it.attribution,
                impression = it.impression,
                cashback = it.cashback,
                productClickUrl = it.productClickUrl,
                isTopads = it.isTopads,
                productViewCountFormatted = it.productViewCountFormatted,
                isOutOfStock = it.isOutOfStock,
                isFreeOngkirActive = it.freeOngkir.isActive,
                freeOngkirImageUrl = it.freeOngkir.imageUrl,
                shopId = it.shop.shopId,
                hasBuyButton = it.hasBuyButton,
                labelGroup = it.labelGroup.map { label ->
                    LabelGroup(
                        title = label.title,
                        position = label.position,
                        type = label.type
                    )
                },
                rating = it.rating,
                ratingFloat = it.ratingFloat,
                countReview = it.countReview,
                backColor = it.backColor,
                benefit = ChannelBenefit(
                    it.benefit.type,
                    it.benefit.value
                ),
                textColor = it.textColor,
                position = index
            )
        }
    )
}

fun DynamicHomeChannel.Header.mapToHomeComponentHeader() = com.tokopedia.home_component_header.model.ChannelHeader(
    id,
    name,
    subtitle,
    expiredTime,
    serverTimeUnix,
    applink,
    url,
    backColor,
    backImage,
    textColor,
)

fun DynamicHomeChannel.Grid.mapToChannelGrid(index: Int) = ChannelGrid(
    id = id,
    warehouseId = warehouseId,
    minOrder = minOrder,
    price = price,
    imageUrl = imageUrl,
    name = name,
    applink = applink,
    url = url,
    discount = discount,
    slashedPrice = slashedPrice,
    label = label,
    soldPercentage = soldPercentage,
    attribution = attribution,
    impression = impression,
    cashback = cashback,
    productClickUrl = productClickUrl,
    isTopads = isTopads,
    productViewCountFormatted = productViewCountFormatted,
    isOutOfStock = isOutOfStock,
    isFreeOngkirActive = freeOngkir.isActive,
    freeOngkirImageUrl = freeOngkir.imageUrl,
    shop = ChannelShop(
        id = shop.shopId,
        shopLocation = shop.city,
        shopName = shop.name,
        shopProfileUrl = shop.imageUrl,
        shopUrl = shop.url,
        shopApplink = shop.applink,
    ),
    labelGroup = labelGroup.map { label ->
        LabelGroup(
            title = label.title,
            position = label.position,
            type = label.type
        )
    },
    hasBuyButton = hasBuyButton,
    rating = rating,
    ratingFloat = ratingFloat,
    countReview = countReview,
    backColor = backColor,
    benefit = ChannelBenefit(
        benefit.type,
        benefit.value
    ),
    textColor = textColor,
    recommendationType = recommendationType,
    campaignCode = campaignCode,
    shopId = shop.shopId,
    badges = badges.map { badge ->
        ChannelGridBadges(
            title = badge.title,
            imageUrl = badge.imageUrl
        )
    },
    expiredTime = expiredTime,
    categoryBreadcrumbs = categoryBreadcrumbs,
    position = index
)

fun RecommendationItem.mapToChannelGrid(index: Int) = ChannelGrid(
    id = productId.toString(),
    warehouseId = warehouseId.toString(),
    minOrder = minOrder,
    price = price,
    imageUrl = imageUrl,
    name = name,
    applink = appUrl,
    url = url,
    discount = discountPercentage,
    slashedPrice = slashedPrice,
    impression = trackerImageUrl,
    productClickUrl = clickUrl,
    isTopads = isTopAds,
    isFreeOngkirActive = isFreeOngkirActive,
    freeOngkirImageUrl = freeOngkirImageUrl,
    shop = ChannelShop(
        id = shopId.toString(),
        shopLocation = location,
        shopName = shopName,
    ),
    labelGroup = labelGroupList.map { label ->
        LabelGroup(
            title = label.title,
            position = label.position,
            type = label.type,
            url = label.imageUrl
        )
    },
    rating = rating,
    ratingFloat = ratingAverage,
    countReview = countReview,
    recommendationType = recommendationType,
    badges = badges.map {
        ChannelGridBadges(
            title = it.title,
            imageUrl = it.imageUrl
        )
    },
    categoryBreadcrumbs = categoryBreadcrumbs,
    position = index
)

fun DynamicHomeChannel.Channels.mapToTrackingAttributionModel(verticalPosition: Int) = TrackingAttributionModel(
    galaxyAttribution = galaxyAttribution,
    persona = persona,
    brandId = brandId,
    categoryPersona = categoryPersona,
    categoryId = categoryID,
    persoType = persoType,
    campaignCode = campaignCode,
    homeAttribution = homeAttribution,
    promoName = promoName,
    campaignType = campaignType,
    bannerId = banner.id,
    headerName = header.name,
    channelId = id,
    parentPosition = (verticalPosition + 1).toString(),
    pageName = pageName,
)
