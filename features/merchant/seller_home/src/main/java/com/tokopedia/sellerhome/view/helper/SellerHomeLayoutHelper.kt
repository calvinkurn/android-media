package com.tokopedia.sellerhome.view.helper

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.WidgetHeight
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.domain.usecase.BaseGqlUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetAnnouncementDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetBarChartDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCardDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetCarouselDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetLineGraphDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMilestoneDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetMultiLineGraphUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetPieChartDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetPostDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetProgressDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetRecommendationDataUseCase
import com.tokopedia.sellerhomecommon.domain.usecase.GetTableDataUseCase
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LastUpdatedDataInterface
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.Utils
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 20/11/21
 */

class SellerHomeLayoutHelper @Inject constructor(
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
    private val getMilestoneDataUseCase: Lazy<GetMilestoneDataUseCase>,
    private val remoteConfig: Lazy<SellerHomeRemoteConfig>,
    private val dispatcher: CoroutineDispatchers
) {

    private var startWidgetCustomMetricTag = MutableLiveData<String>()
    private var stopWidgetType = MutableLiveData<String>()
    private var dynamicParameter: DynamicParameterModel = DynamicParameterModel()

    fun init(
        startWidgetLiveData: MutableLiveData<String>,
        stopWidgetLiveData: MutableLiveData<String>,
        dynamicParameter: DynamicParameterModel
    ) {
        this.startWidgetCustomMetricTag = startWidgetLiveData
        this.stopWidgetType = stopWidgetLiveData
        this.dynamicParameter = dynamicParameter
    }

    /**
     * Return flow that will get predicted widget that should be shown first in the screen and load the widgets's data
     * After that, map the original widgets to initial loaded widgets by their ids
     *
     * @param   widgets         original widget list
     * @param   deviceHeightDp  expected screen height to determine initial widgets to be loaded
     * @return  flow that emit mapped/combined widget layouts
     */
    suspend fun getInitialWidget(
        widgets: List<BaseWidgetUiModel<*>>,
        deviceHeightDp: Float,
        isFromCache: Boolean
    ): Flow<List<BaseWidgetUiModel<*>>> {
        val widgetFlow = flow { emit(widgets) }
        val predictedInitialWidgetFlow = getPredictedInitialWidget(
            widgets, deviceHeightDp, isFromCache
        )
        return widgetFlow.combine(predictedInitialWidgetFlow) { widgetsFromFlow, initialWidgets ->
            widgetsFromFlow.map { widget ->
                initialWidgets.find { it.id == widget.id } ?: widget
            }.filter { !it.isNeedToBeRemoved }
        }
    }

    /**
     * Return flow that will calculate height to predict which widgets that will load their data initially
     *
     * @param   widgetList      original widget list
     * @param   deviceHeightDp  expected screen height to determine initial widgets to be loaded
     * @return  flow that will return initial widgets layout which has its data loaded
     */
    private fun getPredictedInitialWidget(
        widgetList: List<BaseWidgetUiModel<*>>,
        deviceHeightDp: Float,
        isFromCache: Boolean
    ): Flow<List<BaseWidgetUiModel<*>>> {
        var remainingHeight = deviceHeightDp
        var hasCardCalculated = false
        val newWidgetList = widgetList.map { widget ->
            val requestedHeight = WidgetHeight.getWidgetHeight(widget.widgetType)
            if (remainingHeight > 0f) {
                widget.apply { isLoading = true }
            }
            return@map widget.also {
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
        return getLoadedInitialWidgetData(newWidgetList, isFromCache)
    }

    /**
     * Return flow that will get all required initial widget data by checking which widgets that still need their data loaded
     *
     * @param   widgetList  predicted initial widget list
     * @return  flow that return list of all loaded widget
     */
    private fun getLoadedInitialWidgetData(
        widgetList: List<BaseWidgetUiModel<*>>,
        isFromCache: Boolean
    ): Flow<List<BaseWidgetUiModel<*>>> {
        val loadedWidgetList = widgetList.filter { it.isLoading }
            .map {
                if (it.widgetType == WidgetType.SECTION) {
                    it.isLoaded = true
                }
                return@map it
            }
        return getWidgetsData(loadedWidgetList, isFromCache)
    }

    /**
     * Return flow that will load required data from each expected widget and combine those to single widget list
     *
     * @param   widgets     list of widgets which their data need to be loaded
     * @return  flow that return list of combined widget list that has their data loaded
     */
    private fun getWidgetsData(
        widgets: List<BaseWidgetUiModel<*>>,
        isFromCache: Boolean
    ): Flow<List<BaseWidgetUiModel<*>>> {
        val groupedWidgets = widgets.groupBy { it.widgetType } as MutableMap
        val lineGraphDataFlow = groupedWidgets.getWidgetDataByType<LineGraphDataUiModel>(
            WidgetType.LINE_GRAPH,
            isFromCache
        )
        val announcementDataFlow = groupedWidgets.getWidgetDataByType<AnnouncementDataUiModel>(
            WidgetType.ANNOUNCEMENT,
            isFromCache
        )
        val cardDataFlow = groupedWidgets.getWidgetDataByType<CardDataUiModel>(
            WidgetType.CARD,
            isFromCache
        )
        val progressDataFlow = groupedWidgets.getWidgetDataByType<ProgressDataUiModel>(
            WidgetType.PROGRESS,
            isFromCache
        )
        val carouselDataFlow = groupedWidgets.getWidgetDataByType<CarouselDataUiModel>(
            WidgetType.CAROUSEL,
            isFromCache
        )
        val postDataFlow = groupedWidgets.getWidgetDataByType<PostListDataUiModel>(
            WidgetType.POST_LIST,
            isFromCache
        )
        val tableDataFlow = groupedWidgets.getWidgetDataByType<TableDataUiModel>(
            WidgetType.TABLE,
            isFromCache
        )
        val pieChartDataFlow = groupedWidgets.getWidgetDataByType<PieChartDataUiModel>(
            WidgetType.PIE_CHART,
            isFromCache
        )
        val barChartDataFlow = groupedWidgets.getWidgetDataByType<BarChartDataUiModel>(
            WidgetType.BAR_CHART,
            isFromCache
        )
        val multiLineGraphDataFlow = groupedWidgets.getWidgetDataByType<MultiLineGraphDataUiModel>(
            WidgetType.MULTI_LINE_GRAPH,
            isFromCache
        )
        val recommendationDataFlow = groupedWidgets.getWidgetDataByType<RecommendationDataUiModel>(
            WidgetType.RECOMMENDATION,
            isFromCache
        )
        val milestoneDataFlow = groupedWidgets.getWidgetDataByType<MilestoneDataUiModel>(
            WidgetType.MILESTONE,
            isFromCache
        )

        return combine(
            lineGraphDataFlow, announcementDataFlow, cardDataFlow, progressDataFlow,
            carouselDataFlow, postDataFlow, tableDataFlow, pieChartDataFlow,
            barChartDataFlow, multiLineGraphDataFlow, recommendationDataFlow, milestoneDataFlow
        ) { widgetDataList ->
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
    private inline fun <reified D : BaseDataUiModel> MutableMap<String, List<BaseWidgetUiModel<*>>>.getWidgetDataByType(
        widgetType: String,
        isFromCache: Boolean
    ): Flow<List<BaseDataUiModel>> {
        return flow {
            val widgetList = this@getWidgetDataByType[widgetType]
            val widgetDataList =
                try {
                    widgetList?.let {
                        when (widgetType) {
                            WidgetType.LINE_GRAPH -> getLineGraphData(it, isFromCache)
                            WidgetType.ANNOUNCEMENT -> getAnnouncementData(it)
                            WidgetType.CARD -> getCardData(it)
                            WidgetType.PROGRESS -> getProgressData(it)
                            WidgetType.CAROUSEL -> getCarouselData(it)
                            WidgetType.POST_LIST -> getPostData(it)
                            WidgetType.TABLE -> getTableData(it)
                            WidgetType.PIE_CHART -> getPieChartData(it)
                            WidgetType.BAR_CHART -> getBarChartData(it)
                            WidgetType.MULTI_LINE_GRAPH -> getMultiLineGraphData(it)
                            WidgetType.RECOMMENDATION -> getRecommendationData(it)
                            WidgetType.MILESTONE -> getMilestoneData(it)
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
                stopWidgetType.value = widgetType
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
    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.mapToWidgetModel(
        widgets: List<BaseWidgetUiModel<*>>
    ): List<BaseWidgetUiModel<*>> {
        val newWidgetList = widgets.toMutableList()
        forEach { widgetData ->
            hideLastUpdatedRefreshButton(widgetData)
            newWidgetList.indexOfFirst {
                it.dataKey == widgetData.dataKey
            }.takeIf { it > -1 }?.let { index ->
                val widget = newWidgetList.getOrNull(index)
                if (widget is W) {
                    val copiedWidget = widget.copyWidget()
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

    private fun <D> hideLastUpdatedRefreshButton(widgetData: D) {
        (widgetData as? LastUpdatedDataInterface)?.lastUpdated?.needToUpdated = false
    }

    /**
     * Determine whether the widget need to be removed from its original list, which depends on its data value.
     * This will only take cloud values in consideration, not cache.
     *
     * @param   widget      widget to check
     * @param   widgetData  the widget's data
     * @return  is the widget should be removed
     */
    private fun shouldRemoveWidgetInitially(
        widget: BaseWidgetUiModel<*>,
        widgetData: BaseDataUiModel
    ): Boolean {
        return !widgetData.showWidget || (!widget.isShowEmpty && widgetData.isWidgetEmpty())
    }

    private fun removeEmptySections(
        newWidgetList: MutableList<BaseWidgetUiModel<*>>,
        removedWidgetIndex: Int
    ) {
        val previousWidgetIndex =
            newWidgetList.take(removedWidgetIndex).indexOfLast { !it.isNeedToBeRemoved }
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
        val useCase = getCardDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_CARD_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getLineGraphData(
        widgets: List<BaseWidgetUiModel<*>>,
        shouldUseCache: Boolean
    ): List<LineGraphDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        val params = GetLineGraphDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        val useCase = getLineGraphDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_LINE_GRAPH_TRACE
        }
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getProgressData(widgets: List<BaseWidgetUiModel<*>>): List<ProgressDataUiModel> {
        widgets.setLoading()
        val today = DateTimeUtil.format(Date().time, SellerHomeViewModel.DATE_FORMAT)
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        val params = GetProgressDataUseCase.getRequestParams(today, dataKeys)
        val useCase = getProgressDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_PROGRESS_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getPostData(widgets: List<BaseWidgetUiModel<*>>): List<PostListDataUiModel> {
        widgets.setLoading()
        val dataKeys: List<TableAndPostDataKey> =
            widgets.filterIsInstance<PostListWidgetUiModel>().map {
                val postFilter = it.postFilter.find { filter -> filter.isSelected }
                val postFilters = postFilter?.value.orEmpty()
                return@map TableAndPostDataKey(it.dataKey, postFilters, it.maxData, it.maxDisplay)
            }
        val params = GetPostDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        val useCase = getPostDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_POST_LIST_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getCarouselData(widgets: List<BaseWidgetUiModel<*>>): List<CarouselDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        val params = GetCarouselDataUseCase.getRequestParams(dataKeys)
        val useCase = getCarouselDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_CAROUSEL_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getTableData(widgets: List<BaseWidgetUiModel<*>>): List<TableDataUiModel> {
        widgets.setLoading()
        val dataKeys: List<TableAndPostDataKey> =
            widgets.filterIsInstance<TableWidgetUiModel>().map {
                val postFilter = it.tableFilters.find { filter -> filter.isSelected }
                val postFilters = postFilter?.value.orEmpty()
                return@map TableAndPostDataKey(it.dataKey, postFilters, it.maxData, it.maxDisplay)
            }
        val params = GetTableDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        val useCase = getTableDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_TABLE_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getPieChartData(widgets: List<BaseWidgetUiModel<*>>): List<PieChartDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<PieChartWidgetUiModel>(widgets)
        val params = GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        val useCase = getPieChartDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_PIE_CHART_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getBarChartData(widgets: List<BaseWidgetUiModel<*>>): List<BarChartDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<BarChartWidgetUiModel>(widgets)
        val params = GetBarChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
        val useCase = getBarChartDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_BAR_CHART_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getMultiLineGraphData(widgets: List<BaseWidgetUiModel<*>>): List<MultiLineGraphDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<MultiLineGraphWidgetUiModel>(widgets)
        val params = GetMultiLineGraphUseCase.getRequestParams(dataKeys, dynamicParameter)
        val useCase = getMultiLineGraphUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_MULTI_LINE_GRAPH_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getRecommendationData(widgets: List<BaseWidgetUiModel<*>>): List<RecommendationDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<RecommendationWidgetUiModel>(widgets)
        val params = GetRecommendationDataUseCase.createParams(dataKeys)
        val useCase = getRecommendationUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_RECOMMENDATION_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getMilestoneData(widgets: List<BaseWidgetUiModel<*>>): List<MilestoneDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<MilestoneWidgetUiModel>(widgets)
        val params = GetMilestoneDataUseCase.createParams(dataKeys)
        val useCase = getMilestoneDataUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_MILESTONE_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private suspend fun getAnnouncementData(widgets: List<BaseWidgetUiModel<*>>): List<AnnouncementDataUiModel> {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<AnnouncementWidgetUiModel>(widgets)
        val params = GetAnnouncementDataUseCase.createRequestParams(dataKeys)
        val useCase = getAnnouncementUseCase.get()
        useCase.params = params
        withContext(dispatcher.main) {
            startWidgetCustomMetricTag.value =
                SellerHomePerformanceMonitoringConstant.SELLER_HOME_ANNOUNCEMENT_TRACE
        }
        val shouldUseCache = remoteConfig.get().isSellerHomeDashboardCachingEnabled()
                && useCase.isFirstLoad
        return getDataFromUseCase(useCase, shouldUseCache)
    }

    private fun List<BaseWidgetUiModel<*>>.setLoading() {
        forEach {
            it.isLoading = true
            it.isLoaded = true
        }
    }

    private suspend fun <T : Any> getDataFromUseCase(
        useCase: BaseGqlUseCase<T>,
        useCache: Boolean
    ): T {
        useCase.isFirstLoad = false
        useCase.setUseCache(useCache)
        return withContext(dispatcher.io) {
            try {
                useCase.executeOnBackground()
            } catch (e: Exception) {
                useCase.setUseCache(false)
                useCase.executeOnBackground()
            }
        }
    }
}