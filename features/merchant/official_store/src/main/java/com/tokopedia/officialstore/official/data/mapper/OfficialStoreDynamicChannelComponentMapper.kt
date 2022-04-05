package com.tokopedia.officialstore.official.data.mapper

import com.tokopedia.home_component.model.*
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel

object OfficialStoreDynamicChannelComponentMapper {
    fun mapChannelToComponent(channel: Channel, verticalPosition: Int): ChannelModel {
        return ChannelModel(
                id = channel.id,
                groupId = "",
                type = "",
                style = ChannelStyle.ChannelOS,
                name = channel.name,
                layout = channel.layout,
                widgetParam = channel.widgetParam,
                contextualInfo = channel.contextualInfo,
                verticalPosition = verticalPosition,
                channelViewAllCard = ChannelViewAllCard(
                        id = channel.viewAllCard.id,
                        contentType = channel.viewAllCard.contentType,
                        description = channel.viewAllCard.description,
                        title = channel.viewAllCard.title,
                        imageUrl = channel.viewAllCard.imageUrl,
                        gradientColor = channel.viewAllCard.gradientColor
                ),
                channelHeader = ChannelHeader(
                        channel.header?.id.toString(),
                        channel.header?.name?:"",
                        channel.header?.subtitle?:"",
                        channel.header?.expiredTime?:"",
                        channel.header?.serverTime?:0,
                        channel.header?.applink?:"",
                        channel.header?.url?:"",
                        channel.header?.backColor?:"",
                        channel.header?.backImage?:"",
                        channel.header?.textColor?:""
                ),
                channelBanner = ChannelBanner(
                        id = channel.banner?.id.toString(),
                        title = channel.banner?.title?:"",
                        description = channel.banner?.description?:"",
                        backColor = channel.banner?.backColor?:"",
                        url = channel.banner?.url?:"",
                        applink = channel.banner?.applink?:"",
                        textColor = channel.banner?.textColor?:"",
                        imageUrl = channel.banner?.imageUrl?:"",
                        attribution = channel.banner?.attribution?:"",
                        cta = ChannelCtaData(
                                channel.banner?.cta?.type?:"",
                                channel.banner?.cta?.mode?:"",
                                channel.banner?.cta?.text?:"",
                                channel.banner?.cta?.couponCode?:""
                        ),
                        gradientColor = channel.banner?.gradientColor?: arrayListOf()
                ),
                channelConfig = ChannelConfig(
                        channel.layout,
                        showPromoBadge = false,
                        hasCloseButton = false,
                        serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(channel.header?.serverTime?:0)
                ),
                trackingAttributionModel = TrackingAttributionModel(
                        galaxyAttribution = channel.galaxyAttribution,
                        persona = channel.persona,
                        brandId = channel.brandId,
                        categoryPersona = channel.categoryPersona,
                        campaignId = channel.campaignID.toString(),
                        campaignType = channel.campaignType
                ),
                channelGrids = channel.grids.map {
                    ChannelGrid(
                            id = it.id.toString(),
                            price = it.price,
                            imageUrl = it.imageUrl,
                            name = it.name,
                            applink = it.applink,
                            discount = it.discount,
                            slashedPrice = it.slashedPrice,
                            label = it.label,
                            soldPercentage = it.soldPercentage.toInt(),
                            attribution = it.attribution,
                            impression = it.impression,
                            cashback = it.cashback,
                            rating = it.rating,
                            countReview = it.countReview,
                            ratingFloat = it.ratingAverage?:"",
                            productClickUrl = it.productClickUrl,
                            isFreeOngkirActive = it.freeOngkir?.isActive?:false,
                            freeOngkirImageUrl = it.freeOngkir?.imageUrl?:"",
                            labelGroup = it.labelGroup.map { label ->
                                LabelGroup(
                                        title = label.title,
                                        position = label.position,
                                        type = label.type,
                                        url = label.imageUrl
                                )
                            },
                            shop =  ChannelShop(
                                    id = it.shop?.shopId?: "",
                                    shopName = it.shop?.name?: "",
                                    shopApplink = it.shop?.applink?: ""
                            ),
                            badges = it.badges?.map { badge ->
                                ChannelGridBadges(
                                        imageUrl = badge.imageUrl
                                )
                            } ?: listOf(),
                            backColor = it.backColor,
                            productImageUrl = it.productImageUrl,
                            benefit = ChannelBenefit(it.benefit.type, it.benefit.value)
                    )
                }
        )
    }

        fun mapChannelToComponentBannerToHeader(channel: Channel, verticalPosition: Int): ChannelModel {
                return ChannelModel(
                        id = channel.id,
                        groupId = "",
                        type = "",
                        style = ChannelStyle.ChannelOS,
                        name = channel.name,
                        layout = channel.layout,
                        widgetParam = channel.widgetParam,
                        contextualInfo = channel.contextualInfo,
                        verticalPosition = verticalPosition,
                        channelHeader = ChannelHeader(
                                channel.header?.id.toString(),
                                (channel.header?.name?: "") .ifBlank { channel.banner?.title?: "" },
                                (channel.header?.subtitle?:"").ifBlank { channel.banner?.description?: "" },
                                channel.header?.expiredTime?:"",
                                channel.header?.serverTime?:0,
                                (channel.header?.applink?:"").ifBlank { channel.banner?.applink?: "" },
                                (channel.header?.url?:"").ifBlank { channel.banner?.url?: "" },
                                channel.header?.backColor?:"",
                                channel.header?.backImage?:"",
                                channel.header?.textColor?:""
                        ),
                        channelBanner = ChannelBanner(
                                id = channel.banner?.id.toString(),
                                title = channel.banner?.title?:"",
                                description = channel.banner?.description?:"",
                                backColor = channel.banner?.backColor?:"",
                                url = channel.banner?.url?:"",
                                applink = channel.banner?.applink?:"",
                                textColor = channel.banner?.textColor?:"",
                                imageUrl = channel.banner?.imageUrl?:"",
                                attribution = channel.banner?.attribution?:"",
                                cta = ChannelCtaData(
                                        channel.banner?.cta?.type?:"",
                                        channel.banner?.cta?.mode?:"",
                                        channel.banner?.cta?.text?:"",
                                        channel.banner?.cta?.couponCode?:""
                                ),
                                gradientColor = channel.banner?.gradientColor?: arrayListOf()
                        ),
                        channelConfig = ChannelConfig(
                                channel.layout,
                                showPromoBadge = false,
                                hasCloseButton = false,
                                serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(channel.header?.serverTime?:0)
                        ),
                        trackingAttributionModel = TrackingAttributionModel(
                                galaxyAttribution = channel.galaxyAttribution,
                                persona = channel.persona,
                                brandId = channel.brandId,
                                categoryPersona = channel.categoryPersona,
                                campaignId = channel.campaignID.toString()
                        ),
                        channelGrids = channel.grids.map {
                                ChannelGrid(
                                        id = it.id.toString(),
                                        price = it.price,
                                        imageUrl = it.imageUrl,
                                        name = it.name,
                                        applink = it.applink,
                                        discount = it.discount,
                                        slashedPrice = it.slashedPrice,
                                        label = it.label,
                                        soldPercentage = it.soldPercentage.toInt(),
                                        attribution = it.attribution,
                                        impression = it.impression,
                                        cashback = it.cashback,
                                        rating = it.rating,
                                        countReview = it.countReview,
                                        ratingFloat = it.ratingAverage?:"",
                                        productClickUrl = it.productClickUrl,
                                        isFreeOngkirActive = it.freeOngkir?.isActive?:false,
                                        freeOngkirImageUrl = it.freeOngkir?.imageUrl?:"",
                                        labelGroup = it.labelGroup.map { label ->
                                                LabelGroup(
                                                        title = label.title,
                                                        position = label.position,
                                                        type = label.type,
                                                        url = label.imageUrl
                                                )
                                        },
                                        backColor = it.backColor,
                                        productImageUrl = it.productImageUrl,
                                        benefit = ChannelBenefit(it.benefit.type, it.benefit.value)
                                )
                        }
                )
        }
}