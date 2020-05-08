package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.analytics.v2.MixTopTracking
import com.tokopedia.home.analytics.v2.ProductHighlightTracking
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeoLocationPromptDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.util.ServerTimeOffsetUtil
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.domain.model.ProductImage
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

class HomeVisitableFactoryImpl(val userSessionInterface: UserSessionInterface) : HomeVisitableFactory {
    private var context: Context? = null
    private var trackingQueue: TrackingQueue? = null
    private var homeData: HomeData? = null
    private var isCache: Boolean = true
    private var visitableList: MutableList<Visitable<*>> = mutableListOf()

    val DEFAULT_BANNER_APPLINK_1 = "tokopedia://category-explore?type=1"
    val DEFAULT_BANNER_APPLINK_2 = ApplinkConst.OFFICIAL_STORE
    val DEFAULT_BANNER_APPLINK_3 = ApplinkConst.PROMO

    val DEFAULT_BANNER_IMAGE_URL_1 = "https://ecs7.tokopedia.net/defaultpage/banner/bannerbelanja500new.jpg"
    val DEFAULT_BANNER_IMAGE_URL_2 = "https://ecs7.tokopedia.net/defaultpage/banner/banneros500new.jpg"
    val DEFAULT_BANNER_IMAGE_URL_3 = "https://ecs7.tokopedia.net/defaultpage/banner/bannerpromo500new.jpg"

    override fun buildVisitableList(homeData: HomeData, isCache: Boolean, trackingQueue: TrackingQueue, context: Context): HomeVisitableFactory {
        this.homeData = homeData
        this.isCache = isCache
        this.visitableList = mutableListOf<Visitable<*>>()
        this.trackingQueue = trackingQueue
        this.context = context
        return this
    }

    override fun addBannerVisitable(): HomeVisitableFactory {
        val bannerViewModel = HomepageBannerDataModel()
        val bannerDataModel = homeData?.banner
        bannerViewModel.isCache = isCache

        if (bannerDataModel == null || bannerDataModel.slides == null || bannerDataModel.slides.isEmpty()) {
            val defaultSlides = mutableListOf<BannerSlidesModel>()
            val defaultBannerSlidesModel1 = BannerSlidesModel()
            defaultBannerSlidesModel1.applink = DEFAULT_BANNER_APPLINK_1
            defaultBannerSlidesModel1.type = BannerSlidesModel.TYPE_BANNER_DEFAULT
            defaultBannerSlidesModel1.imageUrl = DEFAULT_BANNER_IMAGE_URL_1

            val defaultBannerSlidesModel2 = BannerSlidesModel()
            defaultBannerSlidesModel2.applink = DEFAULT_BANNER_APPLINK_2
            defaultBannerSlidesModel2.type = BannerSlidesModel.TYPE_BANNER_DEFAULT
            defaultBannerSlidesModel2.imageUrl = DEFAULT_BANNER_IMAGE_URL_2

            val defaultBannerSlidesModel3 = BannerSlidesModel()
            defaultBannerSlidesModel3.applink = DEFAULT_BANNER_APPLINK_3
            defaultBannerSlidesModel3.type = BannerSlidesModel.TYPE_BANNER_DEFAULT
            defaultBannerSlidesModel3.imageUrl = DEFAULT_BANNER_IMAGE_URL_3

            defaultSlides.add(defaultBannerSlidesModel1)
            defaultSlides.add(defaultBannerSlidesModel2)
            defaultSlides.add(defaultBannerSlidesModel3)

            bannerViewModel.slides = defaultSlides
        } else {
            bannerDataModel.slides.forEachIndexed { index, bannerSlidesModel ->
                bannerSlidesModel.position = index+1
            }
            bannerViewModel.slides = bannerDataModel.slides
        }
        visitableList.add(bannerViewModel)
        return this
    }

    override fun addTickerVisitable(): HomeVisitableFactory {
        if (!isCache) {
            val tmpTickers = ArrayList<Ticker.Tickers>()
            val tickers = homeData?.ticker?.tickers
            if (!HomeFragment.HIDE_TICKER) {
                tickers?.let {
                    for (tmpTicker in tickers) {
                        if (tmpTicker.layout != StickyLoginConstant.LAYOUT_FLOATING) {
                            tmpTickers.add(tmpTicker)
                        }
                    }
                    if (tmpTickers.isNotEmpty()) {
                        val viewModel = TickerDataModel()
                        viewModel.tickers = tmpTickers

                        visitableList.add(viewModel)
                    }
                }
            }
        }
        return this
    }

    override fun addUserWalletVisitable(): HomeVisitableFactory {
        val needToShowUserWallet = homeData?.homeFlag?.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false
        if (needToShowUserWallet) {
            val headerViewModel = HeaderDataModel()
            headerViewModel.isPendingTokocashChecked = false
            headerViewModel.isUserLogin = userSessionInterface.isLoggedIn

            visitableList.add(headerViewModel)
        }
        return this
    }

    override fun addGeolocationVisitable(): HomeVisitableFactory {
        visitableList.add(GeoLocationPromptDataModel())
        return this
    }

    override fun addDynamicIconVisitable(): HomeVisitableFactory {
        val isDynamicIconWrapType = homeData?.homeFlag?.getFlag(HomeFlag.TYPE.DYNAMIC_ICON_WRAP)?: false
        val iconList = homeData?.dynamicHomeIcon?.dynamicIcon?: listOf()

        val viewModelDynamicIcon = DynamicIconSectionDataModel()
        viewModelDynamicIcon.dynamicIconWrap = isDynamicIconWrapType
        for (icon in iconList) {
            viewModelDynamicIcon.addItem(HomeIconItem(
                    icon.id,
                    icon.name,
                    icon.imageUrl,
                    icon.applinks,
                    icon.url,
                    icon.bu_identifier,
                    icon.galaxyAttribution,
                    icon.persona,
                    icon.brandId,
                    icon.categoryPersona
            ))
        }

        if (!isCache) {
            viewModelDynamicIcon.setTrackingData(
                    HomePageTracking.getEnhanceImpressionDynamicIconHomePage(viewModelDynamicIcon.itemList))
            viewModelDynamicIcon.isTrackingCombined = false
        }
        visitableList.add(viewModelDynamicIcon)
        return this
    }

    override fun addDynamicChannelVisitable(): HomeVisitableFactory {
        homeData?.dynamicHomeChannel?.channels?.forEachIndexed { index, channel ->
            val position = index+1
            setDynamicChannelPromoName(position, channel)
            when (channel.layout) {
                DynamicHomeChannel.Channels.LAYOUT_TOPADS -> createDynamicTopAds(channel)
                DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT -> {
                    homeData?.spotlight?.let { spotlight ->  createSpotlight(spotlight, isCache)} }
                DynamicHomeChannel.Channels.LAYOUT_HOME_WIDGET -> createBusinessUnitWidget(position)
                DynamicHomeChannel.Channels.LAYOUT_3_IMAGE, DynamicHomeChannel.Channels.LAYOUT_HERO ->
                    createDynamicChannel(
                            channel = channel,
                            trackingDataForCombination = channel.convertPromoEnhanceDynamicChannelDataLayerForCombination(),
                            isCombined = true)
                DynamicHomeChannel.Channels.LAYOUT_6_IMAGE, DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE, DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE -> {
                    createDynamicChannel(
                            channel = channel,
                            trackingDataForCombination = channel.convertPromoEnhanceLegoBannerDataLayerForCombination(),
                            isCombined = true)
                }
                DynamicHomeChannel.Channels.LAYOUT_SPRINT -> {
                    createDynamicChannel(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL -> {
                    createDynamicChannel(
                            channel = channel,
                            trackingDataForCombination = channel.convertProductEnhanceSprintSaleCarouselDataLayerForCombination(),
                            isCombined = true)
                }
                DynamicHomeChannel.Channels.LAYOUT_ORGANIC -> {
                    createDynamicChannel(
                            channel = channel,
                            trackingData = channel.enhanceImpressionDynamicSprintLegoHomePage
                    )
                }
                DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO -> {
                    createDynamicChannel(
                            channel = channel,
                            trackingData = HomePageTrackingV2.SprintSale.getSprintSaleImpression(channel)
                    )
                }
                DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC, DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL -> {
                    createDynamicChannel(
                            channel = channel,
                            trackingData = channel.enhanceImpressionProductChannelMix
                    )
                    if(!isCache) trackingQueue?.putEETracking(channel.enhanceImpressionBannerChannelMix)
                }
                DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF -> {
                    createDynamicChannel(
                            channel = channel,
                            trackingData = channel.enhanceImpressionProductChannelMix
                    )
                    if(!isCache) trackingQueue?.putEETracking(HomePageTracking.getEventEnhanceImpressionBannerGif(channel))
                }
                DynamicHomeChannel.Channels.LAYOUT_LIST_CAROUSEL -> {
                    createDynamicChannel(
                            channel = channel,
                            trackingData = HomePageTrackingV2.RecommendationList.getRecommendationListImpression(channel,  userId = userSessionInterface.userId ?: "")
                    )
                }
                DynamicHomeChannel.Channels.LAYOUT_MIX_LEFT -> {createDynamicChannel(
                        channel = channel,
                        trackingData = HomePageTrackingV2.MixLeft.getMixLeftProductView(channel)
                )}
                DynamicHomeChannel.Channels.LAYOUT_PRODUCT_HIGHLIGHT -> {
                    createDynamicChannel(
                            channel = channel,
                            trackingData = ProductHighlightTracking.getProductHighlightImpression(channel)) }
                DynamicHomeChannel.Channels.LAYOUT_POPULAR_KEYWORD -> {createPopularKeywordChannel(channel = channel)}
                DynamicHomeChannel.Channels.LAYOUT_DEFAULT_ERROR -> { createDynamicChannel(channel = channel) }
                DynamicHomeChannel.Channels.LAYOUT_REVIEW -> { createReviewWidget(channel = channel) }
                DynamicHomeChannel.Channels.LAYOUT_PLAY_BANNER -> { createPlayWidget(channel) }
                DynamicHomeChannel.Channels.LAYOUT_MIX_TOP -> { createDynamicChannel(
                        channel,
                        trackingData = MixTopTracking.getMixTopView(MixTopTracking.mapChannelToProductTracker(channel), headerName = channel.header.name, positionOnWidgetHome = position.toString()),
                        isCombined = false
                ) }
                DynamicHomeChannel.Channels.LAYOUT_RECHARGE_RECOMMENDATION -> { createRechargeRecommendationWidget() }
            }
        }

        return this
    }

    private fun createPlayWidget(channel: DynamicHomeChannel.Channels) {
        if (!isCache) {
            val playBanner = mappingPlayChannel(channel, HashMap(), isCache)
            if (!visitableList.contains(playBanner)) visitableList.add(playBanner)
        }
    }

    private fun createMixTopWidget(channel: DynamicHomeChannel.Channels) {
        if (!isCache) {
            val playBanner = mappingPlayChannel(channel, HashMap(), isCache)
            if (!visitableList.contains(playBanner)) visitableList.add(playBanner)
        }
    }

    private fun createDynamicChannel(channel: DynamicHomeChannel.Channels,
                                     trackingData: Map<String, Any>? = null,
                                     trackingDataForCombination: List<Any>? = null,
                                     isCombined: Boolean = false) {
        visitableList.add(mappingDynamicChannel(
                channel,
                trackingData,
                trackingDataForCombination,
                isCombined,
                isCache))
        context?.let { HomeTrackingUtils.homeDiscoveryWidgetImpression(it,
                visitableList.size, channel) }
    }

    private fun createBusinessUnitWidget(position: Int) {
        if (!isCache) {
            visitableList.add(NewBusinessUnitWidgetDataModel(
                    position = position,
                    isCache = false))
        }
    }

    private fun setDynamicChannelPromoName(position: Int, channel: DynamicHomeChannel.Channels) {
        val PROMO_NAME_LEGO_6_IMAGE = "/ - p%s - lego banner - %s"
        val PROMO_NAME_LEGO_3_IMAGE = "/ - p%s - lego banner 3 image - %s"
        val PROMO_NAME_LEGO_4_IMAGE = "/ - p%s - lego banner 4 image - %s"
        val PROMO_NAME_MIX_LEFT = "/ - p%s - mix left - %s"
        val PROMO_NAME_SPRINT = "/ - p%s - %s"
        val PROMO_NAME_SPOTLIGHT_BANNER = "/ - p%s - spotlight banner"
        val PROMO_NAME_GIF_BANNER = "/ - p%s - lego banner gif - %s"
        val PROMO_NAME_DC_MIX_BANNER = "/ - p%s - dynamic channel mix - banner - %s"
        val PROMO_NAME_UNKNOWN = "/ - p%s - %s - %s"

        val VALUE_BANNER_UNKNOWN = "banner unknown"
        val VALUE_BANNER_UNKNOWN_LAYOUT_TYPE = "lego banner unknown"

        if (!isCache) {
            if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT) {
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL) {
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_6_IMAGE) {
                channel.promoName = String.format(PROMO_NAME_LEGO_6_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE) {
                channel.promoName = String.format(PROMO_NAME_LEGO_3_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE) {
                channel.promoName = String.format(PROMO_NAME_LEGO_4_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO || channel.layout == DynamicHomeChannel.Channels.LAYOUT_ORGANIC) {
                channel.promoName = String.format(PROMO_NAME_SPRINT, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT) {
                homeData?.spotlight?.promoName = String.format(PROMO_NAME_SPOTLIGHT_BANNER, position.toString())
                homeData?.spotlight?.channelId = channel.id
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_HERO || channel.layout == DynamicHomeChannel.Channels.LAYOUT_TOPADS || channel.layout == DynamicHomeChannel.Channels.LAYOUT_3_IMAGE) {
                channel.promoName = String.format(PROMO_NAME_SPRINT, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC || channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL) {
                channel.promoName = String.format(PROMO_NAME_DC_MIX_BANNER, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_REVIEW) {
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF) {
                channel.promoName = String.format(PROMO_NAME_GIF_BANNER, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if(channel.layout == DynamicHomeChannel.Channels.LAYOUT_MIX_LEFT) {
                channel.promoName = String.format(PROMO_NAME_MIX_LEFT, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else {
                val headerName = if (channel.header.name.isEmpty()) VALUE_BANNER_UNKNOWN else channel.header.name
                val layoutType = if (channel.layout.isEmpty()) VALUE_BANNER_UNKNOWN_LAYOUT_TYPE else channel.layout
                channel.promoName = String.format(PROMO_NAME_UNKNOWN, position.toString(), layoutType, headerName)
            }
        }
    }

    private fun createReviewWidget(channel: DynamicHomeChannel.Channels) {
        if (!isCache) visitableList.add(ReviewDataModel(channel = channel))
    }

    private fun createDynamicTopAds(channel: DynamicHomeChannel.Channels) {
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
            visitable.setTrackingDataForCombination(channel.convertPromoEnhanceDynamicChannelDataLayerForCombination())
            visitable.isTrackingCombined = true
        }
        visitableList.add(visitable)
    }

    private fun mappingDynamicChannel(channel: DynamicHomeChannel.Channels,
                                      trackingData: Map<String, Any>?,
                                      trackingDataForCombination: List<Any>?,
                                      isCombined: Boolean,
                                      isCache: Boolean): Visitable<*> {
        val viewModel = DynamicChannelDataModel()
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

    private fun createSpotlight(spotlight: Spotlight, isCache: Boolean) {
        val spotlightItems: MutableList<SpotlightItemDataModel> = ArrayList()
        for (spotlightItem in spotlight.spotlights) {
            spotlightItems.add(SpotlightItemDataModel(
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
                    spotlight.channelId,
                    spotlightItem.galaxyAttribution,
                    spotlightItem.persona,
                    spotlightItem.brandId,
                    spotlightItem.categoryPersona
            ))
        }
        val viewModel = SpotlightDataModel(spotlightItems, spotlight.channelId)
        if (!isCache) {
            viewModel.setTrackingData(spotlight.enhanceImpressionSpotlightHomePage)
            viewModel.isTrackingCombined = false
        }
        visitableList.add(viewModel)
    }

    private fun mappingPlayChannel(channel: DynamicHomeChannel.Channels,
                                   trackingData: MutableMap<String, Any>,
                                   isCache: Boolean): Visitable<*> {
        val playCardViewModel = PlayCardDataModel(channel, null)
        if (!isCache) {
            playCardViewModel.setTrackingData(trackingData)
        }
        return playCardViewModel
    }

    private fun createPopularKeywordChannel(channel: DynamicHomeChannel.Channels) {
        visitableList.add(PopularKeywordListDataModel(popularKeywordList = mutableListOf(), channel = channel))
    }

    private fun createRechargeRecommendationWidget() {
        if (!isCache) visitableList.add(RechargeRecommendationViewModel())
    }

    override fun build(): List<Visitable<*>> = visitableList
}