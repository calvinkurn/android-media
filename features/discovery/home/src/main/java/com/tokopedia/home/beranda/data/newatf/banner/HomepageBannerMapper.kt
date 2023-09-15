package com.tokopedia.home.beranda.data.newatf.banner

import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.BannerRevampDataModel

object HomepageBannerMapper {
    private const val PROMO_NAME_BANNER_CAROUSEL = "/ - p%s - slider banner - banner - %s"
    private const val VALUE_BANNER_DEFAULT = ""

    fun com.tokopedia.home.beranda.domain.model.banner.BannerDataModel.asVisitable(
        index: Int,
        isCache: Boolean
    ): BannerDataModel {
        val channelModel = ChannelModel(
            channelGrids = slides?.map {
                ChannelGrid(
                    applink = it.applink,
                    campaignCode = it.campaignCode,
                    id = it.id.toString(),
                    imageUrl = it.imageUrl,
                    attribution = it.creativeName,
                    persona = it.persona,
                    categoryPersona = it.categoryPersona,
                    brandId = it.brandId,
                    categoryId = it.categoryId
                )
            } ?: listOf(),
            groupId = "",
            id = "",
            trackingAttributionModel = TrackingAttributionModel(
                promoName = String.format(
                    PROMO_NAME_BANNER_CAROUSEL,
                    (index + 1).toString(),
                    VALUE_BANNER_DEFAULT
                )
            )
        )
        return BannerDataModel(
            channelModel = channelModel,
            isCache = isCache,
            dimenMarginTop = home_componentR.dimen.home_banner_default_margin_vertical_design,
            dimenMarginBottom = home_componentR.dimen.home_banner_default_margin_vertical_design,
            cardInteraction = true,
            enableDotsAndInfiniteScroll = true,
            scrollTransitionDuration = BannerDataModel.NEW_IDLE_DURATION
        )
    }

    private fun mapToBannerRevampDataModel(
        bannerDataModel: com.tokopedia.home.beranda.domain.model.banner.BannerDataModel,
        index: Int,
        isCache: Boolean,
    ): BannerRevampDataModel {
        return bannerDataModel.let {
            val channelModel = ChannelModel(
                verticalPosition = index,
                channelGrids = mapIntoGrids(it, isCache),
                groupId = "",
                id = "",
                trackingAttributionModel = TrackingAttributionModel(
                    promoName = String.format(
                        PROMO_NAME_BANNER_CAROUSEL,
                        (index + 1).toString(),
                        VALUE_BANNER_DEFAULT
                    )
                )
            )
            BannerRevampDataModel(
                channelModel = channelModel,
                isCache = isCache
            )
        }
    }

    private fun mapIntoGrids(
        bannerDataModel: com.tokopedia.home.beranda.domain.model.banner.BannerDataModel,
        isCache: Boolean,
    ): List<ChannelGrid> {
        return bannerDataModel.slides.takeIf { !isCache }?.map {
            ChannelGrid(
                applink = it.applink,
                campaignCode = it.campaignCode,
                id = it.id.toString(),
                imageUrl = it.imageUrl,
                attribution = it.creativeName,
                persona = it.persona,
                categoryPersona = it.categoryPersona,
                brandId = it.brandId,
                categoryId = it.categoryId
            )
        }.orEmpty()
    }
}
