package com.tokopedia.home.beranda.data.mapper.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.model.AtfData
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeInitialShimmerDataModel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.constant.AtfKey.TYPE_BANNER
import com.tokopedia.home.constant.AtfKey.TYPE_CHANNEL
import com.tokopedia.home.constant.AtfKey.TYPE_ICON
import com.tokopedia.home.constant.AtfKey.TYPE_TICKER
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ANNOUNCEMENT
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ERROR
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_INFORMATION
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.user.session.UserSessionInterface

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

        private const val LAYOUT_FLOATING = "floating"
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
        bannerViewModel.createdTimeMillis = bannerDataModel.timestamp

        visitableList.add(bannerViewModel)
        return this
    }

    override fun addHomeHeaderOvo(): HomeVisitableFactory {
        val needToShowUserWallet = homeData?.homeFlag?.getFlag(HomeFlag.TYPE.HAS_TOKOPOINTS)?: false

        val homeHeader = HomeHeaderOvoDataModel(needToShowUserWallet = needToShowUserWallet)
        val headerViewModel = HeaderDataModel()
        headerViewModel.isUserLogin = userSessionInterface?.isLoggedIn?:false
        homeHeader.headerDataModel = headerViewModel
        visitableList.add(homeHeader)
        return this
    }

    override fun addTickerVisitable(): HomeVisitableFactory {
        addTickerData()
        return this
    }

    private fun addTickerData(defaultTicker: Ticker? = null) {
        if (!isCache) {
            if (defaultTicker != null) {
                defaultTicker.tickers.let { ticker ->
                    if (!HomeRevampFragment.HIDE_TICKER) {
                        ticker.filter { it.layout != LAYOUT_FLOATING }.let {
                            if (it.isNotEmpty()) {
                                visitableList.add(TickerDataModel(tickers = mappingTickerFromServer(it)))
                            }
                        }
                    }
                }
            } else {
                homeData?.ticker?.tickers?.let { ticker ->
                    if (!HomeRevampFragment.HIDE_TICKER) {
                        ticker.filter { it.layout != LAYOUT_FLOATING }.let {
                            if (it.isNotEmpty()) {
                                visitableList.add(TickerDataModel(tickers = mappingTickerFromServer(it)))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addDynamicIconData(defaultIconList: List<DynamicHomeIcon.DynamicIcon> = listOf()) {
        var isDynamicIconWrapType = homeData?.homeFlag?.getFlag(HomeFlag.TYPE.DYNAMIC_ICON_WRAP)?: false
        var iconList = defaultIconList
        if (iconList.isEmpty()) {
            iconList = homeData?.dynamicHomeIcon?.dynamicIcon?: listOf()
        }

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
    }

    private fun addDynamicIconData(id: String = "", type: Int = 1, defaultIconList: List<DynamicHomeIcon.DynamicIcon> = listOf()) {
        val viewModelDynamicIcon = DynamicIconComponentDataModel(
                id = id,
                dynamicIconComponent = DynamicIconComponent(
                        defaultIconList.map {
                            DynamicIconComponent.DynamicIcon(
                                    id = it.id,
                                    applink = it.applinks,
                                    imageUrl = it.imageUrl,
                                    name = it.name,
                                    url = it.url,
                                    businessUnitIdentifier = it.bu_identifier,
                                    galaxyAttribution = it.galaxyAttribution,
                                    persona = it.persona,
                                    brandId = it.brandId,
                                    categoryPersona = it.categoryPersona,
                                    campaignCode = it.campaignCode,
                                    withBackground = it.withBackground
                            )
                        }
                ),
                isCache = isCache,
                type = type
        )

        visitableList.add(viewModelDynamicIcon)
    }

    private fun addDynamicChannelData(addLoadingMore: Boolean, defaultDynamicHomeChannel: DynamicHomeChannel? = null, useDefaultWhenEmpty: Boolean = true, startPosition: Int = 0) {
        if (defaultDynamicHomeChannel != null) {
            defaultDynamicHomeChannel?.let {
                val data = dynamicChannelDataMapper?.mapToDynamicChannelDataModel(
                        HomeChannelData(it), isCache, addLoadingMore, useDefaultWhenEmpty, startPosition = startPosition)
                data?.let { it1 -> visitableList.addAll(it1) }
            }
        } else {
            homeData?.let {
                val data = dynamicChannelDataMapper?.mapToDynamicChannelDataModel(
                        HomeChannelData(it.dynamicHomeChannel), isCache, addLoadingMore, useDefaultWhenEmpty, startPosition = startPosition)
                data?.let { it1 -> visitableList.addAll(it1) }
            }
        }
    }

    private fun AtfData.atfStatusCondition(
            onLoading: ()-> Unit = {},
            onError: ()-> Unit = {},
            onSuccess: ()-> Unit = {}
    ) {
        when(status) {
            AtfKey.STATUS_LOADING -> if (!isOptional) {
                onLoading.invoke()
            }
            AtfKey.STATUS_ERROR -> if (!isOptional) {
                onError.invoke()
            }
            AtfKey.STATUS_SUCCESS -> onSuccess.invoke()
        }
    }

    private fun mappingTickerFromServer(it: List<Tickers>): List<Tickers> {
        return it.map { ticker ->
            ticker.tickerType = mapTickerType(ticker)
            ticker
        }
    }

    private fun mapTickerType(ticker: Tickers): Int = when(ticker.tickerType) {
        0 -> TYPE_ANNOUNCEMENT
        1 -> TYPE_INFORMATION
        2 -> TYPE_WARNING
        3 -> TYPE_ERROR
        else -> TYPE_ANNOUNCEMENT
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

    override fun addDynamicIconVisitable(): HomeVisitableFactory {
        addDynamicIconData()
        return this
    }

    override fun addAtfComponentVisitable(isProcessingAtf: Boolean): HomeVisitableFactory {
        if (homeData?.atfData?.dataList?.isNotEmpty() == true) {
            homeData?.atfData?.let {
                var channelPosition = 0
                var tickerPosition = 0
                var iconPosition = 0

                it.dataList.forEachIndexed { index, data ->
                    when(data.component) {
                        TYPE_ICON -> {
                            data.atfStatusCondition (
                                    onLoading = {
                                        visitableList.add(ShimmeringIconDataModel(data.id.toString()))
                                    },
                                    onError = {
                                        visitableList.add(ErrorStateIconModel())
                                    },
                                    onSuccess = {
                                        val icon = data.getAtfContent<DynamicHomeIcon>()
                                        addDynamicIconData(data.id.toString(),icon?.type ?: 1, icon?.dynamicIcon?: listOf())
                                    }
                            )
                            iconPosition++
                        }

                        TYPE_BANNER -> {

                        }

                        TYPE_TICKER -> {
                            data.atfStatusCondition (
                                    onSuccess = {
                                        addTickerData(data.getAtfContent<Ticker>())
                                    }
                            )
                            tickerPosition++
                        }

                        TYPE_CHANNEL -> {
                            data.atfStatusCondition (
                                    onLoading = {
                                        visitableList.add(ShimmeringChannelDataModel(data.id.toString()))
                                    },
                                    onError = {
                                        when(channelPosition) {
                                            0 -> visitableList.add(ErrorStateChannelOneModel())
                                            1 -> visitableList.add(ErrorStateChannelTwoModel())
                                            2 -> visitableList.add(ErrorStateChannelThreeModel())
                                        }
                                    },
                                    onSuccess = {
                                        addDynamicChannelData(
                                                false,
                                                data.getAtfContent<DynamicHomeChannel>(),
                                                false,
                                                index
                                        )
                                    }
                            )
                            channelPosition++
                        }
                    }
                }
            }
        } else if(isProcessingAtf)  {
            visitableList.add(HomeInitialShimmerDataModel())
        }

        if (homeData?.atfData == null) {
            visitableList.add(ErrorStateAtfModel())
        }
        return this
    }

    override fun addDynamicChannelVisitable(addLoadingMore: Boolean, useDefaultWhenEmpty: Boolean): HomeVisitableFactory {
        addDynamicChannelData(addLoadingMore = addLoadingMore, useDefaultWhenEmpty = useDefaultWhenEmpty, startPosition = homeData?.atfData?.dataList?.size ?: 0)
        return this
    }

    override fun build(): List<Visitable<*>> = visitableList
}