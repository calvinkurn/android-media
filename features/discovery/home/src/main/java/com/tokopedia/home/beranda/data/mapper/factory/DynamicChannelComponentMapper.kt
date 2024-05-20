package com.tokopedia.home.beranda.data.mapper.factory

import com.google.gson.Gson
import com.tokopedia.home.beranda.domain.model.DynamicChannelTracker
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.model.*
import com.tokopedia.home_component.util.ChannelStyleUtil.parseBorderStyle
import com.tokopedia.home_component.util.ChannelStyleUtil.parseDividerSize
import com.tokopedia.home_component.util.ChannelStyleUtil.parseImageStyle
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.home_component_header.model.ChannelHeader as HomeComponentHeader

object DynamicChannelComponentMapper {
    const val LABEL_FULFILLMENT = "fulfillment"
    fun mapHomeChannelToComponent(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int,
        mapGrids: Boolean = true
    ): ChannelModel {
        return ChannelModel(
            id = channel.id,
            name = channel.name,
            groupId = channel.groupId,
            type = channel.type,
            layout = channel.layout,
            verticalPosition = verticalPosition,
            contextualInfo = channel.contextualInfo,
            widgetParam = channel.widgetParam,
            pageName = channel.pageName,
            channelViewAllCard = channel.viewAllCard.mapToViewAllCard(),
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
                channel.header.textColor,
                headerType = ChannelHeader.HeaderType.CHEVRON
            ),
            channelBanner = channel.banner.mapToChannelBanner(),
            channelConfig = channel.mapToChannelConfig(),
            trackingAttributionModel = channel.mapToTrackingAttributionModel(verticalPosition),
            channelGrids = channel.grids.takeIf { mapGrids }?.mapToChannelGrids().orEmpty(),
        )
    }

    fun mapHomeChannelTrackerToModel(
        channel: DynamicHomeChannel.Channels?,
        grid: DynamicHomeChannel.Grid
    ): ChannelTracker {
        val json = Gson().fromJson(grid.trackerJson, DynamicChannelTracker::class.java) ?: return ChannelTracker()

        return ChannelTracker(
            entranceForm = json.entranceForm,
            sourceModuleType = json.sourceModuleType,
            recomPageName = json.recomPageName,
            layoutTrackerType = json.layoutTrackerType,
            productId = json.productId,
            isTopAds = json.isTopAds.toBoolean(),
            trackId = json.trackId,
            recSessionId = json.recSessionId,
            recParams = json.recParams,
            requestId = json.requestId,
            shopId = json.shopId,
            itemOrder = json.itemOrder,
            layout = json.layout,
            cardName = json.cardName,
            campaignCode = json.campaignCode,
            creativeName = json.creativeName,
            creativeSlot = json.creativeSlot,
            isCarousel = json.isCarousel.toBoolean(),
            categoryId = json.categoryId,
            productName = json.productName,
            recommendationType = json.recommendationType,
            buType = json.buType,
            channelId = channel?.id.orEmpty(),
            channelName = channel?.name.orEmpty(),
            gridId = grid.id,
            headerName = channel?.header?.name.orEmpty(),
            bannerId = channel?.brandId.orEmpty(),
            attribution = channel?.homeAttribution.orEmpty(),
            persoType = channel?.persoType.orEmpty(),
        )
    }

    fun mapHomeChannelToComponentBannerHeader(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int
    ): ChannelModel {
        return ChannelModel(
            id = channel.id,
            groupId = channel.groupId,
            type = channel.type,
            layout = channel.layout,
            verticalPosition = verticalPosition,
            contextualInfo = channel.contextualInfo,
            widgetParam = channel.widgetParam,
            pageName = channel.pageName,
            channelViewAllCard = ChannelViewAllCard(
                id = channel.viewAllCard.id,
                contentType = channel.viewAllCard.contentType,
                description = channel.viewAllCard.description,
                title = channel.viewAllCard.title,
                imageUrl = channel.viewAllCard.imageUrl,
                gradientColor = channel.viewAllCard.gradientColor
            ),
            channelHeader = ChannelHeader(
                channel.header.id,
                channel.header.name.ifBlank { channel.banner.title },
                channel.header.subtitle.ifBlank { channel.banner.description },
                channel.header.expiredTime,
                channel.header.serverTimeUnix,
                channel.header.applink.ifBlank { channel.banner.applink },
                channel.header.url.ifBlank { channel.banner.url },
                channel.header.backColor,
                channel.header.backImage,
                channel.header.textColor,
                headerType = ChannelHeader.HeaderType.CHEVRON,
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
                layout = channel.layout,
                showPromoBadge = channel.showPromoBadge,
                hasCloseButton = channel.hasCloseButton,
                serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(
                    channel.header.serverTimeUnix
                ),
                createdTimeMillis = channel.timestamp,
                isAutoRefreshAfterExpired = channel.isAutoRefreshAfterExpired,
                dividerType = channel.dividerType,
                styleParam = channel.styleParam,
                dividerSize = channel.styleParam.parseDividerSize(),
                borderStyle = channel.styleParam.parseBorderStyle(),
                imageStyle = channel.styleParam.parseImageStyle()
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
                promoName = channel.promoName
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
                    shop = ChannelShop(
                        id = it.shop.shopId,
                        shopLocation = it.shop.city
                    ),
                    labelGroup = it.labelGroup.mapLabelGroup(),
                    hasBuyButton = it.hasBuyButton,
                    rating = it.rating,
                    ratingFloat = it.ratingFloat,
                    countReview = it.countReview,
                    backColor = it.backColor,
                    benefit = ChannelBenefit(
                        it.benefit.type,
                        it.benefit.value
                    ),
                    textColor = it.textColor,
                    recommendationType = it.recommendationType,
                    campaignCode = it.campaignCode,
                    shopId = it.shop.shopId,
                    badges = it.badges.map { badge ->
                        ChannelGridBadges(
                            title = badge.title,
                            imageUrl = badge.imageUrl
                        )
                    },
                    position = index
                )
            }
        )
    }

    private fun Array<com.tokopedia.home.beranda.domain.model.LabelGroup>.mapLabelGroup(): List<LabelGroup> {
        return map { label ->
            LabelGroup(
                title = label.title,
                position = label.position,
                type = label.type,
                url = label.imageUrl,
                styles = label.styles.map {
                    LabelGroup.Style(it.key, it.value)
                },
            )
        }
    }

    fun DynamicHomeChannel.Header.mapToHomeComponentHeader() = HomeComponentHeader(
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

    fun Array<DynamicHomeChannel.Grid>.mapToChannelGrids() = mapIndexed { index, grid ->
        grid.mapToChannelGrid(index)
    }

    fun DynamicHomeChannel.Grid.mapToChannelGrid(
        index: Int,
        useDtAsShopBadge: Boolean = false,
    ): ChannelGrid {
        val shopBadges = if(useDtAsShopBadge && badges.isEmpty()) {
            val label = labelGroup.getLabelGroupFulfillment()
            if(label != null) {
                listOf(
                    ChannelGridBadges(
                        title = label.title,
                        imageUrl = label.url
                    )
                )
            } else {
                emptyList()
            }
        } else {
            badges.map { badge ->
                ChannelGridBadges(
                    title = badge.title,
                    imageUrl = badge.imageUrl
                )
            }
        }

        return ChannelGrid(
            id = id,
            warehouseId = warehouseId,
            minOrder = minOrder,
            price = price,
            imageUrl = imageUrl,
            imageList = imageList.map {
                ChannelGridImage(
                    type = it.type,
                    imageUrl = it.imageUrl,
                    leftPadding = it.leftPadding,
                    rightPadding = it.rightPadding,
                )
            },
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
            labelGroup = labelGroup.mapLabelGroup(),
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
            badges = shopBadges,
            expiredTime = expiredTime,
            categoryBreadcrumbs = categoryBreadcrumbs,
            position = index
        )
    }

    fun DynamicHomeChannel.Channels.mapToChannelConfig() = ChannelConfig(
        layout = layout,
        showPromoBadge = showPromoBadge,
        hasCloseButton = hasCloseButton,
        serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(header.serverTimeUnix),
        createdTimeMillis = timestamp,
        isAutoRefreshAfterExpired = isAutoRefreshAfterExpired,
        dividerType = dividerType,
        styleParam = styleParam,
        dividerSize = styleParam.parseDividerSize(),
        borderStyle = styleParam.parseBorderStyle(),
        imageStyle = styleParam.parseImageStyle()
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

    fun DynamicHomeChannel.Banner.mapToChannelBanner() = ChannelBanner(
        id = id,
        title = title,
        description = description,
        backColor = backColor,
        url = url,
        applink = applink,
        textColor = textColor,
        imageUrl = imageUrl,
        attribution = attribution,
        cta = ChannelCtaData(
            cta.type,
            cta.mode,
            cta.text,
            cta.couponCode
        ),
        gradientColor = gradientColor
    )

    fun DynamicHomeChannel.ViewAllCard.mapToViewAllCard() = ChannelViewAllCard(
        id = id,
        contentType = contentType,
        description = description,
        title = title,
        imageUrl = imageUrl,
        gradientColor = gradientColor
    )

    fun List<RecommendationItem>.mapToChannelGrids() = mapIndexed { index, recommendationItem ->
        recommendationItem.mapToChannelGrid(index)
    }

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
                url = label.imageUrl,
                styles = label.styles.map {
                    LabelGroup.Style(it.key, it.value)
                }
            )
        },
        rating = rating,
        ratingFloat = ratingAverage,
        countReview = countReview,
        recommendationType = recommendationType,
        badges = badges.map { badge ->
            ChannelGridBadges(
                title = badge.title,
                imageUrl = badge.imageUrl,
            )
        },
        categoryBreadcrumbs = categoryBreadcrumbs,
        position = index
    )

    private fun Array<com.tokopedia.home.beranda.domain.model.LabelGroup>.getLabelGroupFulfillment(): LabelGroup? {
        val label = this.firstOrNull { it.position == LABEL_FULFILLMENT } ?: return null
        return LabelGroup(
            title = label.title,
            position = label.position,
            type = label.type,
            url = label.imageUrl
        )
    }
}
