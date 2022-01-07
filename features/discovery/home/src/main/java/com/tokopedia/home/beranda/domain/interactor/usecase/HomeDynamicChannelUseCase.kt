package com.tokopedia.home.beranda.domain.interactor.usecase

import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper
import com.tokopedia.home.beranda.data.model.*
import com.tokopedia.home.beranda.data.repository.HomeRevampRepository
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.usecase.featuredshop.DisplayHeadlineAdsEntity
import com.tokopedia.home_component.usecase.featuredshop.mappingTopAdsHeaderToChannelGrid
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HomeDynamicChannelUseCase @Inject constructor(
        private val homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase,
        private val homeDataMapper: HomeDataMapper,
        private val homeDynamicChannelsRepository: HomeDynamicChannelsRepository,
        private val homeDataRepository: HomeDataRepository,
        private val atfDataRepository: HomeAtfRepository,
        private val homeFlagRepository: HomeFlagRepository,
        private val homePageBannerRepository: HomePageBannerRepository,
        private val homeIconRepository: HomeIconRepository,
        private val homeTickerRepository: HomeTickerRepository,
        private val getHomeRoomDataSource: HomeRoomDataSource,
        private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper,
        private val applicationContext: Context?,
        private val remoteConfig: RemoteConfig,
        private val homePlayRepository: HomePlayRepository,
        private val homeReviewSuggestedRepository: HomeReviewSuggestedRepository,
        private val homePlayLiveDynamicRepository: HomePlayLiveDynamicRepository,
        private val homePopularKeywordRepository: HomePopularKeywordRepository,
        private val homeHeadlineAdsRepository: HomeHeadlineAdsRepository,
        private val homeRecommendationRepository: HomeRecommendationRepository,
        private val homeRecommendationChipRepository: HomeRecommendationChipRepository,
        private val bestSellerMapper: BestSellerMapper,
        private val homeTopadsImageRepository: HomeTopadsImageRepository,
        private val homeRechargeRecommendationRepository: HomeRechargeRecommendationRepository,
        private val homeSalamWidgetRepository: HomeSalamWidgetRepository,
        private val homeRecommendationFeedTabRepository: HomeRecommendationFeedTabRepository,
        private val homeChooseAddressRepository: HomeChooseAddressRepository
): HomeRevampRepository {

    private var CHANNEL_LIMIT_FOR_PAGINATION = 1
    companion object{
        private const val TYPE_ATF_1 = "atf-1"
    }
    val gson = Gson()
    var cachedHomeData: HomeData? = null

    private val jobList = mutableListOf<Deferred<AtfData>>()

    fun updateHeaderData(homeHeaderDataModel: HomeHeaderDataModel, homeDataModel: HomeDynamicChannelModel): Visitable<*>? {
        val homeHeaderOvoDataModel = (homeDataModel.list.find { visitable-> visitable is HomeHeaderDataModel } as HomeHeaderDataModel?)
        homeHeaderOvoDataModel?.let {
            val currentPosition = -1
            val index = homeDataModel.list.withIndex().find { (_, model) ->  model is HomeHeaderDataModel }?.index ?: -1
            val visitable = homeDataModel.list.get(index)
            return visitable
        }
        return null
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getHomeDataFlow(): Flow<HomeDynamicChannelModel?> {
        var isCache = true

        val homeAtfCacheFlow = getHomeRoomDataSource.getCachedAtfData().map {
            val defaultFlag = HomeFlag()
            defaultFlag.addFlag(HomeFlag.HAS_TOKOPOINTS_STRING, true)
            val dynamicChannelPlainResponse = homeDataMapper.mapToHomeRevampViewModel(HomeData(
                    atfData = HomeAtfData(
                            dataList = it.map {
                                AtfData(
                                        id = it.id,
                                        name = it.name,
                                        component = it.component,
                                        param = it.param,
                                        isOptional = it.isOptional,
                                        content = it.content,
                                        status = it.status
                                )
                            },
                            isProcessingAtf = true
                    ),
                    isProcessingDynamicChannel = true, homeFlag = defaultFlag),
                    isCache = true,
                    addShimmeringChannel = true,
                    isLoadingAtf = true
            )
            dynamicChannelPlainResponse
        }.transform {
            emit(it)
        }

        val homeDynamicChannelFlow = getHomeRoomDataSource.getCachedHomeData().flatMapConcat {
            flow {
                val dynamicChannelPlainResponse = homeDataMapper.mapToHomeRevampViewModel(
                        homeData = it,
                        isCache = isCache
                )

                if (isCache) {
                    /**
                     * Emit cache data
                     */
                    emit(dynamicChannelPlainResponse)
                }

                /**
                 * Get choose address data
                 */
                applicationContext?.let {
                    val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(applicationContext)
                    dynamicChannelPlainResponse.setAndEvaluateHomeChooseAddressData(
                            HomeChooseAddressData(isActive = true)
                                    .setLocalCacheModel(localCacheModel)
                    )
                }
                /**
                 * Get header data
                 */
                val currentHeaderDataModel = homeBalanceWidgetUseCase.onGetBalanceWidgetData(HomeHeaderDataModel())
                updateHeaderData(currentHeaderDataModel, dynamicChannelPlainResponse)

                emit(dynamicChannelPlainResponse)

                /**
                 * Get Dynamic channel external data
                 */

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        CarouselPlayWidgetDataModel,
                        PlayWidgetUiModel>(widgetRepository = homePlayRepository) { visitableFound, data, position ->
                    visitableFound.copy(widgetUiModel = data)
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        ReviewDataModel,
                        SuggestedProductReview>(widgetRepository = homeReviewSuggestedRepository) { visitableFound, data, position ->
                    visitableFound.copy(suggestedProductReview = data)
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        PlayCardDataModel,
                        PlayData>(widgetRepository = homePlayLiveDynamicRepository) { visitableFound, data, position ->
                    visitableFound.copy(playCardHome = data.playChannels.first())
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        PlayCardDataModel,
                        PlayData>(widgetRepository = homePlayLiveDynamicRepository) { visitableFound, data, position ->
                    visitableFound.copy(playCardHome = data.playChannels.first())
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        PopularKeywordListDataModel,
                        HomeWidget.PopularKeywordQuery>(widgetRepository = homePopularKeywordRepository) { visitableFound, data, position ->
                    val resultList = convertPopularKeywordDataList(data.data)

                    visitableFound.copy(
                            title = data.data.title,
                            subTitle = data.data.subTitle,
                            popularKeywordList = resultList,
                            isErrorLoad = false
                    )
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        FeaturedShopDataModel,
                        List<DisplayHeadlineAdsEntity.DisplayHeadlineAds>>(
                        widgetRepository = homeHeadlineAdsRepository,
                        bundleParam = {
                            Bundle().apply {
                                putString(HomeHeadlineAdsRepository.WIDGET_PARAM, it.channelModel.widgetParam)
                            }
                        },
                        deleteWidgetWhen = {
                            it?.isEmpty() == true
                        }
                ) { visitableFound, data, position ->
                    visitableFound.copy(
                            channelModel = visitableFound.channelModel.copy(
                                    channelGrids = data.mappingTopAdsHeaderToChannelGrid()
                            ),
                            state = FeaturedShopDataModel.STATE_READY)
                }

                getRecommendationWidget(dynamicChannelPlainResponse)

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        HomeTopAdsBannerDataModel,
                        ArrayList<TopAdsImageViewModel>>(
                        widgetRepository = homeTopadsImageRepository,
                        deleteWidgetWhen = {
                            it?.isEmpty() == true
                        }
                ) { visitableFound, data, position ->
                    var newTopAdsModel = visitableFound.copy()
                    if (data.isNotEmpty()) {
                        newTopAdsModel = visitableFound.copy(topAdsImageViewModel = data[0])
                    }
                    dynamicChannelPlainResponse.topadsNextPageToken = newTopAdsModel.topAdsImageViewModel?.nextPageToken?:""
                    newTopAdsModel
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        ReminderWidgetModel,
                        RechargeRecommendation>(
                        widgetRepository = homeRechargeRecommendationRepository,
                        predicate = {
                            it?.source == ReminderEnum.RECHARGE
                        }
                ) { visitableFound, data, position ->
                    val newFindRechargeRecommendationViewModel = visitableFound.copy(
                            data = ReminderWidgetMapper.mapperRechargetoReminder(data),
                            source = ReminderEnum.RECHARGE
                    )
                    newFindRechargeRecommendationViewModel
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                        ReminderWidgetModel,
                        SalamWidget>(
                        widgetRepository = homeSalamWidgetRepository,
                        predicate = {
                            it?.source == ReminderEnum.SALAM
                        }
                ) { visitableFound, data, position ->
                    val newFindRechargeRecommendationViewModel = visitableFound.copy(
                            data = ReminderWidgetMapper.mapperSalamtoReminder(data),
                            source = ReminderEnum.SALAM
                    )
                    newFindRechargeRecommendationViewModel
                }

                emit(dynamicChannelPlainResponse.copy(
                        isCache = false
                ))

                if (!dynamicChannelPlainResponse.isProcessingDynamicChannle) {
                    dynamicChannelPlainResponse.evaluateRecommendationSection(
                            onNeedTabLoad = { getFeedTabData(dynamicChannelPlainResponse) }
                    )
                }

                emit(dynamicChannelPlainResponse.copy(
                        isCache = false
                ))
            }
        }

        return combineTransform(homeAtfCacheFlow, homeDynamicChannelFlow) { atfDC, fullDc ->
            emit(atfDC)
            emit(fullDc)
        }.onEach {
            isCache = false
        }
    }

    private inline fun <reified T> widgetIsAvailable(homeDataModel: HomeDynamicChannelModel, predicate: (T) -> Boolean = {true}): Boolean {
        homeDataModel.list.filterIsInstance<T>().let {
            return it.find { predicate.invoke(it) } != null
        }
    }

    suspend fun getFeedTabData(homeDataModel: HomeDynamicChannelModel) {
        if (!widgetIsAvailable<HomeRecommendationFeedDataModel>(homeDataModel)) {
            homeDataModel.addWidgetModel(HomeLoadingMoreModel())
        }
        try {
            val homeRecommendationTabs = homeRecommendationFeedTabRepository.getRemoteData(
                    Bundle().apply {
                        putString(
                                HomeRecommendationFeedTabRepository.LOCATION_PARAM,
                                homeChooseAddressRepository.getRemoteData()?.convertToLocationParams()
                        )
                    }
            )
            val findRetryModel = homeDataModel.list.withIndex().find { data -> data.value is HomeRetryModel
            }

            val findLoadingModel = homeDataModel.list.withIndex().find {
                data -> data.value is HomeLoadingMoreModel
            }

            homeDataModel.list.withIndex().find {
                it.value is HomeRecommendationFeedDataModel ||
                        it.value is HomeLoadingMoreModel ||
                        it.value is HomeRetryModel
            }.let {
                it?.let {
                    when {
                        findLoadingModel != null -> {
                            homeDataModel.updateWidgetModel(
                                    visitable = HomeRecommendationFeedDataModel(
                                            recommendationTabDataModel = homeRecommendationTabs,
                                            homeChooseAddressData = homeDataModel.homeChooseAddressData.copy()
                                    ),
                                    visitableToChange = findLoadingModel.value,
                                    position = it.index) {}
                        }
                        findRetryModel != null -> {
                            homeDataModel.updateWidgetModel(
                                    visitable = HomeRecommendationFeedDataModel(
                                            recommendationTabDataModel = homeRecommendationTabs,
                                            homeChooseAddressData = homeDataModel.homeChooseAddressData.copy()
                                    ),
                                    visitableToChange = findRetryModel.value,
                                    position = it.index) {}
                        }
                        else -> {
                            (it.value as? HomeRecommendationFeedDataModel)?.let { recomModel ->
                                val newModel = recomModel.copy(
                                        recommendationTabDataModel = homeRecommendationTabs,
                                        homeChooseAddressData = homeDataModel.homeChooseAddressData.copy()
                                )
                                newModel.recommendationTabDataModel = homeRecommendationTabs
                                newModel.isNewData = true
                                homeDataModel.updateWidgetModel(visitable = recomModel, visitableToChange = newModel, position = it.index) {}
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            val findRetryModel = homeDataModel.list.withIndex().find { data -> data.value is HomeRetryModel
            }
            val findLoadingModel = homeDataModel.list.withIndex().find { data -> data.value is HomeLoadingMoreModel
            }
            homeDataModel.addWidgetModel(HomeRetryModel())
            homeDataModel.deleteWidgetModel(findLoadingModel?.value, findLoadingModel?.index ?: -1) {}
            homeDataModel.deleteWidgetModel(findRetryModel?.value, findRetryModel?.index ?: -1) {}
        }
    }

    suspend fun getRecommendationWidget(homeDataModel: HomeDynamicChannelModel){
        findWidget<BestSellerDataModel>(homeDataModel) { bestSellerDataModel, index ->
            val recomFilterList = mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()

            val recommendationChip = homeRecommendationChipRepository.getRemoteData(
                    Bundle().apply {
                        putString(HomeRecommendationChipRepository.PAGE_NAME, bestSellerDataModel.pageName)
                        putString(HomeRecommendationChipRepository.QUERY_PARAM, bestSellerDataModel.widgetParam)
                    }
            )
            recomFilterList.addAll(recommendationChip)
            val activatedChip = recomFilterList.find { it.isActivated }
            val recomData = if (activatedChip == null) {
                homeRecommendationRepository.getRemoteData(
                        Bundle().apply {
                            putString(HomeRecommendationChipRepository.PAGE_NAME, bestSellerDataModel.pageName)
                            putString(HomeRecommendationChipRepository.QUERY_PARAM, bestSellerDataModel.widgetParam)
                        }
                )
            } else {
                homeRecommendationRepository.getRemoteData(
                        Bundle().apply {
                            putString(HomeRecommendationChipRepository.PAGE_NAME, bestSellerDataModel.pageName)
                            putString(HomeRecommendationChipRepository.QUERY_PARAM, if(activatedChip.isActivated) activatedChip.value else "")
                        }
                )
            }

            if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                val recomWidget = recomData.first().copy(
                        recommendationFilterChips = recomFilterList
                )
                val dataModel = bestSellerMapper.mappingRecommendationWidget(recomWidget)

                homeDataModel.updateWidgetModel(
                        visitable = dataModel.copy(
                                id = bestSellerDataModel.id,
                                pageName = dataModel.pageName,
                                widgetParam = bestSellerDataModel.widgetParam,
                                dividerType = bestSellerDataModel.dividerType
                        ),
                        visitableToChange = bestSellerDataModel,
                        position = index
                ) {}
            } else {
                homeDataModel.deleteWidgetModel(bestSellerDataModel, index) {}
            }
        }
    }

    private fun convertPopularKeywordDataList(popularKeywordList: HomeWidget.PopularKeywordList): MutableList<PopularKeywordDataModel> {
        val keywordList = popularKeywordList.keywords
        val dataList: MutableList<PopularKeywordDataModel> = mutableListOf()
        for (pojo in keywordList) {
            dataList.add(
                    PopularKeywordDataModel(
                            recommendationType = popularKeywordList.recommendationType,
                            applink = pojo.url,
                            imageUrl = pojo.imageUrl,
                            title = pojo.keyword,
                            productCount = pojo.productCount)
            )
        }
        return dataList
    }

    private suspend inline fun <reified T: Visitable<*>, reified K> HomeDynamicChannelModel.getWidgetDataIfExist(
            bundleParam: (T) -> Bundle = { Bundle() },
            widgetRepository: HomeRepository<K>,
            predicate: (T?) -> Boolean = {true},
            deleteWidgetWhen:(K?) -> Boolean = {false},
            mapToWidgetData: (T, K, Int) -> T
    ): HomeDynamicChannelModel {
        findWidget<T>(this, predicate) { visitableFound, visitablePosition ->
            val data = widgetRepository.getRemoteData(bundleParam.invoke(visitableFound))
            if (!deleteWidgetWhen.invoke(data)) {
                this.updateWidgetModel(
                        visitable = mapToWidgetData.invoke(visitableFound, data, visitablePosition),
                        visitableToChange = visitableFound,
                        position = visitablePosition
                ) {}
            } else {
                this.deleteWidgetModel(
                        visitable = visitableFound,
                        position = visitablePosition
                ) {}
            }
        }
        return this
    }

    private inline fun <reified T> findWidget(
            homeDataModel: HomeDynamicChannelModel,
            predicate: (T?) -> Boolean = {true},
            actionOnFound: (T, Int) -> Unit) {
        homeDataModel.list.withIndex().find { it.value is T && predicate.invoke(it.value as? T) }.let {
            it?.let {
                if (it.value is T) {
                    actionOnFound.invoke(it.value as T, it. index)
                }
            }
        }
    }

    /**
     * Home repository flow:
     *
     * 1. Provide initial HomeData
     * 2. Get above the fold skeleton
     *    2.1 Get home flag response
     * 3. Save immediately to produce shimmering for ATF data
     * 4. Get above the fold content
     * 5. Submit current data to database, to trigger HomeViewModel flow
     *      if there is no cache, then submit immediately
     *      if cache exist, don't submit to database because it will trigger jumpy experience
     * 6. Get dynamic channel data
     *    6.1. If channel cache is empty, proceed to channel pagination
     *    6.2. If channel cache is not empty, proceed to full channel request
     *      if there is token and cache is not exist
     *      if cache is exist
     * 7. Submit current data to database, to trigger HomeViewModel flow
     *    7.1 Emit error pagination only when atf is empty
     *      Because there is no content that we can show, we showing error page
     */
    override fun updateHomeData(): Flow<Result<Any>> = flow{
        coroutineScope {

            /**
             * Remote config to disable pagination by request with param 0
             */
            if (!remoteConfig.getBoolean(RemoteConfigKey.HOME_ENABLE_PAGINATION)) {
                CHANNEL_LIMIT_FOR_PAGINATION = 0
            }

            val isCacheExistForProcess = getHomeRoomDataSource.getCachedHomeData().first() != null
            val currentTimeMillisString = System.currentTimeMillis().toString()
            var currentToken = ""
            var isAtfSuccess = true

            /**
             * 1. Provide initial HomeData
             */
            var homeData = HomeData()
            cacheCondition(isCache = isCacheExistForProcess, isCacheEmptyAction = {
                homeData.isProcessingDynamicChannel = true
            }, isCacheExistAction = {
                homeData.isProcessingDynamicChannel = false
            })

            /**
             * 2. Get above the fold skeleton
             */
            try {
                val homeAtfResponse = atfDataRepository.getRemoteData()
                if (homeAtfResponse.dataList.isEmpty()) {
                    isAtfSuccess = false
                    homeData.atfData = null
                } else {
                    homeData.atfData = homeAtfResponse
                }
            } catch (e: Exception) {
                homeData.atfData = cachedHomeData?.atfData
                isAtfSuccess = false
                emit(Result.errorAtf(error = e, data = null))
            }

            /**
             * 2.1 Get home flag response
             */
            try {
                launch {
                    val homeFlagResponse = homeFlagRepository.getRemoteData()
                    homeFlagResponse.homeFlag.let {
                        homeData.homeFlag = homeFlagResponse.homeFlag
                    }
                }
            } catch (e: Exception) {

            }

            /**
             * 4. Get above the fold content
             */
            if (homeData.atfData?.dataList?.isNotEmpty() == true) {
                var nonTickerResponseFinished = false

                homeData.atfData?.dataList?.map { atfData ->
                    when(atfData.component) {
                        AtfKey.TYPE_TICKER -> {
                            val job = async {
                                try {
                                    val ticker = homeTickerRepository.getRemoteData(
                                            Bundle().apply { putString(HomeTickerRepository.Companion.PARAM_LOCATION,
                                                    applicationContext?.let {
                                                        ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: "") }
                                    )
                                    ticker?.let {
                                        atfData.content = gson.toJson(ticker.ticker)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.errorString = ErrorHandler.getErrorMessage(applicationContext, MessageErrorException(e.localizedMessage))
                                }
                                if (nonTickerResponseFinished) {
                                    cacheCondition(isCacheExistForProcess, isCacheEmptyAction = {
                                        saveToDatabase(homeData)
                                    })
                                }
                                atfData
                            }
                            jobList.add(job)
                        }
                        AtfKey.TYPE_BANNER -> {
                            val job = async {
                                try {
                                    val dynamicChannel = homePageBannerRepository.getRemoteData()
                                    dynamicChannel.let {
                                        val channelFromResponse = it.banner
                                        atfData.content = gson.toJson(channelFromResponse)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                    homeData.atfData?.isProcessingAtf = false
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.content = null
                                    atfData.errorString = ErrorHandler.getErrorMessage(applicationContext, MessageErrorException(e.localizedMessage))
                                }
                                cacheCondition(isCacheExistForProcess, isCacheEmptyAction = {
                                    saveToDatabase(homeData)
                                })
                                nonTickerResponseFinished = true
                                atfData
                            }
                            jobList.add(job)
                        }
                        AtfKey.TYPE_CHANNEL -> {
                            val job = async {
                                try {
                                    val dynamicChannel = homeDynamicChannelsRepository.getRemoteData(
                                            Bundle().apply {
                                                putString(
                                                        HomeDynamicChannelsRepository.PARAMS, atfData.param
                                                )
                                                putString(
                                                        HomeDynamicChannelsRepository.LOCATION, applicationContext?.let {
                                                            ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: ""
                                                )
                                            }
                                    )
                                    dynamicChannel.let {
                                        val channelFromResponse = it.dynamicHomeChannel
                                        atfData.content = gson.toJson(channelFromResponse)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                    homeData.atfData?.isProcessingAtf = false
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.content = null
                                    atfData.errorString = ErrorHandler.getErrorMessage(applicationContext, MessageErrorException(e.localizedMessage))
                                }
                                cacheCondition(isCacheExistForProcess, isCacheEmptyAction = {
                                    saveToDatabase(homeData)
                                })
                                nonTickerResponseFinished = true
                                atfData
                            }
                            jobList.add(job)
                        }
                        AtfKey.TYPE_ICON -> {
                            val job = async {
                                try {
                                    val dynamicIcon = homeIconRepository.getRemoteData(
                                            Bundle().apply {
                                                putString(
                                                        HomeDynamicChannelsRepository.PARAMS, atfData.param
                                                )
                                                putString(
                                                        HomeDynamicChannelsRepository.LOCATION, applicationContext?.let {
                                                    ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: ""
                                                )
                                            }
                                    )
                                    dynamicIcon.let {
                                        atfData.content = gson.toJson(dynamicIcon.dynamicHomeIcon.copy(type=if(atfData.param.contains(TYPE_ATF_1)) 1 else 2))
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                    homeData.atfData?.isProcessingAtf = false
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.errorString = ErrorHandler.getErrorMessage(applicationContext, MessageErrorException(e.localizedMessage))
                                }
                                cacheCondition(isCacheExistForProcess, isCacheEmptyAction = {
                                    saveToDatabase(homeData)
                                })
                                nonTickerResponseFinished = true
                                atfData
                            }
                            jobList.add(job)
                        }
                        else -> {

                        }
                    }
                }
            }

            /**
             * 6. Get dynamic channel data
             */
            val dynamicChannelResponseValue = try {
                val dynamicChannelResponse = homeDynamicChannelsRepository.getRemoteData(
                        Bundle().apply {
                            putInt(
                                    HomeDynamicChannelsRepository.NUM_OF_CHANNEL, CHANNEL_LIMIT_FOR_PAGINATION
                            )
                            putString(
                                    HomeDynamicChannelsRepository.LOCATION, applicationContext?.let {
                                ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: ""
                            )
                        }
                )
                dynamicChannelResponse
            } catch (e: Exception) {
                if (!isAtfSuccess && !isCacheExistForProcess) {
                    null
                } else {
                    HomeChannelData()
                }
            }

            /**
             * 6.1. If channel cache is empty, proceed to channel pagination
             */
            if (!isCacheExistForProcess && dynamicChannelResponseValue != null) {
                val extractPair = extractToken(dynamicChannelResponseValue)

                homeData.let {
                    val combinedChannel = combineChannelWith(it.dynamicHomeChannel, extractPair.second.dynamicHomeChannel)
                    it.dynamicHomeChannel = combinedChannel
                    it.token = extractPair.first
                    it.dynamicHomeChannel.channels.forEach { channel ->
                        channel.timestamp = currentTimeMillisString
                    }
                    currentToken = it.token
                }

                homeData.isProcessingDynamicChannel = false
                if (isAtfSuccess) {
                    saveToDatabase(homeData, true)
                } else {
                    saveToDatabase(homeData, false)
                }
            } else if (dynamicChannelResponseValue == null) {

                /**
                 * 7.1 Emit error pagination only when atf is empty
                 * Because there is no content that we can show, we showing error page
                 */
                if (!isCacheExistForProcess &&
                        (homeData.atfData == null ||
                                (homeData.atfData?.dataList == null && homeData.atfData?.isProcessingAtf == false) ||
                                homeData.atfData?.dataList?.isEmpty() == true)) {
                    emit(Result.errorGeneral(Throwable(),null))
                } else {
                    emit(Result.error(Throwable(), null))
                }
                saveToDatabase(homeData)
            }

            /**
             * 6.2. If channel cache is not empty, proceed to full channel request
             * - if there is token and cache is not exist
             * - if cache is exist
             *
             */
            if ((!isCacheExistForProcess && currentToken.isNotEmpty()) ||
                    isCacheExistForProcess) {
                try {
                    homeData = processFullPageDynamicChannel(
                            homeDataResponse = homeData)
                            ?: HomeData()
                    homeData.dynamicHomeChannel.channels.forEach {
                        it.timestamp = currentTimeMillisString
                    }
                    homeData.let {
                        emit(Result.success(null))

                        /**
                         * 7. Submit current data to database, to trigger HomeViewModel flow
                         */
                        homeData.isProcessingDynamicChannel = false
                        if (isAtfSuccess) {
                            saveToDatabase(it, true)
                        } else {
                            saveToDatabase(it, false)
                        }
                    }
                } catch (e: Exception) {

                    /**
                     * 7.1 Emit error pagination only when atf is empty
                     * Because there is no content that we can show, we showing error page
                     */
                    if (homeData.atfData?.dataList == null || homeData.atfData?.dataList?.isEmpty() == true) {
                        emit(Result.errorPagination(error = MessageErrorException(e.localizedMessage), data = null))
                    }
                    cacheCondition(
                            isCacheExistForProcess,
                            isCacheEmptyAction = {
                                saveToDatabase(homeData)
                            }
                    )
                }
            }
        }
    }

    private suspend fun saveToDatabase(homeData: HomeData?, saveAtf: Boolean = false) {
        getHomeRoomDataSource.saveToDatabase(homeData)
        if (saveAtf) {
            homeData?.atfData?.let {
                getHomeRoomDataSource.saveCachedAtf(
                        it.dataList.filter {
                            it.component != "channel"
                        }.map {atfData ->
                            AtfCacheEntity(
                                    id = atfData.id,
                                    name = atfData.name,
                                    component = atfData.component,
                                    param = atfData.param,
                                    isOptional = atfData.isOptional,
                                    content = atfData.content,
                                    status = atfData.status
                            )
                        }
                )
            }
        }
    }

    override suspend fun onDynamicChannelExpired(groupId: String): List<Visitable<*>> {
        val dynamicChannelResponse = homeDynamicChannelsRepository.getRemoteData(
                Bundle().apply {
                    putString(
                            HomeDynamicChannelsRepository.GROUP_IDS, groupId
                    )
                    putString(
                            HomeDynamicChannelsRepository.LOCATION, applicationContext?.let {
                        ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: ""
                    )
                }
        )
        val homeChannelData = HomeChannelData(dynamicChannelResponse.dynamicHomeChannel)

        return homeDynamicChannelDataMapper.mapToDynamicChannelDataModel(
                homeChannelData,
                isCache = false,
                addLoadingMore = false,
                useDefaultWhenEmpty = false)
    }

    private suspend fun processFullPageDynamicChannel(homeDataResponse: HomeData?): HomeData? {
        val dynamicChannelCompleteResponse = homeDynamicChannelsRepository.getRemoteData(
                Bundle().apply {
                    putInt(
                            HomeDynamicChannelsRepository.NUM_OF_CHANNEL, 0
                    )
                    putString(
                            HomeDynamicChannelsRepository.TOKEN, homeDataResponse?.token?:""
                    )
                    putString(
                            HomeDynamicChannelsRepository.LOCATION, applicationContext?.let {
                        ChooseAddressUtils.getLocalizingAddressData(applicationContext)?.convertToLocationParams()} ?: ""
                    )
                }
        )
        val currentChannelList = homeDataResponse?.dynamicHomeChannel?.channels?.toMutableList()?: mutableListOf()
        currentChannelList.addAll(dynamicChannelCompleteResponse.dynamicHomeChannel.channels)

        homeDataResponse?.let {
            homeDataResponse.token = ""
            val combinedChannel = combineChannelWith(homeDataResponse.dynamicHomeChannel, dynamicChannelCompleteResponse.dynamicHomeChannel)
            homeDataResponse.dynamicHomeChannel = combinedChannel
        }
        return homeDataResponse
    }

    override fun deleteHomeData() {
        getHomeRoomDataSource.deleteHomeData()
    }

    private fun extractToken(homeChannelData: HomeChannelData): Pair<String, HomeChannelData> {
        return if (homeChannelData.dynamicHomeChannel.channels.isNotEmpty()) {
            val token = homeChannelData.dynamicHomeChannel.channels[0].token
            homeChannelData.dynamicHomeChannel.channels[0].token = ""
            Pair(token, homeChannelData)
        } else Pair("", homeChannelData)
    }

    private fun combineChannelWith(currentChannel: DynamicHomeChannel, newChannel: DynamicHomeChannel): DynamicHomeChannel {
        val combinationChannel = currentChannel.channels.toMutableList()
        combinationChannel.addAll(newChannel.channels)
        return DynamicHomeChannel(combinationChannel)
    }

    private suspend fun cacheCondition(
            isCache: Boolean,
            isCacheExistAction: suspend () -> Unit = {}, isCacheEmptyAction: suspend () -> Unit = {}) {
        if (isCache) {
            isCacheExistAction.invoke()
        } else {
            isCacheEmptyAction.invoke()
        }
    }
}