package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToChannelGrid
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToHomeComponentHeader
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToTrackingAttributionModel
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home_component.util.HomeComponentRemoteConfigController
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.util.ChannelStyleUtil.BORDER_STYLE_PADDING
import com.tokopedia.home_component.util.ChannelStyleUtil.parseBorderStyle
import com.tokopedia.home_component.util.ChannelStyleUtil.parseDividerSize
import com.tokopedia.home_component.visitable.*
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.item.ProductCardGridShimmerDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.home_component.widget.special_release.SpecialReleaseRevampDataModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

class HomeDynamicChannelVisitableFactoryImpl(
    val userSessionInterface: UserSessionInterface?,
    val remoteConfig: RemoteConfig,
    private val homeDefaultDataSource: HomeDefaultDataSource
) : HomeDynamicChannelVisitableFactory {
    private var context: Context? = null
    private var trackingQueue: TrackingQueue? = null
    private var homeChannelData: HomeChannelData? = null
    private var isCache: Boolean = true
    private var visitableList: MutableList<Visitable<*>> = mutableListOf()

    companion object {
        private const val PROMO_NAME_LEGO_6_IMAGE = "/ - p%s - lego banner - %s"
        private const val PROMO_NAME_LEGO_6_AUTO_IMAGE = "/ - p%s - lego banner 6 auto - %s - %s"

        private const val PROMO_NAME_LEGO_4_IMAGE = "/ - p%s - lego banner 4 image - %s"
        private const val PROMO_NAME_LEGO_2_IMAGE = "/ - p%s - lego banner 2 image - %s"
        private const val PROMO_NAME_MIX_LEFT = "/ - p%s - mix left - %s"
        private const val PROMO_NAME_CATEGORY_WIDGET = "/ - p%s - category widget banner - %s"
        private const val PROMO_NAME_CATEGORY_WIDGET_V2 = "/ - p%s - category widget banner - %s"
        private const val PROMO_NAME_SPRINT = "/ - p%s - %s"
        private const val PROMO_NAME_UNKNOWN = "/ - p%s - %s - %s"
        private const val PROMO_NAME_TOPADS_BANNER = "/ - p%s - dynamic channel ads - %s"
        private const val PROMO_NAME_BANNER_CAROUSEL = "/ - p%s - dynamic channel carousel - %s"
        private const val PROMO_NAME_BANNER_SPECIAL_RELEASE = "/ - p%s - dynamic channel feature campaign - banner - %s"

        private const val VALUE_BANNER_UNKNOWN = "banner unknown"
        private const val VALUE_BANNER_DEFAULT = "default"
        private const val VALUE_BANNER_UNKNOWN_LAYOUT_TYPE = "lego banner unknown"
        private const val VALUE_EMPTY_APPLINK = "-"

        private const val CUE_WIDGET_MIN_SIZE = 4
        private const val VPS_WIDGET_SIZE = 4
        private const val LEGO_4_PRODUCT_SIZE = 4
        private const val DEALS_WIDGET_SIZE = 4
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
        if ((
            homeChannelData?.dynamicHomeChannel == null ||
                homeChannelData?.dynamicHomeChannel?.channels == null ||
                homeChannelData?.dynamicHomeChannel?.channels?.isEmpty() == true
            ) && useDefaultWhenEmpty
        ) {
            homeDefaultDataSource
            dynamicChannelList = homeDefaultDataSource.createDefaultHomeDynamicChannel().channels as MutableList<DynamicHomeChannel.Channels>
        } else if (homeChannelData?.dynamicHomeChannel?.channels?.isNotEmpty() == true) {
            dynamicChannelList = homeChannelData?.dynamicHomeChannel?.channels as MutableList<DynamicHomeChannel.Channels>
        }
        dynamicChannelList.forEachIndexed { index, channel ->
            val position = index + startPosition
            setDynamicChannelPromoName(position, channel)
            when (channel.layout) {
                DynamicHomeChannel.Channels.LAYOUT_HOME_WIDGET -> createBusinessUnitWidget(channel = channel, position = position)
                DynamicHomeChannel.Channels.LAYOUT_6_IMAGE,
                DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE,
                DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE,
                DynamicHomeChannel.Channels.LAYOUT_LEGO_2_IMAGE -> {
                    createDynamicLegoBannerComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_LEGO_6_AUTO -> {
                    createDynamicLegoBannerSixAutoComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_LIST_CAROUSEL -> {
                    createRecommendationListCarouselComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_MIX_LEFT -> {
                    val borderStyle = channel.styleParam.parseBorderStyle()
                    if (borderStyle == BORDER_STYLE_PADDING) {
                        createMixLeftPaddingComponent(channel, position, isCache)
                    } else {
                        createMixLeftComponent(channel, position, isCache)
                    }
                }
                DynamicHomeChannel.Channels.LAYOUT_PRODUCT_HIGHLIGHT -> {
                    createProductHighlightComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_POPULAR_KEYWORD -> {
                    createPopularKeywordChannel(channel = channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_DEFAULT_ERROR -> {
                    createDynamicChannelError(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_REVIEW -> {
                    createReviewWidget(channel = channel)
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
                DynamicHomeChannel.Channels.LAYOUT_CAMPAIGN_WIDGET -> {
                    createCampaignWidget(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_CAMPAIGN_FEATURING -> {
                    createCampaignFeaturingWidget(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET -> {
//                    HomeTrackingUtils.homeDiscoveryWidgetViewAll(
//                        context,
//                        DynamicLinkHelper.getActionLink(channel.header)
//                    )
                    createCategoryWidget(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET_V2 -> {
                    createCategoryWidgetV2(
                        channel,
                        position,
                        isCache
                    )
                }
                DynamicHomeChannel.Channels.LAYOUT_BANNER_ADS -> {
                    createTopAdsBannerModel(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_VERTICAL_BANNER_ADS -> {
                    createTopAdsVerticalBannerModel(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_FEATURED_SHOP -> {
                    createFeaturedShopComponent(channel, position, isCache)
                }
                DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_BANNER,
                DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_NEW_NO_PRODUCT,
                DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_NEW_WITH_PRODUCT -> {
                    createCarouselPlayWidget(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_BEST_SELLING -> {
                    createBestSellingWidget(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_BEST_SELLING_LIST -> {
                    createBestSellingListWidget(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL_V2 -> {
                    createBannerChannel(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_MERCHANT_VOUCHER -> {
                    createMerchantVoucher(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_CM_HOME_TO_DO -> {
                    createHomeToDoWidget(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_PAYLATER_CICIL -> {
                    createPayLaterHomeToDoWidget(channel)
                }
                DynamicHomeChannel.Channels.LAYOUT_CUE_WIDGET -> {
                    createCueCategory(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_VPS_WIDGET -> {
                    createVpsWidget(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_MISSION_WIDGET -> {
                    createMissionWidgetChannel(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_LEGO_4_PRODUCT -> {
                    createLego4Product(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_TODO_WIDGET_REVAMP -> {
                    createTodoWidget(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_DEALS_WIDGET -> {
                    createDealsWidget(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_FLASH_SALE_WIDGET -> {
                    createFlashSaleWidget(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_SPECIAL_RELEASE_REVAMP -> {
                    createSpecialReleaseRevamp(channel, position)
                }
                DynamicHomeChannel.Channels.LAYOUT_SPECIAL_SHOP_FLASH_SALE -> {
                    createShopFlashSale(channel, position)
                }
            }
        }
        if (addLoadingMore) {
            createDynamicChannelLoadingMore()
        }

        return this
    }

    private fun createFeaturedShopComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            FeaturedShopDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponentBannerHeader(channel, verticalPosition)
            )
        )
        if (!isCache && channel.convertPromoEnhanceLegoBannerDataLayerForCombination().isNotEmpty() &&
            !HomeComponentRemoteConfigController.isUsingNewLegoTracking(remoteConfig)) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                trackingQueue,
                channel.convertPromoEnhanceLegoBannerDataLayerForCombination()
            )
        }
        context?.let {
            HomeTrackingUtils.homeDiscoveryWidgetImpression(
                it,
                visitableList.size,
                channel
            )
        }
    }

    private fun createDynamicChannelLoadingMore() {
        visitableList.add(DynamicChannelLoadingModel())
    }

    private fun createDynamicChannelError(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        visitableList.add(DynamicChannelErrorModel(DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition)))
    }

    private fun createDynamicLegoBannerComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            mappingDynamicLegoBannerComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
        context?.let {
            HomeTrackingUtils.homeDiscoveryWidgetImpression(
                it,
                visitableList.size,
                channel
            )
        }
    }

    private fun createDynamicLegoBannerSixAutoComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            mappingDynamicLegoSixAutoBannerComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
        context?.let {
            HomeTrackingUtils.homeDiscoveryWidgetImpression(
                it,
                visitableList.size,
                channel
            )
        }
    }

    private fun createRecommendationListCarouselComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            mappingRecommendationListCarouselComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
        context?.let {
            HomeTrackingUtils.homeDiscoveryWidgetImpression(
                it,
                visitableList.size,
                channel
            )
        }
    }

    private fun createProductHighlightComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            mappingProductHighlightComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
        context?.let {
            HomeTrackingUtils.homeDiscoveryWidgetImpression(
                it,
                visitableList.size,
                channel
            )
        }
    }

    private fun createMixLeftComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            mappingMixLeftComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
        context?.let {
            HomeTrackingUtils.homeDiscoveryWidgetImpression(
                it,
                visitableList.size,
                channel
            )
        }
    }

    private fun createMixLeftPaddingComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            mappingMixLeftPaddingComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
        context?.let {
            HomeTrackingUtils.homeDiscoveryWidgetImpression(
                it,
                visitableList.size,
                channel
            )
        }
    }

    private fun createMixTopComponent(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            mappingMixTopComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
        context?.let {
            HomeTrackingUtils.homeDiscoveryWidgetImpression(
                it,
                visitableList.size,
                channel
            )
        }
    }

    private fun createBusinessUnitWidget(channel: DynamicHomeChannel.Channels, position: Int) {
        if (!isCache) {
            visitableList.add(
                NewBusinessUnitWidgetDataModel(
                    isCache = false,
                    channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, position)
                )
            )
        }
    }

    private fun createBestSellingWidget(channel: DynamicHomeChannel.Channels) {
        // best seller widget limited to only 1 widget per list
        if (!isCache && !visitableList.any { it is BestSellerDataModel }) {
            visitableList.add(
                BestSellerDataModel(
                    id = channel.id,
                    pageName = channel.pageName,
                    widgetParam = channel.widgetParam,
                    dividerType = channel.dividerType,
                    dividerSize = channel.styleParam.parseDividerSize(),
                    channelHeader = ChannelHeader(
                        channel.header.id,
                        channel.header.name,
                        channel.header.subtitle,
                        channel.header.expiredTime,
                        channel.header.serverTimeUnix,
                        channel.header.applink,
                        channel.header.url,
                        channel.header.backColor,
                        channel.header.backImage,
                        channel.header.textColor,
                        channelId = channel.id,
                        serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(
                            channel.header.serverTimeUnix
                        )
                    )
                )
            )
        }
    }

    private fun createBestSellingListWidget(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int,
    ) {
        visitableList.add(
            BestSellerDataModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                    channel = channel.copy(header = channel.header.copy(applink = VALUE_EMPTY_APPLINK)),
                    verticalPosition = verticalPosition,
                ),
            )
        )
    }

    private fun createCampaignWidget(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int,
        isCache: Boolean
    ) {
        if (!isCache) {
            visitableList.add(
                mappingCampaignWidgetComponent(
                    channel,
                    isCache,
                    verticalPosition
                )
            )
        }
    }

    private fun createCampaignFeaturingWidget(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int,
        isCache: Boolean
    ) {
        visitableList.add(
            mappingCampaignFeaturingComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
    }

    private fun setDynamicChannelPromoName(position: Int, channel: DynamicHomeChannel.Channels) {
        if (!isCache) {
            if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_6_IMAGE) {
                channel.promoName =
                    String.format(PROMO_NAME_LEGO_6_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_6_AUTO) {
                channel.promoName = String.format(PROMO_NAME_LEGO_6_AUTO_IMAGE, position.toString(), "individual_grid", channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE) {
                channel.promoName = String.format(PROMO_NAME_LEGO_4_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_LEGO_2_IMAGE) {
                channel.promoName = String.format(PROMO_NAME_LEGO_2_IMAGE, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_TOPADS) {
                channel.promoName = String.format(PROMO_NAME_SPRINT, position.toString(), channel.header.name)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_REVIEW) {
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_MIX_LEFT) {
                channel.promoName = String.format(PROMO_NAME_MIX_LEFT, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET) {
                channel.promoName = String.format(PROMO_NAME_CATEGORY_WIDGET, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_CATEGORY_WIDGET_V2) {
                channel.promoName = String.format(PROMO_NAME_CATEGORY_WIDGET_V2, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_ADS) {
                channel.promoName = String.format(PROMO_NAME_TOPADS_BANNER, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_VERTICAL_BANNER_ADS) {
                channel.promoName = String.format(PROMO_NAME_TOPADS_BANNER, position.toString(), channel.header.name)
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL_V2) {
                channel.promoName = String.format(
                    PROMO_NAME_BANNER_CAROUSEL,
                    position.toString(),
                    if (channel.header.name.isNotEmpty()) {
                        channel.header.name
                    } else {
                        VALUE_BANNER_DEFAULT
                    }
                )
                channel.setPosition(position)
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_CAMPAIGN_FEATURING) {
                channel.promoName = String.format(
                    PROMO_NAME_BANNER_SPECIAL_RELEASE,
                    position.toString(),
                    if (channel.header.name.isNotEmpty()) {
                        channel.header.name
                    } else {
                        VALUE_BANNER_DEFAULT
                    }
                )
            } else if (channel.layout == DynamicHomeChannel.Channels.LAYOUT_MERCHANT_VOUCHER) {
                channel.promoName = String.format(
                    PROMO_NAME_BANNER_CAROUSEL,
                    position.toString(),
                    if (channel.header.name.isNotEmpty()) {
                        channel.header.name
                    } else {
                        VALUE_BANNER_DEFAULT
                    }
                )
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

    private fun mappingDynamicLegoBannerComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        val viewModel = DynamicLegoBannerDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
            isCache = isCache,
            cardInteraction = true
        )
        if (!isCache && !HomeComponentRemoteConfigController.isUsingNewLegoTracking(remoteConfig)) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                trackingQueue,
                channel.convertPromoEnhanceLegoBannerDataLayerForCombination(),
                userSessionInterface?.userId.orEmpty()
            )
        }
        return viewModel
    }

    private fun mappingDynamicLegoSixAutoBannerComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        val viewModel = DynamicLegoBannerSixAutoDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
            isCache = isCache
        )
        if (!isCache && !HomeComponentRemoteConfigController.isUsingNewLegoTracking(remoteConfig)) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(
                trackingQueue,
                LegoBannerTracking.convertLegoSixAutoBannerDataLayerForCombination(channel, verticalPosition)
            )
        }
        return viewModel
    }

    private fun mappingRecommendationListCarouselComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        val viewModel = RecommendationListCarouselDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
            isCache = isCache
        )
        return viewModel
    }

    private fun mappingProductHighlightComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        val viewModel = ProductHighlightDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
            isCache = isCache
        )
        return viewModel
    }

    private fun mappingMixLeftComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return MixLeftDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
            isCache = isCache
        )
    }

    private fun mappingMixLeftPaddingComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return MixLeftPaddingDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache
        )
    }

    private fun mappingMixTopComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return MixTopDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache
        )
    }

    private fun mappingCampaignWidgetComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return CampaignWidgetDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache
        )
    }

    private fun mappingCampaignFeaturingComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return SpecialReleaseDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache
        )
    }

    private fun mappingMerchantVoucherComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return MerchantVoucherDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
            isCache = isCache
        )
    }

    private fun mappingCueCategoryComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return CueCategoryDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache
        )
    }

    private fun mappingVpsWidgetComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return VpsDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache
        )
    }

    private fun mappingLego4ProductComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return Lego4ProductDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache
        )
    }

    private fun mappingTodoWidgetComponent(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int
    ): Visitable<*> {
        return TodoWidgetListDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            status = TodoWidgetListDataModel.STATUS_LOADING
        )
    }

    private fun mappingDealsWidgetComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return DealsDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache
        )
    }

    private fun mappingFlashSaleWidgetComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return FlashSaleDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache,
            cardInteraction = false
        )
    }

    private fun mappingSpecialReleaseRevampComponent(
        channel: DynamicHomeChannel.Channels,
        isCache: Boolean,
        verticalPosition: Int
    ): Visitable<*> {
        return SpecialReleaseRevampDataModel(
            channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                channel,
                verticalPosition
            ),
            isCache = isCache,
            cardInteraction = false
        )
    }

    private fun createMissionWidgetChannel(
        channel: DynamicHomeChannel.Channels,
        verticalPosition: Int
    ) {
        if (!isCache) {
            visitableList.add(
                MissionWidgetListDataModel(
                    channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                        channel,
                        verticalPosition
                    ),
                    status = MissionWidgetListDataModel.STATUS_LOADING
                )
            )
        }
    }

    private fun createPopularKeywordChannel(channel: DynamicHomeChannel.Channels) {
        if (!isCache) {
            visitableList.add(
                PopularKeywordListDataModel(
                    popularKeywordList = mutableListOf(),
                    channel = channel
                )
            )
        }
    }

    private fun createBannerChannel(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        visitableList.add(
            BannerDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(
                    channel,
                    verticalPosition
                ),
                isCache,
                dimenMarginTop = com.tokopedia.home_component.R.dimen.home_banner_default_margin_vertical_design,
                dimenMarginBottom = com.tokopedia.home_component.R.dimen.home_banner_default_margin_vertical_design,
                cardInteraction = true
            )
        )
    }

    private fun createTopAdsBannerModel(channel: DynamicHomeChannel.Channels) {
        if (!isCache) visitableList.add(HomeTopAdsBannerDataModel(null, channel = channel))
    }

    private fun createTopAdsVerticalBannerModel(channel: DynamicHomeChannel.Channels) {
        if (!isCache) visitableList.add(HomeTopAdsVerticalBannerDataModel(null, channel = channel))
    }

    private fun createReminderWidget(source: ReminderEnum) {
        if (!isCache) visitableList.add(ReminderWidgetModel(source = source, id = generateReminderWidgetId(source)))
    }

    private fun generateReminderWidgetId(source: ReminderEnum): String {
        val numberOfExistingSource = visitableList.filter {
            it is ReminderWidgetModel && it.source.name == source.name
        }.size
        return source.name + (numberOfExistingSource + 1)
    }

    private fun createRechargeBUWidget(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        if (!isCache) {
            visitableList.add(
                RechargeBUWidgetDataModel(
                    channel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                    isDataCache = isCache
                )
            )
        }
    }

    private fun createCategoryWidget(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            CategoryWidgetDataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache
            )
        )
    }

    private fun createCategoryWidgetV2(channel: DynamicHomeChannel.Channels, verticalPosition: Int, isCache: Boolean) {
        visitableList.add(
            CategoryWidgetV2DataModel(
                DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, verticalPosition),
                isCache
            )
        )
    }

    private fun createMerchantVoucher(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        visitableList.add(
            mappingMerchantVoucherComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
    }

    private fun createCueCategory(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        val gridSize = channel.grids.size
        if (gridSize >= CUE_WIDGET_MIN_SIZE) {
            visitableList.add(
                mappingCueCategoryComponent(
                    channel,
                    isCache,
                    verticalPosition
                )
            )
        }
    }

    private fun createVpsWidget(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        val gridSize = channel.grids.size
        if (gridSize >= VPS_WIDGET_SIZE) {
            visitableList.add(
                mappingVpsWidgetComponent(
                    channel,
                    isCache,
                    verticalPosition
                )
            )
        }
    }

    private fun createLego4Product(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        val gridSize = channel.grids.size
        if (gridSize >= LEGO_4_PRODUCT_SIZE) {
            visitableList.add(
                mappingLego4ProductComponent(
                    channel,
                    isCache,
                    verticalPosition
                )
            )
        }
    }

    private fun createTodoWidget(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        visitableList.add(
            mappingTodoWidgetComponent(
                channel,
                verticalPosition
            )
        )
    }

    private fun createDealsWidget(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        val gridSize = channel.grids.size
        if (gridSize >= DEALS_WIDGET_SIZE) {
            visitableList.add(
                mappingDealsWidgetComponent(
                    channel,
                    isCache,
                    verticalPosition
                )
            )
        }
    }

    private fun createFlashSaleWidget(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        visitableList.add(
            mappingFlashSaleWidgetComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
    }

    private fun createSpecialReleaseRevamp(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        visitableList.add(
            mappingSpecialReleaseRevampComponent(
                channel,
                isCache,
                verticalPosition
            )
        )
    }

    private fun createShopFlashSale(channel: DynamicHomeChannel.Channels, verticalPosition: Int) {
        if(!isCache) {
            visitableList.add(
                ShopFlashSaleWidgetDataModel(
                    id = channel.id,
                    channelHeader = channel.header.mapToHomeComponentHeader(),
                    channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                        channel,
                        verticalPosition,
                        mapGrids = false
                    ),
                    tabList = channel.grids.mapIndexed { index, grid ->
                        ShopFlashSaleTabDataModel(
                            grid.mapToChannelGrid(index),
                            channel.mapToTrackingAttributionModel(verticalPosition),
                            index == 0
                        )
                    },
                    itemList = ProductCardGridShimmerDataModel.getAsList()
                )
            )
        }
    }

    override fun build(): List<Visitable<*>> = visitableList

    /**
     * Play Widget
     */
    private fun createCarouselPlayWidget(dynamicHomeChannel: DynamicHomeChannel.Channels, position: Int) {
        if (isCache) return
        val dataModel = CarouselPlayWidgetDataModel(
            DynamicChannelComponentMapper.mapHomeChannelToComponent(dynamicHomeChannel, position)
        )
        val listOfRegisteredPlayWidget = visitableList.filterIsInstance(CarouselPlayWidgetDataModel::class.java)
        if (listOfRegisteredPlayWidget.isEmpty()) visitableList.add(dataModel)
    }

    private fun createHomeToDoWidget(channel: DynamicHomeChannel.Channels) {
        if (!isCache) visitableList.add(CMHomeWidgetDataModel(null, channel))
    }

    private fun createPayLaterHomeToDoWidget(channel: DynamicHomeChannel.Channels) {
        if (!isCache) visitableList.add(HomePayLaterWidgetDataModel(null, channel))
    }
}
