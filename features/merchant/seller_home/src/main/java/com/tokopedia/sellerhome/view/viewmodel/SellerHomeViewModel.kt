package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
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

    private suspend fun <T : Any> BaseGqlUseCase<T>.executeUseCase() = withContext(dispatcher.io) {
        executeOnBackground()
    }

    private suspend fun <T : Any> getDataFromUseCase(useCase: BaseGqlUseCase<T>,
                                                     liveData: MutableLiveData<Result<T>>,
                                                     transformer: suspend (result: T) -> T = {result -> result} ) {
        if (remoteConfig.isSellerHomeDashboardCachingEnabled() && useCase.isFirstLoad) {
            useCase.isFirstLoad = false
            try {
                useCase.setUseCache(true)
                liveData.value = Success(transformer(useCase.executeUseCase()))
            } catch (_: Exception) {
                // ignore exception from cache
            }
        }
        useCase.setUseCache(false)
        liveData.value = Success(transformer(useCase.executeUseCase()))
    }

    private suspend fun <T: Any> getDataFromUseCase(useCase: BaseGqlUseCase<T>): T {
        if (remoteConfig.isSellerHomeDashboardCachingEnabled() && useCase.isFirstLoad) {
            useCase.isFirstLoad = false
            useCase.setUseCache(true)
        } else {
            useCase.setUseCache(false)
        }
        return useCase.executeUseCase()
    }

    private fun <T : Any> CloudAndCacheGraphqlUseCase<*, T>.startCollectingResult(liveData: MutableLiveData<Result<T>>,
                                                                                  transformer: suspend (result: T) -> T = {result -> result} ) {
        if (!collectingResult) {
            collectingResult = true
            launchCatchError(block = {
                getResultFlow().collect {
                    withContext(dispatcher.main) {
                        liveData.value = Success(transformer(it))
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

    fun getWidgetLayout(heightDp: Float) {
        launchCatchError(block = {
            val params = GetLayoutUseCase.getRequestParams(shopId, SELLER_HOME_PAGE_NAME)
            if (remoteConfig.isSellerHomeDashboardNewCachingEnabled()) {
                getLayoutUseCase.get().run {
                    startCollectingResult(_widgetLayout) {
                        getInitialWidget(it, heightDp)
                    }
                    executeOnBackground(params, isFirstLoad && remoteConfig.isSellerHomeDashboardCachingEnabled())
                }
            } else {
                getLayoutUseCase.get().params = params
                getDataFromUseCase(getLayoutUseCase.get(), _widgetLayout) {
                    getInitialWidget(it, heightDp)
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

    private suspend fun getInitialWidget(widgets: List<BaseWidgetUiModel<*>>, deviceHeightDp: Float): List<BaseWidgetUiModel<*>> {
        val predictedInitialWidget = getPredictedInitialWidget(widgets, deviceHeightDp)
        return widgets
                .map { widget -> predictedInitialWidget.find { it.id == widget.id } ?: widget }
                .filter { !it.isNeedToBeRemoved }
    }

    private suspend fun getPredictedInitialWidget(widgetList: List<BaseWidgetUiModel<*>>, deviceHeightDp: Float): List<BaseWidgetUiModel<*>> {
        var remainingHeight = deviceHeightDp
        var hasCardCalculated = false
        val newWidgetList = widgetList.map { widget ->
            val requestedHeight = WidgetHeight.getWidgetHeight(widget.widgetType)
            if (widget.widgetType == WidgetType.CARD) {
                if (!hasCardCalculated) {
                    remainingHeight -= requestedHeight
                }
                hasCardCalculated = !hasCardCalculated
            } else {
                remainingHeight -= requestedHeight
            }
            if (remainingHeight > 0f) {
                widget.apply { isLoading = true }
            } else {
                widget
            }
        }
        return getLoadedInitialWidgetData(newWidgetList)
    }

    private suspend fun getLoadedInitialWidgetData(widgetList: List<BaseWidgetUiModel<*>>): List<BaseWidgetUiModel<*>> {
        val loadedWidgetList = widgetList.filter { it.isLoading }

        val newWidgetList = loadedWidgetList.toMutableList()
        loadedWidgetList.filter { it.widgetType == WidgetType.SECTION }.forEach { section ->
            newWidgetList.indexOf(section).takeIf { it > -1 }?.let { index ->
                newWidgetList[index] = section.copy().apply { isLoaded = true }
            }
        }
        return getWidgetsData(newWidgetList).mapToWidgetModel(newWidgetList)
    }

    private suspend fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>): List<BaseDataUiModel> {
        val groupedWidgets = widgets.groupBy { it.widgetType } as MutableMap
        return mutableListOf<BaseDataUiModel>().apply {
            val lineGraphData = async { groupedWidgets.getWidgetDataByType<LineGraphDataUiModel>(WidgetType.LINE_GRAPH) }
            val announcementData = async { groupedWidgets.getWidgetDataByType<AnnouncementDataUiModel>(WidgetType.ANNOUNCEMENT) }
            val cardData = async { groupedWidgets.getWidgetDataByType<CardDataUiModel>(WidgetType.CARD) }
            val progressData = async { groupedWidgets.getWidgetDataByType<ProgressDataUiModel>(WidgetType.PROGRESS) }
            val carouselData = async { groupedWidgets.getWidgetDataByType<CarouselDataUiModel>(WidgetType.CAROUSEL) }
            val postData = async { groupedWidgets.getWidgetDataByType<PostListDataUiModel>(WidgetType.POST_LIST) }
            val tableData = async { groupedWidgets.getWidgetDataByType<TableDataUiModel>(WidgetType.TABLE) }
            val pieChartData = async { groupedWidgets.getWidgetDataByType<PieChartDataUiModel>(WidgetType.PIE_CHART) }
            val barChartData = async { groupedWidgets.getWidgetDataByType<BarChartDataUiModel>(WidgetType.BAR_CHART) }
            val multiLineGraphData = async { groupedWidgets.getWidgetDataByType<MultiLineGraphDataUiModel>(WidgetType.MULTI_LINE_GRAPH) }

            val concattedResult = concatAllResult(
                    lineGraphData.await(),
                    announcementData.await(),
                    cardData.await(),
                    progressData.await(),
                    carouselData.await(),
                    postData.await(),
                    tableData.await(),
                    pieChartData.await(),
                    barChartData.await(),
                    multiLineGraphData.await())
            addAll(concattedResult)
        }
    }

    private fun <T> concatAllResult(vararg lists: List<T>): List<T> {
        return mutableListOf<T>().apply {
            lists.forEach {
                addAll(it)
            }
        }
    }

    private suspend inline fun <reified D : BaseDataUiModel> MutableMap<String, List<BaseWidgetUiModel<*>>>.getWidgetDataByType(widgetType: String): List<BaseDataUiModel> {
        val widgetList = this[widgetType]
        return try {
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
    }

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

    private fun shouldRemoveWidgetInitially(widget: BaseWidgetUiModel<*>, widgetData: BaseDataUiModel): Boolean {
        return !widgetData.showWidget || (!widget.isShowEmpty && widgetData.shouldRemove())
    }

    private fun removeEmptySections(newWidgetList: MutableList<BaseWidgetUiModel<*>>, removedWidgetIndex: Int) {
        val previousWidget = newWidgetList.getOrNull(removedWidgetIndex - 1)
        val widgetReplacement = newWidgetList.getOrNull(removedWidgetIndex)
        if ((widgetReplacement == null || widgetReplacement is SectionWidgetUiModel) && previousWidget is SectionWidgetUiModel) {
            newWidgetList.removeAt(removedWidgetIndex - 1)
        }
    }

    private suspend fun getCardData(widgets: List<BaseWidgetUiModel<*>>): List<CardDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        val params = GetCardDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getCardDataUseCase.get().params = params
        return getDataFromUseCase(getCardDataUseCase.get())
    }

    private suspend fun getLineGraphData(widgets: List<BaseWidgetUiModel<*>>): List<LineGraphDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        val params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getLineGraphDataUseCase.get().params = params
        return getDataFromUseCase(getLineGraphDataUseCase.get())
    }

    private suspend fun getProgressData(widgets: List<BaseWidgetUiModel<*>>): List<ProgressDataUiModel> {
        widgets.setLoading()
        val today = DateTimeUtil.format(Date().time, DATE_FORMAT)
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        val params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
        getProgressDataUseCase.get().params = params
        return getDataFromUseCase(getProgressDataUseCase.get())
    }

    private suspend fun getPostData(widgets: List<BaseWidgetUiModel<*>>): List<PostListDataUiModel> {
        widgets.setLoading()
        val dataKeys: List<Pair<String, String>> = widgets.filterIsInstance<PostListWidgetUiModel>().map {
            val postFilter = it.postFilter.find { filter -> filter.isSelected }?.value.orEmpty()
            return@map Pair(it.dataKey, postFilter)
        }
        val params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getPostDataUseCase.get().params = params
        return getDataFromUseCase(getPostDataUseCase.get())
    }

    private suspend fun getCarouselData(widgets: List<BaseWidgetUiModel<*>>): List<CarouselDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        val params = GetCarouselDataUseCase.getRequestParams(dataKeys)
        getCarouselDataUseCase.get().params = params
        return getDataFromUseCase(getCarouselDataUseCase.get())
    }

    private suspend fun getTableData(widgets: List<BaseWidgetUiModel<*>>): List<TableDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<TableWidgetUiModel>(widgets)
        val params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getTableDataUseCase.get().params = params
        return getDataFromUseCase(getTableDataUseCase.get())
    }

    private suspend fun getPieChartData(widgets: List<BaseWidgetUiModel<*>>): List<PieChartDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<PieChartWidgetUiModel>(widgets)
        val params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getPieChartDataUseCase.get().params = params
        return getDataFromUseCase(getPieChartDataUseCase.get())
    }

    private suspend fun getBarChartData(widgets: List<BaseWidgetUiModel<*>>): List<BarChartDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<BarChartWidgetUiModel>(widgets)
        val params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        getBarChartDataUseCase.get().params = params
        return getDataFromUseCase(getBarChartDataUseCase.get())
    }

    private suspend fun getMultiLineGraphData(widgets: List<BaseWidgetUiModel<*>>): List<MultiLineGraphDataUiModel> {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<MultiLineGraphWidgetUiModel>(widgets)
        val params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)
        getMultiLineGraphUseCase.get().params = params
        return getMultiLineGraphUseCase.get().executeOnBackground()
    }

    private suspend fun getAnnouncementData(widgets: List<BaseWidgetUiModel<*>>): List<AnnouncementDataUiModel> {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<AnnouncementWidgetUiModel>(widgets)
        val params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)
        getAnnouncementUseCase.get().params = params
        return getAnnouncementUseCase.get().executeOnBackground()
    }

    private fun List<BaseWidgetUiModel<*>>.setLoading() {
        forEach {
            it.isLoading = true
            it.isLoaded = true
        }
    }

}