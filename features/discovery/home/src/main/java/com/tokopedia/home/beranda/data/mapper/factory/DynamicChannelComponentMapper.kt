package com.tokopedia.home.beranda.data.mapper.factory

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.model.*

object DynamicChannelComponentMapper {
    fun mapHomeChannelToComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int): ChannelModel {
        return ChannelModel(
                id = channel.id,
                groupId = channel.groupId,
                type = channel.type,
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
                        channel.timestamp
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
                channelGrids = channel.grids.map {
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
                            labelGroup = it.labelGroup.map { label ->
                                LabelGroup(
                                        title = label.title,
                                        position = label.position,
                                        type = label.type
                                )
                            },
                            hasBuyButton = it.hasBuyButton,
                            rating = it.rating,
                            countReview = it.countReview
                    )
                }
        )
    }
}