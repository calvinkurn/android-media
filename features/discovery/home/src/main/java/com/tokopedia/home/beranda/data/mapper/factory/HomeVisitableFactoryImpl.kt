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
import com.tokopedia.remoteconfig.RemoteConfigKey.HOME_USE_GLOBAL_COMPONENT
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

    override fun build(): List<Visitable<*>> = visitableList
}