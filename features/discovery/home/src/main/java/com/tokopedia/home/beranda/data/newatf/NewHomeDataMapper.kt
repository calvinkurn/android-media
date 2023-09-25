package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.BannerRevampDataModel

object NewHomeDataMapper {
    private const val PROMO_NAME_BANNER_CAROUSEL = "/ - p%s - slider banner - banner - %s"
    private const val VALUE_BANNER_DEFAULT = ""

    fun mapAtfToHomeDynamicChannel(
        atfList: AtfDataList?
    ): HomeDynamicChannelModel {
        val visitableList = mutableListOf<Visitable<*>>()
        atfList?.listAtfData?.forEachIndexed { idx, it ->
            when(it.atfContent) {
                is com.tokopedia.home.beranda.domain.model.banner.BannerDataModel ->
                    if (HomeRollenceController.isUsingAtf2Variant()) {
                        mapToHomepageBannerAtf2(it.atfContent, idx, it.isCache)
                    } else {
                        mapToHomepageBannerOld(it.atfContent, idx, it.isCache)
                    }
            }
        }
        return HomeDynamicChannelModel(
            list = visitableList
        )
    }

    private fun mapToHomepageBannerOld(
        bannerDataModel: com.tokopedia.home.beranda.domain.model.banner.BannerDataModel,
        index: Int,
        isCache: Boolean,
    ): BannerDataModel {
        val channelModel = ChannelModel(
            channelGrids = bannerDataModel.slides?.map {
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

    private fun mapToHomepageBannerAtf2(
        bannerDataModel: com.tokopedia.home.beranda.domain.model.banner.BannerDataModel,
        index: Int,
        isCache: Boolean,
    ): BannerRevampDataModel {
        val channelModel = ChannelModel(
            verticalPosition = index,
            channelGrids = mapBannerToGrids(bannerDataModel, isCache),
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
        return BannerRevampDataModel(
            channelModel = channelModel,
            isCache = isCache
        )
    }

    private fun mapBannerToGrids(
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
