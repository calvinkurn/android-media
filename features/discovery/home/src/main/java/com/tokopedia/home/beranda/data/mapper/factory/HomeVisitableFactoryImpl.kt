package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.analytics.v2.CategoryWidgetTracking
import com.tokopedia.home.analytics.v2.ProductHighlightTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.domain.model.Spotlight
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeoLocationPromptDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.visitable.*
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

class HomeVisitableFactoryImpl(
        val userSessionInterface: UserSessionInterface?,
        val remoteConfig: RemoteConfig) : HomeVisitableFactory {
    private var context: Context? = null
    private var trackingQueue: TrackingQueue? = null
    private var homeData: HomeData? = null
    private var isCache: Boolean = true
    private var visitableList: MutableList<Visitable<*>> = mutableListOf()

    companion object{
        private const val DEFAULT_BANNER_APPLINK_1 = "tokopedia://category-explore?type=1"
        private const val DEFAULT_BANNER_APPLINK_2 = ApplinkConst.OFFICIAL_STORE
        private const val DEFAULT_BANNER_APPLINK_3 = ApplinkConst.PROMO

        private const val DEFAULT_BANNER_IMAGE_URL_1 = "https://ecs7.tokopedia.net/defaultpage/banner/bannerbelanja500new.jpg"
        private const val DEFAULT_BANNER_IMAGE_URL_2 = "https://ecs7.tokopedia.net/defaultpage/banner/banneros500new.jpg"
        private const val DEFAULT_BANNER_IMAGE_URL_3 = "https://ecs7.tokopedia.net/defaultpage/banner/bannerpromo500new.jpg"
        private const val PROMO_NAME_LEGO_6_IMAGE = "/ - p%s - lego banner - %s"
        private const val PROMO_NAME_LEGO_3_IMAGE = "/ - p%s - lego banner 3 image - %s"
        private const val PROMO_NAME_LEGO_4_IMAGE = "/ - p%s - lego banner 4 image - %s"
        private const val PROMO_NAME_MIX_LEFT = "/ - p%s - mix left - %s"
        private const val PROMO_NAME_CATEGORY_WIDGET = "/ - p%s - category widget banner - %s"
        private const val PROMO_NAME_SPRINT = "/ - p%s - %s"
        private const val PROMO_NAME_TOPADS_BANNER = "/ - p%s - dynamic channel ads - %s"
        private const val PROMO_NAME_SPOTLIGHT_BANNER = "/ - p%s - spotlight banner"
        private const val PROMO_NAME_GIF_BANNER = "/ - p%s - lego banner gif - %s"
        private const val PROMO_NAME_DC_MIX_BANNER = "/ - p%s - dynamic channel mix - banner - %s"
        private const val PROMO_NAME_UNKNOWN = "/ - p%s - %s - %s"

        private const val VALUE_BANNER_UNKNOWN = "banner unknown"
        private const val VALUE_BANNER_UNKNOWN_LAYOUT_TYPE = "lego banner unknown"
    }

    override fun buildVisitableList(homeData: HomeData, isCache: Boolean, trackingQueue: TrackingQueue, context: Context): HomeVisitableFactory {
        this.homeData = homeData
        this.isCache = isCache
        this.visitableList = mutableListOf()
        this.trackingQueue = trackingQueue
        this.context = context
        return this
    }

    override fun addBannerVisitable(): HomeVisitableFactory {
        val bannerViewModel = HomepageBannerDataModel()
        val bannerDataModel = homeData?.banner
        bannerViewModel.isCache = isCache

        if (bannerDataModel?.slides == null || bannerDataModel.slides.isEmpty()) {
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
            homeData?.ticker?.tickers?.let { ticker ->
                if (!HomeFragment.HIDE_TICKER) {
                    ticker.filter { it.layout != StickyLoginConstant.LAYOUT_FLOATING }.let {
                        if (it.isNotEmpty()) {
                            visitableList.add(TickerDataModel(tickers = it))
                        }
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
            headerViewModel.isUserLogin = userSessionInterface?.isLoggedIn?:false
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
        val viewModelDynamicIcon = DynamicIconSectionDataModel(
                dynamicIconWrap = isDynamicIconWrapType,
                itemList = iconList
        )

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
                DynamicHomeChannel.Channels.LAYOUT_SPOTLIGHT -> {
                    homeData?.spotlight?.let { spotlight ->  createSpotlight(spotlight, isCache)} }
                DynamicHomeChannel.Channels.LAYOUT_HOME_WIDGET -> createBusinessUnitWidget(position)
                DynamicHomeChannel.Channels.LAYOUT_3_IMAGE, DynamicHomeChannel.Channels.LAYOUT_HERO ->
                    createDynamicChannel(
                            channel = channel,
                            trackingDataForCombination = channel.convertPromoEnhanceDynamicChannelDataLayerForCombination(),
                            isCombined = true)
                DynamicHomeChannel.Channels.LAYOUT_6_IMAGE, DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE, DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE -> {
                    createDynamicLegoBannerComponent(channel, position, isCache)
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
                    createRecommendationListCarouselComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_MIX_LEFT -> {
                    createMixLeftComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_PRODUCT_HIGHLIGHT -> {
                    createProductHighlightComponent(channel, position, isCache)

                }
                DynamicHomeChannel.Channels.LAYOUT_POPULAR_KEYWORD -> {createPopularKeywordChannel(channel = channel)}
                DynamicHomeChannel.Channels.LAYOUT_DEFAULT_ERROR -> { createDynamicChannel(channel = channel) }
                DynamicHomeChannel.Channels.LAYOUT_REVIEW -> { createReviewWidget(channel = channel) }
                DynamicHomeChannel.Channels.LAYOUT_PLAY_BANNER -> { createPlayWidget(channel) }
                DynamicHomeChannel.Channels.LAYOUT_MIX_TOP -> {
                        createMixTopComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_RECHARGE_RECOMMENDATION -> { createReminderWidget(ReminderEnum.RECHARGE) }
                DynamicHomeChannel.Channels.LAYOUT_SALAM_WIDGET -> {
                    createReminderWidget(ReminderEnum.SALAM)
                }
                DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET -> {
                    createDynamicChannel(
                            channel,
                            trackingData = CategoryWidgetTracking.getCategoryWidgetBanneImpression(
                                    channel.grids.toList(),
                                    userSessionInterface?.userId?:"",
                                    false,
                                    channel
                            ),
                            isCombined = false
                    )
                }
                DynamicHomeChannel.Channels.LAYOUT_BANNER_ADS -> {
                    createTopAdsBannerModel(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_BANNER -> { createPlayCarouselWidget(channel, position) }
            }
        }

        return this
    }

    private fun createPlayWidget(channel: DynamicHomeChannel.Channels) {
        if (!isCache) {
            val playBanner = PlayCardDataModel(channel, null)
            if (!visitableList.contains(playBanner)) visitableList.add(playBanner)
        }
    }

    private fun createPlayCarouselWidget(channel: DynamicHomeChannel.Channels, position: Int) {
        if (!isCache) {
            val playBanner = mappingPlayCarouselChannel(channel, position, HashMap(), isCache)
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

    private fun createDynamicLegoBannerComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(mappingDynamicLegoBannerComponent(
                channel,
                isCache,
                verticalPosition
        ))
        context?.let { HomeTrackingUtils.homeDiscoveryWidgetImpression(it,
                visitableList.size, channel) }
    }

    private fun createRecommendationListCarouselComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(mappingRecommendationListCarouselComponent(
                channel,
                isCache,
                verticalPosition
        ))
        context?.let { HomeTrackingUtils.homeDiscoveryWidgetImpression(it,
                visitableList.size, channel) }
    }

    private fun createProductHighlightComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(mappingProductHighlightComponent(
                channel,
                isCache,
                verticalPosition
        ))
        context?.let { HomeTrackingUtils.homeDiscoveryWidgetImpression(it,
                visitableList.size, channel) }
    }

    private fun createMixLeftComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(mappingMixLeftComponent(
                channel, isCache, verticalPosition
        ))
        context?.let { HomeTrackingUtils.homeDiscoveryWidgetImpression(it,
                visitableList.size, channel) }
    }

    private fun createMixTopComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(mappingMixTopComponent(
                channel, isCache, verticalPosition
        ))
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
        if (!isCache) {
            if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT) {
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL) {
                // do nothing
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
            } else if(channel.layout == DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET) {
                channel.promoName = String.format(PROMO_NAME_CATEGORY_WIDGET, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if(channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_ADS) {
                channel.promoName = String.format(PROMO_NAME_TOPADS_BANNER, position.toString(), channel.header.name)
                channel.setPosition(position)
            }
            else {
                val headerName = if (channel.header.name.isEmpty()) VALUE_BANNER_UNKNOWN else channel.header.name
                val layoutType = if (channel.layout.isEmpty()) VALUE_BANNER_UNKNOWN_LAYOUT_TYPE else channel.layout
                channel.promoName = String.format(PROMO_NAME_UNKNOWN, position.toString(), layoutType, headerName)
            }
        }
    }

    private fun createReviewWidget(channel: DynamicHomeChannel.Channels) {
        if (!isCache) visitableList.add(ReviewDataModel(channel = channel))
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

    private fun mappingDynamicLegoBannerComponent(channel: DynamicHomeChannel.Channels,
                                                  isCache: Boolean,
                                                  verticalPosition: Int): Visitable<*> {
        val viewModel = DynamicLegoBannerDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition)
        )
        if (!isCache) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                    trackingQueue,
                    channel.convertPromoEnhanceLegoBannerDataLayerForCombination())
        }
        return viewModel
    }

    private fun mappingRecommendationListCarouselComponent(channel: DynamicHomeChannel.Channels,
                                                  isCache: Boolean,
                                                  verticalPosition: Int): Visitable<*> {
        val viewModel = RecommendationListCarouselDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition)
        )
        if (!isCache) {
            trackingQueue?.putEETracking(
                    HomePageTrackingV2.RecommendationList.getRecommendationListImpression(channel,  userId = userSessionInterface?.userId ?: "") as HashMap<String, Any>
            )
        }
        return viewModel
    }

    private fun mappingProductHighlightComponent(channel: DynamicHomeChannel.Channels,
                                                  isCache: Boolean,
                                                  verticalPosition: Int): Visitable<*> {
        val viewModel = ProductHighlightDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition)
        )
        if (!isCache) {

            trackingQueue?.putEETracking(
                    ProductHighlightTracking.getProductHighlightImpression(channel,  userId = userSessionInterface?.userId ?: "") as HashMap<String, Any>
            )
        }
        return viewModel
    }

    private fun mappingMixLeftComponent(channel: DynamicHomeChannel.Channels,
                                        isCache: Boolean,
                                        verticalPosition: Int): Visitable<*> {
        return MixLeftDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition)
        )
    }

    private fun mappingMixTopComponent(channel: DynamicHomeChannel.Channels,
                                        isCache: Boolean,
                                        verticalPosition: Int): Visitable<*> {
        return MixTopDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition)
        )
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

    private fun mappingPlayCarouselChannel(channel: DynamicHomeChannel.Channels,
                                           position: Int,
                                           trackingData: MutableMap<String, Any>,
                                           isCache: Boolean): Visitable<*> {
        val playCardViewModel = PlayCarouselCardDataModel(channel = channel, position = position)
        if (!isCache) {
            playCardViewModel.setTrackingData(trackingData)
        }
        return playCardViewModel
    }

    private fun createPopularKeywordChannel(channel: DynamicHomeChannel.Channels) {
        visitableList.add(PopularKeywordListDataModel(popularKeywordList = mutableListOf(), channel = channel))
    }

    private fun createTopAdsBannerModel(channel: DynamicHomeChannel.Channels) {
        visitableList.add(HomeTopAdsBannerDataModel(null, channel = channel))
    }

    private fun createReminderWidget(source: ReminderEnum){
        if (!isCache) visitableList.add(ReminderWidgetModel(source=source))
    }

    override fun build(): List<Visitable<*>> = visitableList
}