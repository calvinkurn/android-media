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
                verticalPosition = verticalPosition,
                channelHeader = ChannelHeader(
                        channel.header?.id.toString(),
                        channel.header?.name?:"",
                        "",
                        channel.header?.expiredTime?:"",
                        channel.header?.serverTime?:0,
                        channel.header?.applink?:"",
                        channel.header?.url?:"",
                        channel.header?.backColor?:"",
                        channel.header?.backImage?:"",
                        ""
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
                            productImageUrl = it.productImageUrl
                    )
                }
        )
    }
}