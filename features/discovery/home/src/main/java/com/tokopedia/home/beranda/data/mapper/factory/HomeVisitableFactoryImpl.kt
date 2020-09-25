package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeoLocationPromptDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

class HomeVisitableFactoryImpl(
        val userSessionInterface: UserSessionInterface?,
        val remoteConfig: RemoteConfig,
        private val homeDefaultDataSource: HomeDefaultDataSource) : HomeVisitableFactory {
    private var context: Context? = null
    private var trackingQueue: TrackingQueue? = null
    private var homeData: HomeData? = null
    private var isCache: Boolean = true
    private var dynamicChannelDataMapper: HomeDynamicChannelDataMapper? = null
    private var visitableList: MutableList<Visitable<*>> = mutableListOf()

    companion object{
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

    override fun buildVisitableList(homeData: HomeData, isCache: Boolean, trackingQueue: TrackingQueue, context: Context, dynamicChannelDataMapper: HomeDynamicChannelDataMapper): HomeVisitableFactory {
        this.homeData = homeData
        this.isCache = isCache
        this.visitableList = mutableListOf()
        this.trackingQueue = trackingQueue
        this.context = context
        this.dynamicChannelDataMapper = dynamicChannelDataMapper
        return this
    }

    override fun addBannerVisitable(): HomeVisitableFactory {
        val bannerViewModel = HomepageBannerDataModel()
        var bannerDataModel = homeData?.banner
        if (bannerDataModel?.slides == null || bannerDataModel.slides?.isEmpty() == true) {
            bannerDataModel = homeDefaultDataSource.createDefaultHomePageBanner()
            bannerViewModel.slides = bannerDataModel.slides
        } else {
            bannerDataModel.slides?.forEachIndexed { index, bannerSlidesModel ->
                bannerSlidesModel.position = index+1
            }
            bannerViewModel.slides = bannerDataModel.slides
        }
        bannerViewModel.isCache = isCache

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
        var isDynamicIconWrapType = homeData?.homeFlag?.getFlag(HomeFlag.TYPE.DYNAMIC_ICON_WRAP)?: false
        var iconList = homeData?.dynamicHomeIcon?.dynamicIcon?: listOf()
        if (iconList.isEmpty()) {
            iconList = homeDefaultDataSource.createDefaultHomeDynamicIcon().dynamicIcon
            isDynamicIconWrapType = true
        }

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

    override fun addDynamicChannelVisitable(addLoadingMore: Boolean): HomeVisitableFactory {
        homeData?.let {
            val data = dynamicChannelDataMapper?.mapToDynamicChannelDataModel(
                    HomeChannelData(it.dynamicHomeChannel), false, addLoadingMore)
            data?.let { it1 -> visitableList.addAll(it1) }
        }
        return this
    }

    override fun build(): List<Visitable<*>> = visitableList
}