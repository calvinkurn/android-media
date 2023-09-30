package com.tokopedia.home.beranda.data.newatf.banner

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateChannelOneModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.BannerRevampDataModel
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel as BannerDomainDataModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class HomepageBannerMapper @Inject constructor() {
    companion object {
        private const val PROMO_NAME_BANNER_CAROUSEL = "/ - p%s - slider banner - banner - %s"
        private const val VALUE_BANNER_DEFAULT = ""
        private const val BANNER_EXPIRY_DAYS = 15L
        private val BANNER_EXPIRY_IN_MILLIS = TimeUnit.DAYS.toMillis(BANNER_EXPIRY_DAYS)
    }

    fun asVisitable(
        data: BannerDomainDataModel,
        index: Int,
        atfData: AtfData,
    ): Visitable<*> {
        val isExpired = System.currentTimeMillis() - atfData.lastUpdate > BANNER_EXPIRY_IN_MILLIS
        return if (atfData.atfStatus == AtfKey.STATUS_ERROR) {
            ErrorStateChannelOneModel()
        } else if (HomeRollenceController.isUsingAtf2Variant()) {
            mapToAtf2Banner(data, index, atfData.isCache, isExpired)
        } else {
            mapToOldBanner(data, index, atfData.isCache, isExpired)
        }
    }

    private fun mapToOldBanner(
        data: BannerDomainDataModel,
        index: Int,
        isCache: Boolean,
        isExpired: Boolean,
    ): BannerDataModel {
        val channelModel = ChannelModel(
            channelGrids = data.slides?.takeIf { !isExpired }?.map {
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

    private fun mapToAtf2Banner(
        data: BannerDomainDataModel,
        index: Int,
        isCache: Boolean,
        isExpired: Boolean,
    ): BannerRevampDataModel {
        val channelModel = ChannelModel(
            verticalPosition = index,
            channelGrids = data.slides?.takeIf { !isExpired }?.map {
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
            }.orEmpty(),
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
}
