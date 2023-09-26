package com.tokopedia.home.beranda.domain.interactor.usecase

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.ReminderWidgetMapper
import com.tokopedia.home.beranda.data.mapper.ShopFlashSaleMapper
import com.tokopedia.home.beranda.data.model.*
import com.tokopedia.home.beranda.data.newatf.AtfMapper.mapToVisitableList
import com.tokopedia.home.beranda.data.newatf.HomeAtfUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.helper.LazyLoadDynamicChannelHelper
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.usecase.featuredshop.DisplayHeadlineAdsEntity
import com.tokopedia.home_component.usecase.featuredshop.mappingTopAdsHeaderToChannelGrid
import com.tokopedia.home_component.usecase.missionwidget.GetMissionWidget
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.GetTodoWidgetUseCase
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.tokopedia.home.beranda.data.mapper.BestSellerMapper as BestSellerRevampMapper
import com.tokopedia.home_component.visitable.BestSellerDataModel as BestSellerRevampDataModel

class HomeDynamicChannelUseCase @Inject constructor(
    private val homeBalanceWidgetUseCase: HomeBalanceWidgetUseCase,
    private val homeDataMapper: HomeDataMapper,
    private val bestSellerRevampMapper: BestSellerRevampMapper,
    private val homeDynamicChannelsRepository: HomeDynamicChannelsRepository,
    private val atfDataRepository: HomeAtfRepository,
    private val homeUserStatusRepository: HomeUserStatusRepository,
    private val homePageBannerRepository: HomePageBannerRepository,
    private val homeIconRepository: HomeIconRepository,
    private val homeTickerRepository: HomeTickerRepository,
    private val getHomeRoomDataSource: HomeRoomDataSource,
    private val homeDynamicChannelDataMapper: HomeDynamicChannelDataMapper,
    private val applicationContext: Context?,
    private val remoteConfig: RemoteConfig,
    private val homePlayRepository: HomePlayRepository,
    private val homeReviewSuggestedRepository: HomeReviewSuggestedRepository,
    private val homePopularKeywordRepository: HomePopularKeywordRepository,
    private val homeHeadlineAdsRepository: HomeHeadlineAdsRepository,
    private val homeRecommendationRepository: HomeRecommendationRepository,
    private val homeRecommendationChipRepository: HomeRecommendationChipRepository,
    private val bestSellerMapper: BestSellerMapper,
    private val homeTopadsImageRepository: HomeTopadsImageRepository,
    private val homeRechargeRecommendationRepository: HomeRechargeRecommendationRepository,
    private val homeSalamWidgetRepository: HomeSalamWidgetRepository,
    private val homeRecommendationFeedTabRepository: HomeRecommendationFeedTabRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    private val userSessionInterface: UserSessionInterface,
    private val homeMissionWidgetRepository: HomeMissionWidgetRepository,
    private val homeTodoWidgetRepository: HomeTodoWidgetRepository,
    private val homeAtfUseCase: HomeAtfUseCase,
) {

    private var CHANNEL_LIMIT_FOR_PAGINATION = 1

    private var currentHeaderDataModel: HomeHeaderDataModel? = null
    private var previousHeaderDataModel: HomeHeaderDataModel? = null

    companion object {
        private const val TYPE_ATF_1 = "atf-1"
        private const val MINIMUM_BANNER_TO_SHOW = 1
        private const val MINIMUM_DC_TO_SHOW_RECOM = 3
        private const val DEFAULT_TOPADS_TDN_PAGE = "0"
    }

    val gson = Gson()
    var cachedHomeData: HomeData? = null

    var localHomeRecommendationFeedDataModel: HomeRecommendationFeedDataModel? = null
    private var topadsTdnPage = DEFAULT_TOPADS_TDN_PAGE

    private val jobList = mutableListOf<Deferred<AtfData>>()

    var isCache = true
    var isCacheDc = true

    fun updateHeaderData(
        homeHeaderDataModel: HomeHeaderDataModel,
        homeDataModel: HomeDynamicChannelModel
    ) {
        findWidget<HomeHeaderDataModel>(homeDataModel) { model, index ->
            homeDataModel.updateWidgetModel(
                visitable = homeHeaderDataModel,
                position = index
            ) {}
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getHomeDataFlow(): Flow<HomeDynamicChannelModel?> {
        isCache = true
        isCacheDc = true

        val homeAtfCacheFlow = getHomeRoomDataSource.getCachedAtfData().flatMapConcat {
            flow<HomeDynamicChannelModel> {
                if (isCache) {
                    val dynamicChannelPlainResponse = homeDataMapper.mapToHomeRevampViewModel(
                        HomeData(
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
                            isProcessingDynamicChannel = true,
                        ),
                        isCache = true,
                        addShimmeringChannel = true,
                        isLoadingAtf = true
                    )
                    dynamicChannelPlainResponse.apply {
                        Log.d("Each merge list size:", ("" + this.list.size))
                        this.isCache = isCache
                        this.flowCompleted = true
                    }
                    emit(dynamicChannelPlainResponse)
                }
            }
        }

        val homeDynamicChannelFlow = getHomeRoomDataSource.getCachedHomeData().flatMapConcat {
            getDynamicChannelFlow(it, false)
        }

        return merge(homeAtfCacheFlow, homeDynamicChannelFlow).onEach {
            isCache = false
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getNewHomeDataFlow(): Flow<HomeDynamicChannelModel?> {
        val headerVisitables = listOf(
            HomeHeaderDataModel(
                headerDataModel = HeaderDataModel(
                    isUserLogin = userSessionInterface.isLoggedIn
                )
            )
        )

        val atfFlow = homeAtfUseCase.flow.map {
            HomeDynamicChannelModel(
                list = it?.mapToVisitableList().orEmpty(),
                isCache = it?.isCache.orTrue()
            )
        }

        val dynamicChannelFlow = getHomeRoomDataSource.getCachedHomeData().flatMapConcat {
            getDynamicChannelFlow(it, true)
        }

        return combine(atfFlow, dynamicChannelFlow) { atf, dc ->
            val atfVisitables = atf.list
            val dcVisitables = dc.list
            val combinedList = headerVisitables + atfVisitables + dcVisitables
            dc.copy(list = combinedList)
        }
    }

    private fun getDynamicChannelFlow(
        homeData: HomeData?,
        isNewMechanism: Boolean,
    ): Flow<HomeDynamicChannelModel> {
        return flow<HomeDynamicChannelModel> {
            topadsTdnPage = DEFAULT_TOPADS_TDN_PAGE

            val dynamicChannelPlainResponse = if(isNewMechanism)
                homeDataMapper.mapDynamicChannel(
                    homeData = homeData,
                    isCache = isCacheDc
                )
            else
                homeDataMapper.mapToHomeRevampViewModel(
                    homeData = homeData,
                    isCache = isCacheDc
                )

            /**
             * Get choose address data
             */
            applicationContext?.let {
                val localCacheModel =
                    ChooseAddressUtils.getLocalizingAddressData(applicationContext)
                dynamicChannelPlainResponse.setAndEvaluateHomeChooseAddressData(
                    HomeChooseAddressData(isActive = true)
                        .setLocalCacheModel(localCacheModel)
                )
            }

            if (userSessionInterface.isLoggedIn) {
                /**
                 * Get header data
                 */
                if (currentHeaderDataModel == null) {
                    currentHeaderDataModel =
                        homeBalanceWidgetUseCase.onGetBalanceWidgetData(
                            previousHeaderDataModel
                        )
                    previousHeaderDataModel = currentHeaderDataModel
                }
                currentHeaderDataModel?.let {
                    updateHeaderData(it, dynamicChannelPlainResponse)
                    emit(dynamicChannelPlainResponse)
                }
            }

            if (isCacheDc) {
                /**
                 * Emit cache data
                 */
                emit(dynamicChannelPlainResponse)
            } else {
                /**
                 * Get Dynamic channel external data
                 * only on non cache
                 */

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                    CarouselPlayWidgetDataModel,
                    PlayWidgetState>(
                    bundleParam = {
                        Bundle().apply {
                            putString(
                                HomePlayRepository.KEY_WIDGET_LAYOUT,
                                it.homeChannel.layout
                            )
                        }
                    },
                    deleteWidgetWhen = {
                        it?.model?.items?.isEmpty() == true
                    },
                    widgetRepository = homePlayRepository
                ) { visitableFound, data, position ->
                    visitableFound.copy(widgetState = data)
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                    ReviewDataModel,
                    SuggestedProductReview>(widgetRepository = homeReviewSuggestedRepository) { visitableFound, data, position ->
                    visitableFound.copy(suggestedProductReview = data)
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

                dynamicChannelPlainResponse.getWidgetDataIfExistHandleError<
                    MissionWidgetListDataModel,
                    HomeMissionWidgetData.HomeMissionWidget>(
                    widgetRepository = homeMissionWidgetRepository,
                    bundleParam = {
                        Bundle().apply {
                            putString(
                                GetMissionWidget.BANNER_LOCATION_PARAM,
                                homeChooseAddressRepository.getRemoteData()
                                    ?.convertToLocationParams()
                            )
                        }
                    },
                    handleOnFailed = { visitableFound ->
                        visitableFound.copy(status = MissionWidgetListDataModel.STATUS_ERROR)
                    },
                    mapToWidgetData = { visitableFound, data, _ ->
                        val resultList =
                            LazyLoadDynamicChannelHelper.convertMissionWidgetDataList(data.getHomeMissionWidget.missions)
                        visitableFound.copy(
                            missionWidgetList = resultList,
                            status = MissionWidgetListDataModel.STATUS_SUCCESS
                        )
                    }
                )

                dynamicChannelPlainResponse.getWidgetDataIfExistHandleError<
                    TodoWidgetListDataModel,
                    HomeTodoWidgetData.HomeTodoWidget>(
                    widgetRepository = homeTodoWidgetRepository,
                    bundleParam = {
                        Bundle().apply {
                            putString(
                                GetTodoWidgetUseCase.LOCATION_PARAM,
                                homeChooseAddressRepository.getRemoteData()?.convertToLocationParams()
                            )
                            putString(
                                GetTodoWidgetUseCase.PARAM,
                                it.channelModel.widgetParam
                            )
                        }
                    },
                    handleOnFailed = { visitableFound ->
                        visitableFound.copy(status = TodoWidgetListDataModel.STATUS_ERROR)
                    },
                    mapToWidgetData = { visitableFound, data, _ ->
                        val resultList =
                            LazyLoadDynamicChannelHelper.convertTodoWidgetDataList(data.getHomeTodoWidget.todos)
                        visitableFound.copy(
                            todoWidgetList = resultList,
                            status = TodoWidgetListDataModel.STATUS_SUCCESS
                        )
                    }
                )

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                    FeaturedShopDataModel,
                    List<DisplayHeadlineAdsEntity.DisplayHeadlineAds>>(
                    widgetRepository = homeHeadlineAdsRepository,
                    bundleParam = {
                        Bundle().apply {
                            putString(
                                HomeHeadlineAdsRepository.WIDGET_PARAM,
                                it.channelModel.widgetParam
                            )
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
                        state = FeaturedShopDataModel.STATE_READY
                    )
                }

                getOldBestSellerData(dynamicChannelPlainResponse)

                getBestSellerRevampData(dynamicChannelPlainResponse)

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                    HomeTopAdsBannerDataModel,
                    ArrayList<TopAdsImageViewModel>>(
                    widgetRepository = homeTopadsImageRepository,
                    iterateList = true,
                    onWidgetExist = { size ->
                        val currentPage = topadsTdnPage
                        currentPage.toIntOrNull()?.let {
                            val nextPage = ((it + 1) + size)
                            dynamicChannelPlainResponse.topadsPage = nextPage.toString()
                        }
                        emit(dynamicChannelPlainResponse)
                    },
                    bundleParam = {
                        val currentPage = topadsTdnPage
                        currentPage.toIntOrNull()?.let {
                            val nextPage = (it + 1)
                            topadsTdnPage = nextPage.toString()
                        }
                        Bundle().apply {
                            putString(
                                HomeTopadsImageRepository.Companion.TOP_ADS_PAGE,
                                topadsTdnPage
                            )
                        }
                    },
                    deleteWidgetWhen = {
                        it?.isEmpty() == true
                    }
                ) { visitableFound, data, position ->
                    var newTopAdsModel = visitableFound.copy()
                    if (data.isNotEmpty()) {
                        newTopAdsModel = visitableFound.copy(topAdsImageViewModel = data[0])
                    }
                    newTopAdsModel
                }

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                    HomeTopAdsVerticalBannerDataModel,
                    ArrayList<TopAdsImageViewModel>>(
                    widgetRepository = homeTopadsImageRepository,
                    iterateList = true,
                    bundleParam = {
                        Bundle().apply {
                            putString(
                                HomeTopadsImageRepository.Companion.TOP_ADS_BANNER_TYPE,
                                HomeTopadsImageRepository.Companion.VERTICAL
                            )
                        }
                    },
                    deleteWidgetWhen = {
                        it?.isEmpty() == true
                    }
                ) { visitableFound, data, _ ->
                    var newTopAdsModel = visitableFound.copy()
                    if (data.isNotEmpty()) {
                        newTopAdsModel = visitableFound.copy(topAdsImageViewModelList = data)
                    }
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

                dynamicChannelPlainResponse.getWidgetDataIfExist<
                    ShopFlashSaleWidgetDataModel,
                    List<RecommendationWidget>>(
                    widgetRepository = homeRecommendationRepository,
                    bundleParam = {
                        val shopId = it.tabList.firstOrNull { it.isActivated }?.channelGrid?.id.orEmpty()
                        bundleOf(
                            HomeRecommendationRepository.PAGE_NAME to it.channelModel.pageName,
                            HomeRecommendationRepository.QUERY_PARAM to it.channelModel.widgetParam,
                            HomeRecommendationRepository.SHOP_ID to shopId
                        )
                    },
                ) { visitableFound, data, position ->
                    ShopFlashSaleMapper.mapShopFlashSaleItemList(visitableFound, data)
                }

                emit(
                    dynamicChannelPlainResponse.copy(
                        isCache = false
                    )
                )

                val needToGetRecom =
                    dynamicChannelPlainResponse.evaluateRecommendationSection(currentHomeRecom = localHomeRecommendationFeedDataModel)
                if (needToGetRecom && dynamicChannelPlainResponse.list.size > MINIMUM_DC_TO_SHOW_RECOM) {
                    getFeedTabData(dynamicChannelPlainResponse)
                }
            }

            emit(
                dynamicChannelPlainResponse.copy(
                    isCache = false,
                    flowCompleted = true
                )
            )
            isCacheDc = false
        }
    }

    private inline fun <reified T> widgetIsAvailable(
        homeDataModel: HomeDynamicChannelModel,
        predicate: (T) -> Boolean = { true }
    ): Boolean {
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
            val findRetryModel = homeDataModel.list.withIndex().find { data ->
                data.value is HomeRetryModel
            }

            val findLoadingModel = homeDataModel.list.withIndex().find { data ->
                data.value is HomeLoadingMoreModel
            }

            homeDataModel.list.withIndex().find {
                it.value is HomeRecommendationFeedDataModel ||
                    it.value is HomeLoadingMoreModel ||
                    it.value is HomeRetryModel
            }.let {
                it?.let {
                    when {
                        findLoadingModel != null -> {
                            val newRecomModel = HomeRecommendationFeedDataModel(
                                recommendationTabDataModel = homeRecommendationTabs,
                                homeChooseAddressData = homeDataModel.homeChooseAddressData.copy()
                            )
                            homeDataModel.updateWidgetModel(
                                visitable = newRecomModel,
                                visitableToChange = findLoadingModel.value,
                                position = it.index
                            ) {}
                            this.localHomeRecommendationFeedDataModel = newRecomModel
                        }
                        findRetryModel != null -> {
                            val newRecomModel = HomeRecommendationFeedDataModel(
                                recommendationTabDataModel = homeRecommendationTabs,
                                homeChooseAddressData = homeDataModel.homeChooseAddressData.copy()
                            )
                            homeDataModel.updateWidgetModel(
                                visitable = newRecomModel,
                                visitableToChange = findRetryModel.value,
                                position = it.index
                            ) {}
                            this.localHomeRecommendationFeedDataModel = newRecomModel
                        }
                        else -> {
                            (it.value as? HomeRecommendationFeedDataModel)?.let { recomModel ->
                                val newModel = recomModel.copy(
                                    recommendationTabDataModel = homeRecommendationTabs,
                                    homeChooseAddressData = homeDataModel.homeChooseAddressData.copy()
                                )
                                newModel.recommendationTabDataModel = homeRecommendationTabs
                                newModel.isNewData = true
                                this.localHomeRecommendationFeedDataModel = newModel
                                homeDataModel.updateWidgetModel(
                                    visitable = recomModel,
                                    visitableToChange = newModel,
                                    position = it.index
                                ) {}
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            val findRetryModel = homeDataModel.list.withIndex().find { data ->
                data.value is HomeRetryModel
            }
            val findLoadingModel = homeDataModel.list.withIndex().find { data ->
                data.value is HomeLoadingMoreModel
            }
            homeDataModel.addWidgetModel(HomeRetryModel())
            homeDataModel.deleteWidgetModel(
                findLoadingModel?.value,
                findLoadingModel?.index ?: -1
            ) {}
            homeDataModel.deleteWidgetModel(findRetryModel?.value, findRetryModel?.index ?: -1) {}
        }
    }

    suspend fun getOldBestSellerData(homeDataModel: HomeDynamicChannelModel) {
        findWidget<BestSellerDataModel>(homeDataModel) { bestSellerDataModel, index ->
            val recomFilterList = getRecommendationFilterChips(
                bestSellerDataModel.pageName,
                bestSellerDataModel.widgetParam
            )
            val activatedChip = recomFilterList.find { it.isActivated }
            val recomData = getRecommendationData(
                activatedChip,
                bestSellerDataModel.pageName,
                bestSellerDataModel.widgetParam,
            )

            if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                val recomWidget = recomData.first().copy(
                    recommendationFilterChips = recomFilterList
                )
                val dataModel = bestSellerMapper.mappingRecommendationWidget(
                    recomWidget,
                    cardInteraction = true,
                    bestSellerDataModel,
                )

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

    private suspend fun getBestSellerRevampData(homeDataModel: HomeDynamicChannelModel) {
        findWidget<BestSellerRevampDataModel>(homeDataModel) { bestSellerDataModel, index ->
            val recommendationFilterList = getRecommendationFilterChips(
                bestSellerDataModel.pageName,
                bestSellerDataModel.widgetParam,
            )
            val recommendationFilterIterator = recommendationFilterList.iterator()

            var recommendationData: List<RecommendationWidget>

            while (recommendationFilterIterator.hasNext()) {
                val activatedChip = recommendationFilterIterator.next()

                recommendationData = getRecommendationData(
                    activatedChip,
                    bestSellerDataModel.pageName,
                    bestSellerDataModel.widgetParam,
                )

                if (!recommendationListIsEmpty(recommendationData)) {
                    val updatedBestSellerDataModel =
                        bestSellerRevampMapper.mapChipProductDataModelList(
                            recommendationData,
                            recommendationFilterList,
                            bestSellerDataModel,
                            activatedChip,
                        )

                    homeDataModel.updateWidgetModel(
                        visitable = updatedBestSellerDataModel,
                        visitableToChange = bestSellerDataModel,
                        position = index
                    ) {}

                    break
                }
            }
        }
    }

    private fun recommendationListIsEmpty(recommendationData: List<RecommendationWidget>): Boolean =
        recommendationData.isEmpty()
            || recommendationData.first().recommendationItemList.isEmpty()

    private suspend fun getRecommendationFilterChips(
        pageName: String,
        widgetParam: String
    ): List<RecommendationFilterChipsEntity.RecommendationFilterChip> {
        val recomFilterList =
            mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()

        val recommendationChip = homeRecommendationChipRepository.getRemoteData(
            Bundle().apply {
                putString(HomeRecommendationChipRepository.PAGE_NAME, pageName)
                putString(HomeRecommendationChipRepository.QUERY_PARAM, widgetParam)
            }
        )
        recomFilterList.addAll(recommendationChip)
        return recomFilterList
    }

    private suspend fun getRecommendationData(
        activatedChip: RecommendationFilterChipsEntity.RecommendationFilterChip?,
        pageName: String,
        widgetParam: String,
    ) = if (activatedChip == null) {
            homeRecommendationRepository.getRemoteData(
                Bundle().apply {
                    putString(
                        HomeRecommendationChipRepository.PAGE_NAME,
                        pageName
                    )
                    putString(
                        HomeRecommendationChipRepository.QUERY_PARAM,
                        widgetParam
                    )
                }
            )
        } else {
            homeRecommendationRepository.getRemoteData(
                Bundle().apply {
                    putString(HomeRecommendationChipRepository.PAGE_NAME, pageName)
                    putString(
                        HomeRecommendationChipRepository.QUERY_PARAM,
                        if (activatedChip.isActivated) activatedChip.value else ""
                    )
                }
            )
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
                    productCount = pojo.productCount
                )
            )
        }
        return dataList
    }

    private suspend inline fun <reified T : Visitable<*>, reified K> HomeDynamicChannelModel.getWidgetDataIfExist(
        bundleParam: (T) -> Bundle = { Bundle() },
        iterateList: Boolean = false,
        widgetRepository: HomeRepository<K>,
        predicate: (T?) -> Boolean = { true },
        deleteWidgetWhen: (K?) -> Boolean = { false },
        onWidgetExist: (Int) -> Unit = {},
        mapToWidgetData: (T, K, Int) -> T
    ): HomeDynamicChannelModel {
        try {
            if (!iterateList) {
                findWidget<T>(this, predicate) { visitableFound, visitablePosition ->
                    onWidgetExist.invoke(1)
                    val data = widgetRepository.getRemoteData(bundleParam.invoke(visitableFound))
                    if (!deleteWidgetWhen.invoke(data)) {
                        this.updateWidgetModel(
                            visitable = mapToWidgetData.invoke(
                                visitableFound,
                                data,
                                visitablePosition
                            ),
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
            } else {
                findWidgetList<T>(this, predicate) { indexedValueList ->
                    onWidgetExist.invoke(indexedValueList.size)
                    indexedValueList.forEach {
                        val visitableFound = it.value
                        val visitablePosition = it.index
                        val data =
                            widgetRepository.getRemoteData(bundleParam.invoke(visitableFound))
                        if (!deleteWidgetWhen.invoke(data)) {
                            this.updateWidgetModel(
                                visitable = mapToWidgetData.invoke(
                                    visitableFound,
                                    data,
                                    visitablePosition
                                ),
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
                }
            }
            return this
        } catch (e: Exception) {
            findWidget<T>(this, predicate) { visitableFound, visitablePosition ->
                this.deleteWidgetModel(
                    visitable = visitableFound,
                    position = visitablePosition
                ) {}
            }
            HomeServerLogger.warning_home_repository_error(
                e,
                T::class.java.simpleName,
                K::class.java.simpleName
            )
        }
        return this
    }

    private suspend inline fun <reified T : Visitable<*>, reified K> HomeDynamicChannelModel.getWidgetDataIfExistHandleError(
        bundleParam: (T) -> Bundle = { Bundle() },
        widgetRepository: HomeRepository<K>,
        predicate: (T?) -> Boolean = { true },
        deleteWidgetWhen: (K?) -> Boolean = { false },
        onWidgetExist: (Int) -> Unit = {},
        mapToWidgetData: (T, K, Int) -> T,
        handleOnFailed: (T) -> T
    ): HomeDynamicChannelModel {
        findWidgetList<T>(this, predicate) { indexedValueList ->
            onWidgetExist.invoke(indexedValueList.size)
            indexedValueList.forEach {
                val visitableFound = it.value
                val visitablePosition = it.index
                try {
                    val data = widgetRepository.getRemoteData(bundleParam.invoke(visitableFound))
                    if (!deleteWidgetWhen.invoke(data)) {
                        this.updateWidgetModel(
                            visitable = mapToWidgetData.invoke(
                                visitableFound,
                                data,
                                visitablePosition
                            ),
                            visitableToChange = visitableFound,
                            position = visitablePosition
                        ) {
                            // no-op
                        }
                    } else {
                        this.deleteWidgetModel(
                            visitable = visitableFound,
                            position = visitablePosition
                        ) {
                            // no-op
                        }
                    }
                } catch (e: Exception) {
                    this.updateWidgetModel(
                        visitable = handleOnFailed.invoke(
                            visitableFound
                        ),
                        visitableToChange = visitableFound,
                        position = visitablePosition
                    ) {
                        // no-op
                    }
                }
            }
        }
        return this
    }

    private inline fun <reified T> findWidget(
        homeDataModel: HomeDynamicChannelModel,
        predicate: (T?) -> Boolean = { true },
        actionOnFound: (T, Int) -> Unit
    ) {
        homeDataModel.list.withIndex().filter { it.value is T && predicate.invoke(it.value as? T) }
            .let {
                for (visitable in it) {
                    if (visitable.value is T) {
                        actionOnFound.invoke(visitable.value as T, visitable.index)
                    }
                }
            }
    }

    private inline fun <reified T> findWidgetList(
        homeDataModel: HomeDynamicChannelModel,
        predicate: (T?) -> Boolean = { true },
        actionOnFound: (
            List<IndexedValue<T>>
        ) -> Unit
    ) {
        val listFound = mutableListOf<IndexedValue<T>>()
        homeDataModel.list.withIndex().filter { it.value is T && predicate.invoke(it.value as? T) }
            .let {
                it.forEach { indexedValue ->
                    if (indexedValue.value is T) {
                        (indexedValue as? IndexedValue<T>)?.let { findValue ->
                            listFound.add(findValue)
                        }
                    }
                }
            }
        actionOnFound.invoke(listFound)
    }

    /**
     * Home repository flow:
     *
     * 1. Provide initial HomeData
     * 2. Get above the fold skeleton
     *    2.1 Hit home user status query
     * 3. Get above the fold content
     * 4. Get dynamic channel data
     *    4.1. If remote config pagination enabled, proceed with pagination
     *      4.1.1 If get dynamic channel with page = 1 succeed, then save token to homeData
     *      4.1.2 If get dynamic channel with page = 1 failed, then emit error
     *              Because there is no content that we can show, we showing error page
     *      4.1.3 If channel cache is not empty, proceed to full channel request
     *              - if there is token and cache is not exist
     *              - if cache is exist
     *      4.1.4. If full channel request is success
     *              Then submit current data to database, to trigger HomeViewModel flow
     *              Because there is no content that we can show, we showing error page
     *      4.1.5 If full channel request is failed
     *              Then emit error pagination
     *              Because there is no content that we can show, we showing error page
     *    4.2. If remote config pagination disabled, proceed with no pagination
     *      4.2.1 If full dynamic channel request succeed
     *              Then submit current data to database, to trigger HomeViewModel flow
     *      4.2.2 If full dynamic channel request failed
     *              Then emit error pagination
     *              Because there is no content that we can show, we showing error page
     */
    fun updateHomeData(): Flow<Result<Any>> = flow {
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
            currentHeaderDataModel = null

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
             * 2.1 Hit home user status (fire and forget)
             */
            launch { homeUserStatusRepository.hitHomeStatusThenIgnoreResponse() }

            /**
             * 3. Get above the fold content
             */
            if (homeData.atfData?.dataList?.isNotEmpty() == true) {
                var nonTickerResponseFinished = false

                homeData.atfData?.dataList?.map { atfData ->
                    when (atfData.component) {
                        AtfKey.TYPE_TICKER -> {
                            val job = async {
                                try {
                                    val ticker = homeTickerRepository.getRemoteData(
                                        Bundle().apply {
                                            putString(
                                                HomeTickerRepository.Companion.PARAM_LOCATION,
                                                applicationContext?.let {
                                                    ChooseAddressUtils.getLocalizingAddressData(
                                                        applicationContext
                                                    )?.convertToLocationParams()
                                                } ?: ""
                                            )
                                        }
                                    )
                                    ticker.let {
                                        atfData.content = gson.toJson(ticker.ticker)
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.errorString = ErrorHandler.getErrorMessage(
                                        applicationContext,
                                        MessageErrorException(e.localizedMessage)
                                    )
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
                                    val bannerParam = Bundle().apply {
                                        putString(
                                            HomeDynamicChannelsRepository.PARAMS,
                                            atfData.param
                                        )
                                        this.putString(
                                            HomePageBannerRepository.PARAM_LOCATION,
                                            homeChooseAddressRepository.getRemoteData()
                                                ?.convertToLocationParams()
                                        )
                                    }
                                    val dynamicChannel =
                                        homePageBannerRepository.getRemoteData(bannerParam)
                                    dynamicChannel.let {
                                        if (it.banner.slides?.size ?: 0 >= MINIMUM_BANNER_TO_SHOW) {
                                            val channelFromResponse = it.banner
                                            atfData.content = gson.toJson(channelFromResponse)
                                            atfData.status = AtfKey.STATUS_SUCCESS
                                        } else {
                                            atfData.status = AtfKey.STATUS_EMPTY
                                        }
                                    }
                                    homeData.atfData?.isProcessingAtf = false
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.content = null
                                    atfData.errorString = ErrorHandler.getErrorMessage(
                                        applicationContext,
                                        MessageErrorException(e.localizedMessage)
                                    )
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
                                    val dynamicChannel =
                                        homeDynamicChannelsRepository.getRemoteData(
                                            Bundle().apply {
                                                putString(
                                                    HomeDynamicChannelsRepository.PARAMS,
                                                    atfData.param
                                                )
                                                putString(
                                                    HomeDynamicChannelsRepository.LOCATION,
                                                    applicationContext?.let {
                                                        ChooseAddressUtils.getLocalizingAddressData(
                                                            applicationContext
                                                        )?.convertToLocationParams()
                                                    } ?: ""
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
                                    atfData.errorString = ErrorHandler.getErrorMessage(
                                        applicationContext,
                                        MessageErrorException(e.localizedMessage)
                                    )
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
                                                HomeDynamicChannelsRepository.PARAMS,
                                                atfData.param
                                            )
                                            putString(
                                                HomeDynamicChannelsRepository.LOCATION,
                                                applicationContext?.let {
                                                    ChooseAddressUtils.getLocalizingAddressData(
                                                        applicationContext
                                                    )?.convertToLocationParams()
                                                } ?: ""
                                            )
                                        }
                                    )
                                    dynamicIcon.let {
                                        atfData.content = gson.toJson(
                                            dynamicIcon.dynamicHomeIcon.copy(
                                                type = if (atfData.param.contains(TYPE_ATF_1)) 1 else 2
                                            )
                                        )
                                        atfData.status = AtfKey.STATUS_SUCCESS
                                    }
                                    homeData.atfData?.isProcessingAtf = false
                                } catch (e: Exception) {
                                    atfData.status = AtfKey.STATUS_ERROR
                                    atfData.errorString = ErrorHandler.getErrorMessage(
                                        applicationContext,
                                        MessageErrorException(e.localizedMessage)
                                    )
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
             * 4. Get dynamic channel data
             */
            paginationRemoteConfigCondition(
                remoteConfigPaginationEnabled = {
                    /**
                     * 4.1. If remote config pagination enabled, proceed with pagination
                     */
                    if (!isCacheExistForProcess) {
                        val dynamicChannelResponseValue = try {
                            val dynamicChannelResponse = homeDynamicChannelsRepository
                                .getRemoteData(
                                    Bundle().apply {
                                        putInt(
                                            HomeDynamicChannelsRepository.NUM_OF_CHANNEL,
                                            CHANNEL_LIMIT_FOR_PAGINATION
                                        )
                                        putString(
                                            HomeDynamicChannelsRepository.LOCATION,
                                            applicationContext?.let {
                                                ChooseAddressUtils.getLocalizingAddressData(
                                                    applicationContext
                                                )?.convertToLocationParams()
                                            } ?: ""
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

                        if (dynamicChannelResponseValue != null) {
                            /**
                             * 4.1.1 If get dynamic channel with page = 1 succeed, then save token to homeData
                             */
                            val extractPair = extractToken(dynamicChannelResponseValue)

                            homeData.let {
                                val combinedChannel = combineChannelWith(
                                    it.dynamicHomeChannel,
                                    extractPair.second.dynamicHomeChannel
                                )
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
                        } else {
                            /**
                             * 4.1.2 If get dynamic channel with page = 1 failed, then emit error
                             * Because there is no content that we can show, we showing error page
                             */
                            if (!isCacheExistForProcess &&
                                (
                                    homeData.atfData == null ||
                                        (homeData.atfData?.dataList == null && homeData.atfData?.isProcessingAtf == false) ||
                                        homeData.atfData?.dataList?.isEmpty() == true
                                    )
                            ) {
                                emit(Result.errorGeneral(Throwable(), null))
                            } else {
                                emit(Result.error(Throwable(), null))
                            }
                            saveToDatabase(homeData)
                        }
                    }

                    /**
                     * 4.1.3 If channel cache is not empty, proceed to full channel request
                     * - if there is token and cache is not exist
                     * - if cache is exist
                     *
                     */
                    if ((!isCacheExistForProcess && currentToken.isNotEmpty()) ||
                        isCacheExistForProcess
                    ) {
                        try {
                            homeData = processFullPageDynamicChannel(homeDataResponse = homeData)
                                ?: HomeData()
                            homeData.dynamicHomeChannel.channels.forEach {
                                it.timestamp = currentTimeMillisString
                            }
                            homeData.let {
                                emit(Result.success(null))

                                /**
                                 * 4.1.4. If full channel request is success
                                 * Then submit current data to database, to trigger HomeViewModel flow
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
                             * 4.1.5 If full channel request is failed
                             * Then emit error pagination
                             * Because there is no content that we can show, we showing error page
                             */
                            if (homeData.atfData?.dataList == null || homeData.atfData?.dataList?.isEmpty() == true) {
                                emit(
                                    Result.errorPagination(
                                        error = MessageErrorException(e.localizedMessage),
                                        data = null
                                    )
                                )
                            }
                            cacheCondition(
                                isCacheExistForProcess,
                                isCacheEmptyAction = {
                                    saveToDatabase(homeData)
                                }
                            )
                        }
                    }
                },
                remoteConfigPaginationDisabled = {
                    /**
                     * 4.2. If remote config pagination disabled, proceed with no pagination
                     */
                    try {
                        homeData = processFullPageDynamicChannel(
                            homeDataResponse = homeData
                        )
                            ?: HomeData()
                        homeData.dynamicHomeChannel.channels.forEach {
                            it.timestamp = currentTimeMillisString
                        }
                        homeData.let {
                            emit(Result.success(null))

                            /**
                             * 4.2.1 If full dynamic channel request succeed
                             * Then submit current data to database, to trigger HomeViewModel flow
                             */
                            it.isProcessingDynamicChannel = false
                            if (isAtfSuccess) {
                                saveToDatabase(it, true)
                            } else {
                                saveToDatabase(it, false)
                            }
                        }
                    } catch (e: Exception) {
                        /**
                         * 4.2.2 If full dynamic channel request failed
                         * Then emit error pagination
                         * Because there is no content that we can show, we showing error page
                         */
                        if (homeData.atfData?.dataList == null || homeData.atfData?.dataList?.isEmpty() == true) {
                            emit(
                                Result.errorPagination(
                                    error = MessageErrorException(e.localizedMessage),
                                    data = null
                                )
                            )
                        }
                        cacheCondition(
                            isCacheExistForProcess,
                            isCacheEmptyAction = {
                                saveToDatabase(homeData)
                            }
                        )
                    }
                }
            )
        }
    }

    private suspend fun paginationRemoteConfigCondition(
        remoteConfigPaginationDisabled: suspend () -> Unit,
        remoteConfigPaginationEnabled: suspend () -> Unit
    ) {
        val disablePagination =
            remoteConfig.getBoolean(RemoteConfigKey.HOME_REMOVE_PAGINATION, true)
        if (disablePagination) {
            remoteConfigPaginationDisabled.invoke()
        } else {
            remoteConfigPaginationEnabled.invoke()
        }
    }

    private suspend fun saveToDatabase(homeData: HomeData?, saveAtf: Boolean = false) {
        getHomeRoomDataSource.saveToDatabase(homeData)
        if (saveAtf) {
            homeData?.atfData?.let {
                getHomeRoomDataSource.saveCachedAtf(
                    it.dataList.mapIndexed { idx, atfData ->
                        AtfCacheEntity(
                            id = atfData.id,
                            position = idx,
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

    suspend fun onDynamicChannelExpired(groupId: String): List<Visitable<*>> {
        val dynamicChannelResponse = homeDynamicChannelsRepository.getRemoteData(
            Bundle().apply {
                putString(
                    HomeDynamicChannelsRepository.GROUP_IDS,
                    groupId
                )
                putString(
                    HomeDynamicChannelsRepository.LOCATION,
                    applicationContext?.let {
                        ChooseAddressUtils.getLocalizingAddressData(applicationContext)
                            ?.convertToLocationParams()
                    } ?: ""
                )
            }
        )
        val homeChannelData = HomeChannelData(dynamicChannelResponse.dynamicHomeChannel)

        return homeDynamicChannelDataMapper.mapToDynamicChannelDataModel(
            homeChannelData,
            isCache = false,
            addLoadingMore = false,
            useDefaultWhenEmpty = false
        )
    }

    private suspend fun processFullPageDynamicChannel(homeDataResponse: HomeData?): HomeData? {
        val dynamicChannelCompleteResponse = homeDynamicChannelsRepository.getRemoteData(
            Bundle().apply {
                putInt(
                    HomeDynamicChannelsRepository.NUM_OF_CHANNEL,
                    0
                )
                putString(
                    HomeDynamicChannelsRepository.TOKEN,
                    homeDataResponse?.token ?: ""
                )
                putString(
                    HomeDynamicChannelsRepository.LOCATION,
                    applicationContext?.let {
                        ChooseAddressUtils.getLocalizingAddressData(applicationContext)
                            ?.convertToLocationParams()
                    } ?: ""
                )
            }
        )
        val currentChannelList =
            homeDataResponse?.dynamicHomeChannel?.channels?.toMutableList() ?: mutableListOf()
        currentChannelList.addAll(dynamicChannelCompleteResponse.dynamicHomeChannel.channels)

        homeDataResponse?.let {
            homeDataResponse.token = ""
            val combinedChannel = combineChannelWith(
                homeDataResponse.dynamicHomeChannel,
                dynamicChannelCompleteResponse.dynamicHomeChannel
            )
            homeDataResponse.dynamicHomeChannel = combinedChannel
        }
        return homeDataResponse
    }

    fun deleteHomeData() {
        getHomeRoomDataSource.deleteHomeData()
    }

    private fun extractToken(homeChannelData: HomeChannelData): Pair<String, HomeChannelData> {
        return if (homeChannelData.dynamicHomeChannel.channels.isNotEmpty()) {
            val token = homeChannelData.dynamicHomeChannel.channels[0].token
            homeChannelData.dynamicHomeChannel.channels[0].token = ""
            Pair(token, homeChannelData)
        } else {
            Pair("", homeChannelData)
        }
    }

    private fun combineChannelWith(
        currentChannel: DynamicHomeChannel,
        newChannel: DynamicHomeChannel
    ): DynamicHomeChannel {
        val combinationChannel = currentChannel.channels.toMutableList()
        combinationChannel.addAll(newChannel.channels)
        return DynamicHomeChannel(combinationChannel)
    }

    private suspend fun cacheCondition(
        isCache: Boolean,
        isCacheExistAction: suspend () -> Unit = {},
        isCacheEmptyAction: suspend () -> Unit = {}
    ) {
        if (isCache) {
            isCacheExistAction.invoke()
        } else {
            isCacheEmptyAction.invoke()
        }
    }
}
