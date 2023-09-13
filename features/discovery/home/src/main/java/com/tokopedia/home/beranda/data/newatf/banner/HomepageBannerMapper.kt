package com.tokopedia.home.beranda.data.newatf.banner

import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.BannerRevampDataModel

object HomepageBannerMapper {
    private fun addHomePageBannerData(bannerDataModel: com.tokopedia.home.beranda.domain.model.banner.BannerDataModel?, index: Int) {
        if (!isCache) {
            bannerDataModel?.let {
                val channelModel = ChannelModel(
                    channelGrids = it.slides?.map {
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
                            HomeVisitableFactoryImpl.PROMO_NAME_BANNER_CAROUSEL,
                            (index + 1).toString(),
                            HomeVisitableFactoryImpl.VALUE_BANNER_DEFAULT
                        )
                    )
                )
                visitableList.add(
                    BannerDataModel(
                        channelModel = channelModel,
                        isCache = isCache,
                        dimenMarginTop = home_componentR.dimen.home_banner_default_margin_vertical_design,
                        dimenMarginBottom = home_componentR.dimen.home_banner_default_margin_vertical_design,
                        cardInteraction = true,
                        enableDotsAndInfiniteScroll = true,
                        scrollTransitionDuration = BannerDataModel.NEW_IDLE_DURATION
                    )
                )
            }
        }
    }

    private fun addHomePageBannerAtf2Data(bannerDataModel: com.tokopedia.home.beranda.domain.model.banner.BannerDataModel?, index: Int) {
        bannerDataModel?.let {
            val channelModel = ChannelModel(
                verticalPosition = index,
                channelGrids = mapIntoGrids(it),
                groupId = "",
                id = "",
                trackingAttributionModel = TrackingAttributionModel(
                    promoName = String.format(
                        HomeVisitableFactoryImpl.PROMO_NAME_BANNER_CAROUSEL,
                        (index + 1).toString(),
                        HomeVisitableFactoryImpl.VALUE_BANNER_DEFAULT
                    )
                )
            )
            visitableList.add(
                BannerRevampDataModel(
                    channelModel = channelModel,
                    isCache = isCache
                )
            )
        }
    }
}
