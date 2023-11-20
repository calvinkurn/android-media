package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse

object SliderBannerMapper {
    fun mapSliderBannerModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = mapChannelModel(response)
        val bannerDataModel = BannerDataModel(channelModel)
        return HomeLayoutItemUiModel(bannerDataModel, state)
    }

    private fun mapChannelModel(response: HomeLayoutResponse): ChannelModel {
        return ChannelModel(
            id = response.id,
            groupId = response.groupId,
            type = response.type,
            layout = response.layout,
            pageName = response.pageName,
            widgetParam = response.widgetParam,
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
                if (response.header.serverTimeUnix != 0L) ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(response.header.serverTimeUnix) else 0,
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
            )
        )
    }
}
