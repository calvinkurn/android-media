package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelBenefit
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.LabelGroup
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse

object ChannelMapper {

    fun mapToChannelModel(response: HomeLayoutResponse): ChannelModel {
        return ChannelModel(
            id = response.id,
            groupId = response.groupId,
            type = response.type,
            layout = response.layout,
            channelHeader = ChannelHeader(
                response.header.id,
                response.header.name,
                response.header.subtitle,
                response.header.expiredTime,
                response.header.serverTimeUnix,
                response.header.applink,
                response.header.url,
                response.header.backColor,
                response.header.backImage,
                response.header.textColor
            ),
            channelBanner = ChannelBanner(
                id = response.banner.id,
                title = response.banner.title,
                description = response.banner.description,
                backColor = response.banner.backColor,
                url = response.banner.url,
                applink = response.banner.applink,
                textColor = response.banner.textColor,
                imageUrl = response.banner.imageUrl,
                attribution = response.banner.attribution,
                cta = ChannelCtaData(
                    response.banner.cta.type,
                    response.banner.cta.mode,
                    response.banner.cta.text,
                    response.banner.cta.couponCode
                ),
                gradientColor = response.banner.gradientColor
            ),
            channelConfig = ChannelConfig(
                response.layout,
                response.showPromoBadge,
                response.hasCloseButton,
                ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(response.header.serverTimeUnix),
                response.timestamp,
                response.isAutoRefreshAfterExpired
            ),
            trackingAttributionModel = TrackingAttributionModel(
                galaxyAttribution = response.galaxyAttribution,
                persona = response.persona,
                brandId = response.brandId,
                categoryPersona = response.categoryPersona,
                categoryId = response.categoryID,
                persoType = response.persoType,
                campaignCode = response.campaignCode,
                homeAttribution = response.homeAttribution
            ),
            channelGrids = response.grids.map {
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
                    textColor = it.textColor
                )
            }
        )
    }
}