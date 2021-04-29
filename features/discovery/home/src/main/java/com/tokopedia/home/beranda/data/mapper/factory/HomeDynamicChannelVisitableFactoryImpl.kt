package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.analytics.v2.CategoryWidgetTracking
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.analytics.v2.RecommendationListTracking
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.visitable.*
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

class HomeDynamicChannelVisitableFactoryImpl(
        val userSessionInterface: UserSessionInterface?,
        val remoteConfig: RemoteConfig,
        private val homeDefaultDataSource: HomeDefaultDataSource) : HomeDynamicChannelVisitableFactory {
    private var context: Context? = null
    private var trackingQueue: TrackingQueue? = null
    private var homeChannelData: HomeChannelData? = null
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
        private const val PROMO_NAME_LEGO_6_AUTO_IMAGE = "/ - p%s - lego banner 6 auto - %s - %s"

        private const val PROMO_NAME_LEGO_3_IMAGE = "/ - p%s - lego banner 3 image - %s"
        private const val PROMO_NAME_LEGO_4_IMAGE = "/ - p%s - lego banner 4 image - %s"
        private const val PROMO_NAME_LEGO_2_IMAGE = "/ - p%s - lego banner 2 image - %s"
        private const val PROMO_NAME_MIX_LEFT = "/ - p%s - mix left - %s"
        private const val PROMO_NAME_CATEGORY_WIDGET = "/ - p%s - category widget banner - %s"
        private const val PROMO_NAME_SPRINT = "/ - p%s - %s"
        private const val PROMO_NAME_SPOTLIGHT_BANNER = "/ - p%s - spotlight banner"
        private const val PROMO_NAME_GIF_BANNER = "/ - p%s - lego banner gif - %s"
        private const val PROMO_NAME_DC_MIX_BANNER = "/ - p%s - dynamic channel mix - banner - %s"
        private const val PROMO_NAME_UNKNOWN = "/ - p%s - %s - %s"
        private const val PROMO_NAME_TOPADS_BANNER = "/ - p%s - dynamic channel ads - %s"
        private const val PROMO_NAME_BANNER_CAROUSEL = "/ - p%s - dynamic channel carousel - %s"

        private const val VALUE_BANNER_UNKNOWN = "banner unknown"
        private const val VALUE_BANNER_DEFAULT = "default"
        private const val VALUE_BANNER_UNKNOWN_LAYOUT_TYPE = "lego banner unknown"
    }

    override fun buildVisitableList(homeChannelData: HomeChannelData, isCache: Boolean, trackingQueue: TrackingQueue, context: Context): HomeDynamicChannelVisitableFactory {
        this.homeChannelData = homeChannelData
        this.isCache = isCache
        this.visitableList = mutableListOf()
        this.trackingQueue = trackingQueue
        this.context = context
        return this
    }

    override fun addDynamicChannelVisitable(addLoadingMore: Boolean, useDefaultWhenEmpty: Boolean, startPosition: Int): HomeDynamicChannelVisitableFactory {
        var dynamicChannelList = mutableListOf<DynamicHomeChannel.Channels>()
        if ((homeChannelData?.dynamicHomeChannel == null
                        || homeChannelData?.dynamicHomeChannel?.channels == null
                        || homeChannelData?.dynamicHomeChannel?.channels?.isEmpty() == true) && useDefaultWhenEmpty) {
            homeDefaultDataSource
            dynamicChannelList = homeDefaultDataSource.createDefaultHomeDynamicChannel().channels as MutableList<DynamicHomeChannel.Channels>
        } else if (homeChannelData?.dynamicHomeChannel?.channels?.isNotEmpty() == true) {
            dynamicChannelList = homeChannelData?.dynamicHomeChannel?.channels as MutableList<DynamicHomeChannel.Channels>
        }
        dynamicChannelList.forEachIndexed { index, channel ->
            val position = index+ 1 + startPosition
            setDynamicChannelPromoName(position, channel)
            when (channel.layout) {
                DynamicHomeChannel.Channels.LAYOUT_HOME_WIDGET -> createBusinessUnitWidget(position)
                DynamicHomeChannel.Channels.LAYOUT_3_IMAGE, DynamicHomeChannel.Channels.LAYOUT_HERO ->
                    createDynamicChannel(
                            channel = channel,
                            trackingDataForCombination = channel.convertPromoEnhanceDynamicChannelDataLayerForCombination(),
                            isCombined = true)
                DynamicHomeChannel.Channels.LAYOUT_6_IMAGE,
                DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE,
                DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE,
                DynamicHomeChannel.Channels.LAYOUT_LEGO_2_IMAGE-> {
                    createDynamicLegoBannerComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_LEGO_6_AUTO -> {
                    createDynamicLegoBannerSixAutoComponent(channel, position, isCache)
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
                DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF -> {
                    createDynamicChannel(channel = channel)
                    if (!isCache) trackingQueue?.putEETracking(HomePageTracking.getEventEnhanceImpressionBannerGif(channel))
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
                DynamicHomeChannel.Channels.LAYOUT_POPULAR_KEYWORD -> {
                    createPopularKeywordChannel(channel = channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_DEFAULT_ERROR -> {
                    createDynamicChannel(channel = channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_REVIEW -> {
                    createReviewWidget(channel = channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_PLAY_BANNER -> {
                    createPlayWidget(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_MIX_TOP -> {
                    createMixTopComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_RECHARGE_RECOMMENDATION -> {
                    createReminderWidget(ReminderEnum.RECHARGE)
                }
                DynamicHomeChannel.Channels.LAYOUT_SALAM_WIDGET -> {
                    createReminderWidget(ReminderEnum.SALAM)
                }
                DynamicHomeChannel.Channels.LAYOUT_RECHARGE_BU_WIDGET -> {
                    createRechargeBUWidget(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET -> {
                    createDynamicChannel(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_BANNER_ADS -> {
                    createTopAdsBannerModel(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_LEGO_4_AUTO -> {
                    createLego4AutoComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_FEATURED_SHOP -> {
                    createFeaturedShopComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_BANNER -> {
                    createCarouselPlayWidget(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_CATEGORY_ICON -> {
                    createCategoryIconComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_BEST_SELLING -> {
                    createBestSellingWidget(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL_V2 -> {
                    createBannerChannel(channel, position)
                }
            }
        }
        if (addLoadingMore) {
            createDynamicChannelLoadingMore()
        }

        return this
    }

    private fun createFeaturedShopComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(FeaturedShopDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition)
        ))
        if (!isCache) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                    trackingQueue,
                    channel.convertPromoEnhanceLegoBannerDataLayerForCombination())
        }
        context?.let { HomeTrackingUtils.homeDiscoveryWidgetImpression(it,
                visitableList.size, channel) }
    }

    private fun createCategoryIconComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(CategoryNavigationDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition)
        ))
        if (!isCache) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                    trackingQueue,
                    channel.convertPromoEnhanceLegoBannerDataLayerForCombination())
        }
        context?.let { HomeTrackingUtils.homeDiscoveryWidgetImpression(it,
                visitableList.size, channel) }
    }

    private fun createDynamicChannelLoadingMore() {
        visitableList.add(DynamicChannelLoadingModel())
    }

    private fun createPlayWidget(channel: DynamicHomeChannel.Channels) {
        if (!isCache) {
            val playBanner = PlayCardDataModel(channel, null)
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

    private fun createDynamicLegoBannerSixAutoComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(mappingDynamicLegoSixAutoBannerComponent(
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

    private fun createLego4AutoComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(mappingLego4BannerAutoComponent(
                channel, isCache, verticalPosition
        ))
        if (!isCache) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                    trackingQueue,
                    channel.convertPromoEnhanceLegoBannerDataLayerForCombination())
        }
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

    private fun createBestSellingWidget(channel: DynamicHomeChannel.Channels){
        if(!isCache) {
            visitableList.add(BestSellerDataModel(id = channel.id, pageName = channel.pageName, widgetParam = channel.widgetParam))
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
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_6_AUTO) {
                channel.promoName = String.format(PROMO_NAME_LEGO_6_AUTO_IMAGE, position.toString(), "individual_grid", channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE) {
                channel.promoName = String.format(PROMO_NAME_LEGO_3_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE ||
                    channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_4_AUTO) {
                channel.promoName = String.format(PROMO_NAME_LEGO_4_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_2_IMAGE)  {
                channel.promoName = String.format(PROMO_NAME_LEGO_2_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO || channel.layout == DynamicHomeChannel.Channels.LAYOUT_ORGANIC) {
                channel.promoName = String.format(PROMO_NAME_SPRINT, position.toString(), channel.header.name)
                channel.setPosition(position)
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
            } else if(channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL_V2) {
                channel.promoName = String.format(PROMO_NAME_BANNER_CAROUSEL, position.toString(),
                        if (channel.header.name.isNotEmpty()) channel.header.name
                        else VALUE_BANNER_DEFAULT
                )
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
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache = isCache
        )
        if (!isCache) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                    trackingQueue,
                    channel.convertPromoEnhanceLegoBannerDataLayerForCombination())
        }
        return viewModel
    }

    private fun mappingDynamicLegoSixAutoBannerComponent(channel: DynamicHomeChannel.Channels,
                                                         isCache: Boolean,
                                                         verticalPosition: Int): Visitable<*> {
        val viewModel = DynamicLegoBannerSixAutoDataModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache = isCache
        )
        if (!isCache) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                    trackingQueue,
                    LegoBannerTracking.convertLegoSixAutoBannerDataLayerForCombination(channel, verticalPosition))
        }
        return viewModel
    }

    private fun mappingRecommendationListCarouselComponent(channel: DynamicHomeChannel.Channels,
                                                           isCache: Boolean,
                                                           verticalPosition: Int): Visitable<*> {
        val viewModel = RecommendationListCarouselDataModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache = isCache
        )
        return viewModel
    }

    private fun mappingProductHighlightComponent(channel: DynamicHomeChannel.Channels,
                                                 isCache: Boolean,
                                                 verticalPosition: Int): Visitable<*> {
        val viewModel = ProductHighlightDataModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache = isCache
        )
        return viewModel
    }

    private fun mappingMixLeftComponent(channel: DynamicHomeChannel.Channels,
                                        isCache: Boolean,
                                        verticalPosition: Int): Visitable<*> {
        return MixLeftDataModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache = isCache
        )
    }

    private fun mappingMixTopComponent(channel: DynamicHomeChannel.Channels,
                                       isCache: Boolean,
                                       verticalPosition: Int): Visitable<*> {
        return MixTopDataModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache = isCache
        )
    }

    private fun mappingLego4BannerAutoComponent(channel: DynamicHomeChannel.Channels,
                                                isCache: Boolean,
                                                verticalPosition: Int): Visitable<*> {
        return Lego4AutoDataModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache = isCache
        )
    }

    private fun createPopularKeywordChannel(channel: DynamicHomeChannel.Channels) {
        visitableList.add(PopularKeywordListDataModel(popularKeywordList = mutableListOf(), channel = channel))
    }

    private fun createBannerChannel(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        visitableList.add(BannerDataModel(DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition), isCache))
    }

    private fun createTopAdsBannerModel(channel: DynamicHomeChannel.Channels) {
        if (!isCache) visitableList.add(HomeTopAdsBannerDataModel(null, channel = channel))
    }

    private fun createReminderWidget(source: ReminderEnum){
        if (!isCache) visitableList.add(ReminderWidgetModel(source=source))
    }

    private fun createRechargeBUWidget(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        if (!isCache) visitableList.add(RechargeBUWidgetDataModel(
                channel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isDataCache = isCache
        ))
    }

    override fun build(): List<Visitable<*>> = visitableList

    /**
     * Play Widget
     */
    private fun createCarouselPlayWidget(dynamicHomeChannel: DynamicHomeChannel.Channels, position: Int) {
        if (isCache) return
        val dataModel = CarouselPlayWidgetDataModel(
                dynamicHomeChannel.apply {
                    setPosition(position)
                }
        )
        val listOfRegisteredPlayWidget = visitableList.filterIsInstance(CarouselPlayWidgetDataModel::class.java)
        if (listOfRegisteredPlayWidget.isEmpty()) visitableList.add(dataModel)
    }
}