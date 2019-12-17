package com.tokopedia.home.beranda.data.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon.DynamicIcon
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.util.ServerTimeOffsetUtil
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.domain.model.ProductImage
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel
import java.util.*

class HomeDataMapper(
        private val context: Context,
        private val homeVisitableFactory: HomeVisitableFactory
) {
    fun mapToHomeViewModel(homeData: HomeData?, isCache: Boolean): HomeViewModel?{
        if (homeData == null) return null
        val list: MutableList<Visitable<*>> = mutableListOf()
        list.add(mappingBanner(homeVisitableFactory, homeData.banner, isCache))
        if (homeData.ticker != null && homeData.ticker.tickers != null && homeData.ticker.tickers.isNotEmpty()
                && !HomeFragment.HIDE_TICKER) {
            val ticker = mappingTicker(homeData.ticker.tickers)
            if (ticker != null) {
                list.add(ticker)
            }
        }
        if (homeData.homeFlag != null) {
            val data = mappingOvoTokpoint(homeVisitableFactory, homeData.homeFlag.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS), isCache)
            if (data != null) {
                list.add(data)
            }
        }
        list.add(GeolocationPromptViewModel())
        if (homeData.dynamicHomeIcon != null && homeData.dynamicHomeIcon.dynamicIcon != null && !homeData.dynamicHomeIcon.dynamicIcon.isEmpty()) {
            list.add(mappingDynamicIcon(
                    homeData.dynamicHomeIcon.dynamicIcon,
                    isCache,
                    homeData.homeFlag.getFlag(HomeFlag.TYPE.DYNAMIC_ICON_WRAP)
            ))
        }

        if (homeData.dynamicHomeChannel != null && homeData.dynamicHomeChannel.channels != null && !homeData.dynamicHomeChannel.channels.isEmpty()) {
            var position = 1
            for (channel in homeData.dynamicHomeChannel.channels) {
                if (position == 2 && !isCache){
                    list.add(PlayCardViewModel().apply { url="https://www.vidio.com/videos/1559052/vjs_playlist.m3u8" ;thumbnailUrl = "https://cdn0-production-images-kly.akamaized.net/Y3MRGs3f3lKJj6OCxqfLK-jMfgU=/640x360/smart/filters:quality(75):strip_icc():format(jpeg)/kly-media-production/thumbnails/2644554/original/016821600_1546994118-nba-i-cuplikan-pertandingan-lakers-107-vs-mavericks-97-ca246c.jpg" })
                }
                if (channel.layout != null) {
                    if (!isCache) {
                        position++
                        if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT) {
                            channel.setPosition(position)
                        } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL) {
                        } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_6_IMAGE) {
                            channel.promoName = String.format("/ - p%s - lego banner - %s", position.toString(), channel.header.name)
                        } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE) {
                            channel.promoName = String.format("/ - p%s - lego banner 3 image - %s", position.toString(), channel.header.name)
                        } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO || channel.layout == DynamicHomeChannel.Channels.LAYOUT_ORGANIC) {
                            channel.promoName = String.format("/ - p%s - %s", position.toString(), channel.header.name)
                            channel.setPosition(position)
                        } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT) {
                            homeData.spotlight.promoName = String.format("/ - p%s - spotlight banner", position.toString())
                            homeData.spotlight.channelId = channel.id
                        } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_DIGITAL_WIDGET || channel.layout == DynamicHomeChannel.Channels.LAYOUT_HERO || channel.layout == DynamicHomeChannel.Channels.LAYOUT_TOPADS || channel.layout == DynamicHomeChannel.Channels.LAYOUT_3_IMAGE) {
                            channel.promoName = String.format("/ - p%s - %s", position.toString(), channel.header.name)
                        } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC || channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL) {
                            channel.setPosition(position)
                        } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_REVIEW) {
                            channel.setPosition(position)
                        }
                    }
                    when (channel.layout) {
                        DynamicHomeChannel.Channels.LAYOUT_DIGITAL_WIDGET -> list.add(mappingDigitalWidget(
                                context,
                                channel.convertPromoEnhanceDynamicChannelDataLayerForCombination(),
                                isCache
                        ))
                        DynamicHomeChannel.Channels.LAYOUT_TOPADS -> list.add(mappingDynamicTopAds(channel, isCache))
                        DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT -> list.add(mappingSpotlight(homeData.spotlight, isCache))
                        DynamicHomeChannel.Channels.LAYOUT_HOME_WIDGET -> if (!isCache) {
                            list.add(
                                    BusinessUnitViewModel(context.getString(R.string.digital_widget_title), position)
                            )
                        }
                        DynamicHomeChannel.Channels.LAYOUT_3_IMAGE, DynamicHomeChannel.Channels.LAYOUT_HERO -> {
                            list.add(mappingDynamicChannel(
                                    channel,
                                    null,
                                    channel.convertPromoEnhanceDynamicChannelDataLayerForCombination(),
                                    true,
                                    isCache))
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size, channel)
                        }
                        DynamicHomeChannel.Channels.LAYOUT_6_IMAGE, DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE -> {
                            list.add(mappingDynamicChannel(
                                    channel,
                                    null,
                                    channel.convertPromoEnhanceLegoBannerDataLayerForCombination(),
                                    true,
                                    isCache))
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size, channel)
                        }
                        DynamicHomeChannel.Channels.LAYOUT_SPRINT -> {
                            list.add(mappingDynamicChannel(
                                    channel,
                                    null,
                                    null,
                                    false,
                                    isCache))
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size, channel)
                        }
                        DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL -> {
                            list.add(mappingDynamicChannel(
                                    channel,
                                    null,
                                    channel.convertProductEnhanceSprintSaleCarouselDataLayerForCombination(),
                                    true,
                                    isCache))
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size, channel)
                        }
                        DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO, DynamicHomeChannel.Channels.LAYOUT_ORGANIC -> {
                            list.add(mappingDynamicChannel(
                                    channel,
                                    channel.enhanceImpressionDynamicSprintLegoHomePage,
                                    null,
                                    false,
                                    isCache))
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size, channel)
                        }
                        DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC, DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL -> {
                            list.add(mappingDynamicChannel(
                                    channel,
                                    channel.enhanceImpressionProductChannelMix,
                                    null,
                                    false,
                                    isCache))
                            HomeTrackingUtils.homeDiscoveryWidgetImpression(context,
                                    list.size, channel)
                            HomePageTracking.eventEnhanceImpressionBanner(context, channel)
                        }
                        DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF -> {
                            list.add(mappingDynamicChannel(
                                    channel,
                                    channel.enhanceImpressionProductChannelMix,
                                    null,
                                    false,
                                    isCache
                            ))
                            HomePageTracking.eventEnhanceImpressionBannerGif(context, channel)
                        }
                        DynamicHomeChannel.Channels.LAYOUT_REVIEW -> if (!isCache) {
                            list.add(mappingToReviewViewModel())
                        }
                        DynamicHomeChannel.Channels.LAYOUT_PLAY_BANNER -> if (!isCache) {
                            val playBanner = mappingPlayChannel(channel, HashMap(), isCache)
                            if (!list.contains(playBanner)) list.add(playBanner)
                        }
                    }
                }
            }
        }
        return HomeViewModel(homeData.homeFlag, list, isCache)
    }

    private fun mappingToReviewViewModel(): Visitable<*> {
        return ReviewViewModel()
    }

    private fun mappingDigitalWidget(context: Context, trackingDataForCombination: List<Any>, isCache: Boolean): Visitable<*> {
        val digitalsViewModel = DigitalsViewModel(context.getString(R.string.digital_widget_title), 0)
        if (!isCache) {
            digitalsViewModel.isTrackingCombined = true
            digitalsViewModel.trackingDataForCombination = trackingDataForCombination
        }
        return digitalsViewModel
    }

    private fun mappingDynamicTopAds(channel: DynamicHomeChannel.Channels, isCache: Boolean): Visitable<*> {
        val visitable = TopAdsDynamicChannelModel()
        val items: MutableList<Item<*>> = ArrayList()
        for (i in channel.grids.indices) {
            val grid = channel.grids[i]
            val model = ProductDynamicChannelViewModel()
            model.productId = grid.id
            model.productPrice = grid.price
            model.productName = grid.name
            model.productCashback = grid.cashback
            val productImage = ProductImage()
            productImage.m_url = grid.impression
            productImage.m_ecs = grid.imageUrl
            model.productImage = productImage
            model.applink = grid.applink
            model.productClickUrl = grid.productClickUrl
            items.add(model)
        }
        visitable.title = channel.header.name
        visitable.items = items
        if (!isCache) {
            visitable.trackingDataForCombination = channel.convertPromoEnhanceDynamicChannelDataLayerForCombination()
            visitable.isTrackingCombined = true
        }
        return visitable
    }

    private fun mappingTicker(tickers: ArrayList<Ticker.Tickers>): HomeVisitable<*>? {
        val tmpTickers = ArrayList<Ticker.Tickers>()
        for (tmpTicker in tickers) {
            if (tmpTicker.layout != StickyLoginConstant.LAYOUT_FLOATING) {
                tmpTickers.add(tmpTicker)
            }
        }
        if (tmpTickers.size <= 0) {
            return null
        }
        val viewModel = TickerViewModel()
        viewModel.tickers = tmpTickers
        return viewModel
    }

    private fun mappingBanner(homeVisitableFactory : HomeVisitableFactory, slides: BannerDataModel, isCache: Boolean): Visitable<*> {
        return homeVisitableFactory.createBannerVisitable(
                slides, isCache)
    }

    private fun mappingOvoTokpoint(homeVisitableFactory : HomeVisitableFactory, hasTokopoints: Boolean, isCache: Boolean): Visitable<*>? {
        return homeVisitableFactory.createOvoTokopointVisitable(
                hasTokopoints, isCache)
    }

    private fun mappingDynamicIcon(iconList: List<DynamicIcon>,
                                   isCache: Boolean,
                                   dynamicIconWrap: Boolean): Visitable<*> {
        val viewModelDynamicIcon = DynamicIconSectionViewModel()
        viewModelDynamicIcon.dynamicIconWrap = dynamicIconWrap
        for ((id, applinks, imageUrl, name, url, bu_identifier) in iconList) {
            viewModelDynamicIcon.addItem(HomeIconItem(id, name, imageUrl, applinks, url, bu_identifier))
        }
        if (!isCache) {
            viewModelDynamicIcon.setTrackingData(
                    HomePageTracking.getEnhanceImpressionDynamicIconHomePage(viewModelDynamicIcon.itemList)
            )
            viewModelDynamicIcon.isTrackingCombined = false
        }
        return viewModelDynamicIcon
    }

    private fun mappingDynamicChannel(channel: DynamicHomeChannel.Channels,
                                      trackingData: Map<String, Any>?,
                                      trackingDataForCombination: List<Any>?,
                                      isCombined: Boolean,
                                      isCache: Boolean): Visitable<*> {
        val viewModel = DynamicChannelViewModel()
        viewModel.channel = channel
        if (!isCache) {
            viewModel.trackingData = trackingData
            viewModel.trackingDataForCombination = trackingDataForCombination
            viewModel.isTrackingCombined = isCombined
        }
        viewModel.serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(
                channel.header.serverTimeUnix
        )
        return viewModel
    }

    private fun mappingSpotlight(spotlight: Spotlight, isCache: Boolean): Visitable<*> {
        val spotlightItems: MutableList<SpotlightItemViewModel> = ArrayList()
        for (spotlightItem in spotlight.spotlights) {
            spotlightItems.add(SpotlightItemViewModel(
                    spotlightItem.id,
                    spotlightItem.title,
                    spotlightItem.description,
                    spotlightItem.backgroundImageUrl,
                    spotlightItem.tagName,
                    spotlightItem.tagNameHexcolor,
                    spotlightItem.tagHexcolor,
                    spotlightItem.ctaText,
                    spotlightItem.ctaTextHexcolor,
                    spotlightItem.url,
                    spotlightItem.applink,
                    spotlight.promoName,
                    spotlight.channelId
            ))
        }
        val viewModel = SpotlightViewModel(spotlightItems, spotlight.channelId)
        if (!isCache) {
            viewModel.setTrackingData(spotlight.enhanceImpressionSpotlightHomePage)
            viewModel.isTrackingCombined = false
        }
        return viewModel
    }

    private fun mappingPlayChannel(channel: DynamicHomeChannel.Channels,
                                   trackingData: MutableMap<String, Any>,
                                   isCache: Boolean): Visitable<*> {
        val playCardViewModel = PlayCardViewModel()
        if (!isCache) {
            playCardViewModel.setChannel(channel)
            playCardViewModel.setTrackingData(trackingData)
        }
        return playCardViewModel
    }
}