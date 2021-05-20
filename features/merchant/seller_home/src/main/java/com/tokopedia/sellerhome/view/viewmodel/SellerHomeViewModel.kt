package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.usecase.GetShopLocationUseCase
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.common.const.WidgetHeight
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.usecase.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeViewModel @Inject constructor(
        private val userSession: Lazy<UserSessionInterface>,
        private val getTickerUseCase: Lazy<GetTickerUseCase>,
        private val getLayoutUseCase: Lazy<GetLayoutUseCase>,
        private val getShopLocationUseCase: Lazy<GetShopLocationUseCase>,
        private val getCardDataUseCase: Lazy<GetCardDataUseCase>,
        private val getLineGraphDataUseCase: Lazy<GetLineGraphDataUseCase>,
        private val getProgressDataUseCase: Lazy<GetProgressDataUseCase>,
        private val getPostDataUseCase: Lazy<GetPostDataUseCase>,
        private val getCarouselDataUseCase: Lazy<GetCarouselDataUseCase>,
        private val getTableDataUseCase: Lazy<GetTableDataUseCase>,
        private val getPieChartDataUseCase: Lazy<GetPieChartDataUseCase>,
        private val getBarChartDataUseCase: Lazy<GetBarChartDataUseCase>,
        private val getMultiLineGraphUseCase: Lazy<GetMultiLineGraphUseCase>,
        private val getAnnouncementUseCase: Lazy<GetAnnouncementDataUseCase>,
        private val getRecommendationUseCase: Lazy<GetRecommendationDataUseCase>,
        private val remoteConfig: SellerHomeRemoteConfig,
        private val dispatcher: CoroutineDispatchers
) : CustomBaseViewModel(dispatcher) {

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
        private const val SELLER_HOME_PAGE_NAME = "seller-home"
        private const val TICKER_PAGE_NAME = "seller"
    }

    private val shopId: String by lazy { userSession.get().shopId }
    private val dynamicParameter by lazy {
        val startDateMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 7)
        val endDateMillis = DateTimeUtil.getNPastDaysTimestamp(daysBefore = 1)
        return@lazy DynamicParameterModel(
                startDate = DateTimeUtil.format(startDateMillis, DATE_FORMAT),
                endDate = DateTimeUtil.format(endDateMillis, DATE_FORMAT),
                pageSource = SELLER_HOME_PAGE_NAME,
                dateType = DateFilterType.DATE_TYPE_DAY
        )
    }

    private val _homeTicker = MutableLiveData<Result<List<TickerItemUiModel>>>()
    private val _widgetLayout = MutableLiveData<Result<List<BaseWidgetUiModel<*>>>>()
    private val _shopLocation = MutableLiveData<Result<ShippingLoc>>()
    private val _cardWidgetData = MutableLiveData<Result<List<CardDataUiModel>>>()
    private val _lineGraphWidgetData = MutableLiveData<Result<List<LineGraphDataUiModel>>>()
    private val _progressWidgetData = MutableLiveData<Result<List<ProgressDataUiModel>>>()
    private val _postListWidgetData = MutableLiveData<Result<List<PostListDataUiModel>>>()
    private val _carouselWidgetData = MutableLiveData<Result<List<CarouselDataUiModel>>>()
    private val _tableWidgetData = MutableLiveData<Result<List<TableDataUiModel>>>()
    private val _pieChartWidgetData = MutableLiveData<Result<List<PieChartDataUiModel>>>()
    private val _barChartWidgetData = MutableLiveData<Result<List<BarChartDataUiModel>>>()
    private val _multiLineGraphWidgetData = MutableLiveData<Result<List<MultiLineGraphDataUiModel>>>()
    private val _announcementWidgetData = MutableLiveData<Result<List<AnnouncementDataUiModel>>>()
    private val _startWidgetCustomMetricTag = MutableLiveData<String>()
    private val _stopWidgetType = MutableLiveData<String>()
    private val _recommendationWidgetData = MutableLiveData<Result<List<RecommendationDataUiModel>>>()

    val homeTicker: LiveData<Result<List<TickerItemUiModel>>>
        get() = _homeTicker
    val widgetLayout: LiveData<Result<List<BaseWidgetUiModel<*>>>>
        get() = _widgetLayout
    val shopLocation: LiveData<Result<ShippingLoc>>
        get() = _shopLocation
    val cardWidgetData: LiveData<Result<List<CardDataUiModel>>>
        get() = _cardWidgetData
    val lineGraphWidgetData: LiveData<Result<List<LineGraphDataUiModel>>>
        get() = _lineGraphWidgetData
    val progressWidgetData: LiveData<Result<List<ProgressDataUiModel>>>
        get() = _progressWidgetData
    val postListWidgetData: LiveData<Result<List<PostListDataUiModel>>>
        get() = _postListWidgetData
    val carouselWidgetData: LiveData<Result<List<CarouselDataUiModel>>>
        get() = _carouselWidgetData
    val tableWidgetData: LiveData<Result<List<TableDataUiModel>>>
        get() = _tableWidgetData
    val pieChartWidgetData: LiveData<Result<List<PieChartDataUiModel>>>
        get() = _pieChartWidgetData
    val barChartWidgetData: LiveData<Result<List<BarChartDataUiModel>>>
        get() = _barChartWidgetData
    val multiLineGraphWidgetData: LiveData<Result<List<MultiLineGraphDataUiModel>>>
        get() = _multiLineGraphWidgetData
    val announcementWidgetData: LiveData<Result<List<AnnouncementDataUiModel>>>
        get() = _announcementWidgetData
    val startWidgetCustomMetricTag: LiveData<String>
        get() = _startWidgetCustomMetricTag
    val stopWidgetType: LiveData<String>
        get() = _stopWidgetType
    val recommendationWidgetData: LiveData<Result<List<RecommendationDataUiModel>>>
        get() = _recommendationWidgetData

    private suspend fun <T : Any> BaseGqlUseCase<T>.executeUseCase() = withContext(dispatcher.io) {
        executeOnBackground()
    }

    private suspend fun <T : Any> getDataFromUseCase(useCase: BaseGqlUseCase<T>,
                                                     liveData: MutableLiveData<Result<T>>) {
        if (remoteConfig.isSellerHomeDashboardCachingEnabled() && useCase.isFirstLoad) {
            useCase.isFirstLoad = false
            try {
                useCase.setUseCache(true)
                liveData.value = Success(useCase.executeUseCase())
            } catch (_: Exception) {
                // ignore exception from cache
            }
        }
        useCase.setUseCache(false)
        liveData.value = Success(useCase.executeUseCase())
    }

    private suspend fun <T: Any> getDataFromUseCase(useCase: BaseGqlUseCase<T>): T {
        useCase.setUseCache(false)
        return useCase.executeUseCase()
    }

    private suspend fun <T : Any> getDataFromUseCase(useCase: BaseGqlUseCase<T>,
                                                     liveData: MutableLiveData<Result<T>>,
                                                     getTransformerFlow: suspend (T) -> Flow<T>) {
        if (remoteConfig.isSellerHomeDashboardCachingEnabled() && useCase.isFirstLoad) {
            useCase.isFirstLoad = false
            try {
                useCase.setUseCache(true)
                val useCaseResult = useCase.executeOnBackground()
                getTransformerFlow(useCaseResult).collect {
                    liveData.value = Success(it)
                }
            } catch (_: Exception) {
                // ignore exception from cache
            }
        }
        useCase.setUseCache(false)
        val useCaseResult = useCase.executeOnBackground()
        getTransformerFlow(useCaseResult).collect {
            liveData.value = Success(it)
        }
    }

    private fun <T : Any> CloudAndCacheGraphqlUseCase<*, T>.startCollectingResult(liveData: MutableLiveData<Result<T>>) {
        if (!collectingResult) {
            collectingResult = true
            launchCatchError(block = {
                getResultFlow().collect {
                    withContext(dispatcher.main) {
                        liveData.value = Success(it)
                    }
                }
            }, onError = {
                liveData.value = Fail(it)
            })
        }
    }

    private fun <T : Any> CloudAndCacheGraphqlUseCase<*, T>.startCollectingResult(liveData: MutableLiveData<Result<T>>,
                                                                                  getTransformedFlow: suspend (T) -> Flow<T>) {
        if (!collectingResult) {
            collectingResult = true
            launchCatchError(block = {
                getResultFlow().collect { initialResult ->
                    getTransformedFlow(initialResult).collect {
                        withContext(dispatcher.main) {
                            liveData.value = Success(it)
                        }
                    }
                }
            }, onError = {
                liveData.value = Fail(it)
            })
        }
    }

    fun getTicker() {
        launchCatchError(block = {
            val params = GetTickerUseCase.createParams(TICKER_PAGE_NAME)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getTickerUseCase.get().run {
                    startCollectingResult(_homeTicker)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getTickerUseCase.get().params = params
                getDataFromUseCase(getTickerUseCase.get(), _homeTicker)
            }
        }, onError = {
            _homeTicker.value = Fail(it)
        })
    }

    /**
     * Get widget layout for seller home. If screen height value is provided,
     * that means we will also fetch widgets' data that expected to be shown first on the screen
     * and combine those before showing the widgets to user.
     *
     * @param   heightDp    height of device screen in dp
     */
    fun getWidgetLayout(heightDp: Float? = null) {
        launchCatchError(block = {
            val params = GetLayoutUseCase.getRequestParams(shopId, SELLER_HOME_PAGE_NAME)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getLayoutUseCase.get().run {
                    if (heightDp == null) {
                        startCollectingResult(_widgetLayout)
                    } else {
                        startCollectingResult(_widgetLayout) {
                            getInitialWidget(it, heightDp).flowOn(dispatcher.io)
                        }
                    }
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getLayoutUseCase.get().params = params
                if (heightDp == null) {
                    getDataFromUseCase(getLayoutUseCase.get(), _widgetLayout)
                } else {
                    getDataFromUseCase(getLayoutUseCase.get(), _widgetLayout) {
                        getInitialWidget(it, heightDp)
                    }
                }
            }
        }, onError = {
            _widgetLayout.value = Fail(it)
        })
    }

    fun getCardWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getCardDataUseCase.get().run {
                    startCollectingResult(_cardWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getCardDataUseCase.get().params = params
                getDataFromUseCase(getCardDataUseCase.get(), _cardWidgetData)
            }
        }, onError = {
            _cardWidgetData.value = Fail(it)
        })
    }

    fun getLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getLineGraphDataUseCase.get().run {
                    startCollectingResult(_lineGraphWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getLineGraphDataUseCase.get().params = params
                getDataFromUseCase(getLineGraphDataUseCase.get(), _lineGraphWidgetData)
            }
        }, onError = {
            _lineGraphWidgetData.value = Fail(it)
        })
    }

    fun getProgressWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
            val params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getProgressDataUseCase.get().run {
                    startCollectingResult(_progressWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getProgressDataUseCase.get().params = params
                getDataFromUseCase(getProgressDataUseCase.get(), _progressWidgetData)
            }
        }, onError = {
            _progressWidgetData.value = Fail(it)
        })
    }

    fun getPostWidgetData(dataKeys: List<Pair<String, String>>) {
        launchCatchError(block = {
            val params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getPostDataUseCase.get().run {
                    startCollectingResult(_postListWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getPostDataUseCase.get().params = params
                getDataFromUseCase(getPostDataUseCase.get(), _postListWidgetData)
            }
        }, onError = {
            _postListWidgetData.value = Fail(it)
        })
    }

    fun getCarouselWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetCarouselDataUseCase.getRequestParams(dataKeys)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getCarouselDataUseCase.get().run {
                    startCollectingResult(_carouselWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getCarouselDataUseCase.get().params = params
                getDataFromUseCase(getCarouselDataUseCase.get(), _carouselWidgetData)
            }
        }, onError = {
            _carouselWidgetData.value = Fail(it)
        })
    }

    fun getTableWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getTableDataUseCase.get().run {
                    startCollectingResult(_tableWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getTableDataUseCase.get().params = params
                getDataFromUseCase(getTableDataUseCase.get(), _tableWidgetData)
            }
        }, onError = {
            _tableWidgetData.value = Fail(it)
        })
    }

    fun getPieChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getPieChartDataUseCase.get().run {
                    startCollectingResult(_pieChartWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getPieChartDataUseCase.get().params = params
                getDataFromUseCase(getPieChartDataUseCase.get(), _pieChartWidgetData)
            }
        }, onError = {
            _pieChartWidgetData.value = Fail(it)
        })
    }

    fun getBarChartWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getBarChartDataUseCase.get().run {
                    startCollectingResult(_barChartWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getBarChartDataUseCase.get().params = params
                getDataFromUseCase(getBarChartDataUseCase.get(), _barChartWidgetData)
            }
        }, onError = {
            _barChartWidgetData.value = Fail(it)
        })
    }

    fun getMultiLineGraphWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getMultiLineGraphUseCase.get().run {
                    startCollectingResult(_multiLineGraphWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                val result: Success<List<MultiLineGraphDataUiModel>> = Success(withContext(dispatcher.io) {
                    getMultiLineGraphUseCase.get().params = params
                    return@withContext getMultiLineGraphUseCase.get().executeOnBackground()
                })
                _multiLineGraphWidgetData.value = result
            }
        }, onError = {
            _multiLineGraphWidgetData.value = Fail(it)
        })
    }

    fun getAnnouncementWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getAnnouncementUseCase.get().run {
                    startCollectingResult(_announcementWidgetData)
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                val result: Success<List<AnnouncementDataUiModel>> = Success(withContext(dispatcher.io) {
                    getAnnouncementUseCase.get().params = params
                    return@withContext getAnnouncementUseCase.get().executeOnBackground()
                })
                _announcementWidgetData.value = result
            }
        }, onError = {
            _announcementWidgetData.value = Fail(it)
        })
    }

    fun getRecommendationWidgetData(dataKeys: List<String>) {
        launchCatchError(block = {
            val result: Success<List<RecommendationDataUiModel>> = Success(withContext(dispatcher.io) {
                getRecommendationUseCase.get().params = GetRecommendationDataUseCase.createParams(dataKeys)
                return@withContext getRecommendationUseCase.get().executeOnBackground()
            })
            _recommendationWidgetData.value = result
        }, onError = {
            _recommendationWidgetData.value = Fail(it)
        })
    }

    fun getShopLocation() {
        launchCatchError(block = {
            val result: Success<ShippingLoc> = Success(withContext(dispatcher.io) {
                getShopLocationUseCase.get().params = GetShopLocationUseCase.getRequestParams(shopId)
                return@withContext getShopLocationUseCase.get().executeOnBackground()
            })
            _shopLocation.value = result
        }, onError = {
            _shopLocation.value = Fail(it)
        })
    }

    /**
     * Return flow that will get predicted widget that should be shown first in the screen and load the widgets's data
     * After that, map the original widgets to initial loaded widgets by their ids
     *
     * @param   widgets         original widget list
     * @param   deviceHeightDp  expected screen height to determine initial widgets to be loaded
     * @return  flow that emit mapped/combined widget layouts
     */
    private suspend fun getInitialWidget(widgets: List<BaseWidgetUiModel<*>>, deviceHeightDp: Float): Flow<List<BaseWidgetUiModel<*>>> {
        val widgetFlow = flow { emit(widgets) }
        val predictedInitialWidgetFlow = getPredictedInitialWidget(widgets, deviceHeightDp)
        return widgetFlow.combine(predictedInitialWidgetFlow) { widgetsFromFlow, initialWidgets ->
            widgetsFromFlow
                    .map { widget -> initialWidgets.find { it.id == widget.id } ?: widget }
                    .filter { !it.isNeedToBeRemoved }
        }
    }

    /**
     * Return flow that will calculate height to predict which widgets that will load their data initially
     *
     * @param   widgetList      original widget list
     * @param   deviceHeightDp  expected screen height to determine initial widgets to be loaded
     * @return  flow that will return initial widgets layout which has its data loaded
     */
    private fun getPredictedInitialWidget(widgetList: List<BaseWidgetUiModel<*>>, deviceHeightDp: Float): Flow<List<BaseWidgetUiModel<*>>> {
        var remainingHeight = deviceHeightDp
        var hasCardCalculated = false
        val newWidgetList = widgetList.map { widget ->
            val requestedHeight = WidgetHeight.getWidgetHeight(widget.widgetType)
            if (remainingHeight > 0f) {
                widget.apply { isLoading = true }
            } else {
                widget
            }.also {
                if (widget.widgetType == WidgetType.CARD) {
                    if (!hasCardCalculated) {
                        remainingHeight -= requestedHeight
                    }
                    hasCardCalculated = !hasCardCalculated
                } else {
                    remainingHeight -= requestedHeight
                }
            }
        }
        return getLoadedInitialWidgetData(newWidgetList)
    }

    /**
     * Return flow that will get all required initial widget data by checking which widgets that still need their data loaded
     *
     * @param   widgetList  predicted initial widget list
     * @return  flow that return list of all loaded widget
     */
    private fun getLoadedInitialWidgetData(widgetList: List<BaseWidgetUiModel<*>>): Flow<List<BaseWidgetUiModel<*>>> {
        val loadedWidgetList = widgetList.filter { it.isLoading }

        val newWidgetList = loadedWidgetList.toMutableList()
        loadedWidgetList.filter { it.widgetType == WidgetType.SECTION }.forEach { section ->
            newWidgetList.indexOf(section).let { index ->
                newWidgetList[index] = section.copy().apply { isLoaded = true }
            }
        }
        return getWidgetsData(newWidgetList)
    }

    /**
     * Return flow that will load required data from each expected widget and combine those to single widget list
     *
     * @param   widgets     list of widgets which their data need to be loaded
     * @return  flow that return list of combined widget list that has their data loaded
     */
    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>): Flow<List<BaseWidgetUiModel<*>>> {
        val groupedWidgets = widgets.groupBy { it.widgetType } as MutableMap
        val lineGraphDataFlow = groupedWidgets.getWidgetDataByType<LineGraphDataUiModel>(WidgetType.LINE_GRAPH)
        val announcementDataFlow = groupedWidgets.getWidgetDataByType<AnnouncementDataUiModel>(WidgetType.ANNOUNCEMENT)
        val cardDataFlow = groupedWidgets.getWidgetDataByType<CardDataUiModel>(WidgetType.CARD)
        val progressDataFlow = groupedWidgets.getWidgetDataByType<ProgressDataUiModel>(WidgetType.PROGRESS)
        val carouselDataFlow = groupedWidgets.getWidgetDataByType<CarouselDataUiModel>(WidgetType.CAROUSEL)
        val postDataFlow = groupedWidgets.getWidgetDataByType<PostListDataUiModel>(WidgetType.POST_LIST)
        val tableDataFlow = groupedWidgets.getWidgetDataByType<TableDataUiModel>(WidgetType.TABLE)
        val pieChartDataFlow = groupedWidgets.getWidgetDataByType<PieChartDataUiModel>(WidgetType.PIE_CHART)
        val barChartDataFlow = groupedWidgets.getWidgetDataByType<BarChartDataUiModel>(WidgetType.BAR_CHART)
        val multiLineGraphDataFlow = groupedWidgets.getWidgetDataByType<MultiLineGraphDataUiModel>(WidgetType.MULTI_LINE_GRAPH)

        return combine(lineGraphDataFlow, announcementDataFlow, cardDataFlow, progressDataFlow,
                carouselDataFlow, postDataFlow, tableDataFlow, pieChartDataFlow,
                barChartDataFlow, multiLineGraphDataFlow) { widgetDataList ->
            val widgetsData = widgetDataList.flatMap { it }
            widgetsData.mapToWidgetModel(widgets)
        }
    }

    /**
     * Extension function for mapped initial widget list by type which will return flow that will get widget data by its type.
     * If error, will instantiate new data ui model that will has the error message assigned to avoid throwing exception.
     * Also, will stop network custom trace PLT for loaded widget data after completion.
     *
     * @param   widgetType  type of widget which data needed to be loaded
     * @return  flow that will emit list of data for current widget type
     */
    private inline fun <reified D : BaseDataUiModel> MutableMap<String, List<BaseWidgetUiModel<*>>>.getWidgetDataByType(widgetType: String): Flow<List<BaseDataUiModel>> {
        return flow {
            val widgetList = this@getWidgetDataByType[widgetType]
            val widgetDataList =
                    try {
                        widgetList?.let {
                            when(widgetType) {
                                WidgetType.LINE_GRAPH -> getLineGraphData(it)
                                WidgetType.ANNOUNCEMENT -> getAnnouncementData(it)
                                WidgetType.CARD -> getCardData(it)
                                WidgetType.PROGRESS -> getProgressData(it)
                                WidgetType.CAROUSEL -> getCarouselData(it)
                                WidgetType.POST_LIST -> getPostData(it)
                                WidgetType.TABLE -> getTableData(it)
                                WidgetType.PIE_CHART -> getPieChartData(it)
                                WidgetType.BAR_CHART -> getBarChartData(it)
                                WidgetType.MULTI_LINE_GRAPH -> getMultiLineGraphData(it)
                                else -> null
                            }
                        }.orEmpty()
                    } catch (ex: Exception) {
                        widgetList?.map { widget ->
                            D::class.java.newInstance().apply {
                                dataKey = widget.dataKey
                                error = ex.message.orEmpty()
                            }
                        }.orEmpty()
                    }
            withContext(dispatcher.main) {
                _stopWidgetType.value = widgetType
            }
            emit(widgetDataList)
        }
    }

    /**
     * Extension function for list of widget data which will be mapped ot its original initial widget list
     *
     * @param   widgets     original initial widget list
     * @return  list of mapped widgets with their data
     */
    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.mapToWidgetModel(widgets: List<BaseWidgetUiModel<*>>): List<BaseWidgetUiModel<*>> {
        val newWidgetList = widgets.toMutableList()
        forEach{ widgetData ->
            newWidgetList.indexOfFirst {
                it.dataKey == widgetData.dataKey
            }.takeIf { it > -1 }?.let { index ->
                val widget = newWidgetList.getOrNull(index)
                if (widget is W) {
                    val copiedWidget = widget.copy()
                    copiedWidget.data = widgetData
                    if (shouldRemoveWidgetInitially(widget, widgetData)) {
                        copiedWidget.isNeedToBeRemoved = true
                        removeEmptySections(newWidgetList, index)
                    } else {
                        copiedWidget.isLoading = widget.data?.isFromCache ?: false
                    }
                    newWidgetList[index] = copiedWidget
                }
            }
        }
        return newWidgetList
    }

    /**
     * Determine whether the widget need to be removed from its original list, which depends on its data value.
     * This will only take cloud values in consideration, not cache.
     *
     * @param   widget      widget to check
     * @param   widgetData  the widget's data
     * @return  is the widget should be removed
     */
    private fun shouldRemoveWidgetInitially(widget: BaseWidgetUiModel<*>, widgetData: BaseDataUiModel): Boolean {
        return !widgetData.showWidget || (!widget.isShowEmpty && widgetData.shouldRemove())
    }

    private fun removeEmptySections(newWidgetList: MutableList<BaseWidgetUiModel<*>>, removedWidgetIndex: Int) {
        val previousWidgetIndex = newWidgetList.take(removedWidgetIndex).indexOfLast { !it.isNeedToBeRemoved }
        val previousWidget = newWidgetList.getOrNull(previousWidgetIndex)
        val widgetReplacement = newWidgetList.getOrNull(removedWidgetIndex + 1)
        if ((widgetReplacement == null || widgetReplacement is SectionWidgetUiModel) && previousWidget is SectionWidgetUiModel) {
            previousWidget.isNeedToBeRemoved = true
            newWidgetList[previousWidgetIndex] = previousWidget
        }
    }

    private suspend fun getCardData(widgets: List<BaseWidgetUiModel<*>>): List<CardDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        val params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getCardDataUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_CARD_TRACE
        }
        return getDataFromUseCase(getCardDataUseCase.get())
    }

    private suspend fun getLineGraphData(widgets: List<BaseWidgetUiModel<*>>): List<LineGraphDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        val params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getLineGraphDataUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_LINE_GRAPH_TRACE
        }
        return getDataFromUseCase(getLineGraphDataUseCase.get())
    }

    private suspend fun getProgressData(widgets: List<BaseWidgetUiModel<*>>): List<ProgressDataUiModel> {
        widgets.setLoading()
        val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        val params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
        getProgressDataUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_PROGRESS_TRACE
        }
        return getDataFromUseCase(getProgressDataUseCase.get())
    }

    private suspend fun getPostData(widgets: List<BaseWidgetUiModel<*>>): List<PostListDataUiModel> {
        widgets.setLoading()
        val dataKeys: List<Pair<String, String>> = widgets.filterIsInstance<PostListWidgetUiModel>().map {
            val postFilter = it.postFilter.find { filter -> filter.isSelected }
            val postFilters = postFilter?.value.orEmpty()
            return@map Pair(it.dataKey, postFilters)
        }
        val params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getPostDataUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_POST_LIST_TRACE
        }
        return getDataFromUseCase(getPostDataUseCase.get())
    }

    private suspend fun getCarouselData(widgets: List<BaseWidgetUiModel<*>>): List<CarouselDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        val params = GetCarouselDataUseCase.getRequestParams(dataKeys)
        getCarouselDataUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_CAROUSEL_TRACE
        }
        return getDataFromUseCase(getCarouselDataUseCase.get())
    }

    private suspend fun getTableData(widgets: List<BaseWidgetUiModel<*>>): List<TableDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<TableWidgetUiModel>(widgets)
        val params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getTableDataUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_TABLE_TRACE
        }
        return getDataFromUseCase(getTableDataUseCase.get())
    }

    private suspend fun getPieChartData(widgets: List<BaseWidgetUiModel<*>>): List<PieChartDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<PieChartWidgetUiModel>(widgets)
        val params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getPieChartDataUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_PIE_CHART_TRACE
        }
        return getDataFromUseCase(getPieChartDataUseCase.get())
    }

    private suspend fun getBarChartData(widgets: List<BaseWidgetUiModel<*>>): List<BarChartDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<BarChartWidgetUiModel>(widgets)
        val params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getBarChartDataUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_BAR_CHART_TRACE
        }
        return getDataFromUseCase(getBarChartDataUseCase.get())
    }

    private suspend fun getMultiLineGraphData(widgets: List<BaseWidgetUiModel<*>>): List<MultiLineGraphDataUiModel> {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<MultiLineGraphWidgetUiModel>(widgets)
        val params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)
        getMultiLineGraphUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_MULTI_LINE_GRAPH_TRACE
        }
        return getMultiLineGraphUseCase.get().executeOnBackground()
    }

    private suspend fun getAnnouncementData(widgets: List<BaseWidgetUiModel<*>>): List<AnnouncementDataUiModel> {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<AnnouncementWidgetUiModel>(widgets)
        val params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)
        getAnnouncementUseCase.get().params = params
        withContext(dispatcher.main) {
            _startWidgetCustomMetricTag.value = SellerHomePerformanceMonitoringConstant.SELLER_HOME_ANNOUNCEMENT_TRACE
        }
        return getAnnouncementUseCase.get().executeOnBackground()
    }

    private fun List<BaseWidgetUiModel<*>>.setLoading() {
        forEach {
            it.isLoading = true
            it.isLoaded = true
        }
    }

}