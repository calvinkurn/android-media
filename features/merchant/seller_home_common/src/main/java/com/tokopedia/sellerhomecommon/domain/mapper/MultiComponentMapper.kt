package com.tokopedia.sellerhomecommon.domain.mapper

import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.asLowerCase
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.domain.model.FetchMultiComponentResponse
import com.tokopedia.sellerhomecommon.domain.model.MultiComponent
import com.tokopedia.sellerhomecommon.domain.model.WidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentData
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import javax.inject.Inject

class MultiComponentMapper @Inject constructor(
    private val gson: Gson,
    private val tooltipMapper: TooltipMapper
) :
    BaseResponseMapper<FetchMultiComponentResponse, List<MultiComponentDataUiModel>> {

    override fun mapRemoteDataToUiData(
        response: FetchMultiComponentResponse,
        isFromCache: Boolean
    ): List<MultiComponentDataUiModel> {
        return response.fetchMultiComponentWidget.data.map { widgets ->
            MultiComponentDataUiModel(
                dataKey = widgets.dataKey,
                showWidget = widgets.showWidget,
                tabs = widgets.tabs.mapIndexed { index, it ->
                    MultiComponentTab(
                        id = it.id,
                        title = it.title,
                        ticker = it.ticker,
                        components = it.components.map { component ->
                            val tabConfig = getTabConfig(component.configuration)
                            MultiComponentData(
                                componentType = component.componentType,
                                dataKey = component.dataKey,
                                data = mapToWidgetUiModel(component, tabConfig, isFromCache)
                            )
                        },
                        isLoaded = false,
                        isError = widgets.error
                    )
                }
            )
        }
    }

    private fun getTabConfig(configStr: String): WidgetModel {
        return gson.fromJson(configStr, WidgetModel::class.java)
    }

    private fun mapToWidgetUiModel(
        data: MultiComponent,
        tabConfig: WidgetModel,
        isFromCache: Boolean
    ): BaseWidgetUiModel<*>? {
        return when (data.componentType.asLowerCase()) {
            WidgetType.PIE_CHART.asLowerCase() -> {
                mapToPieChartUiModel(data, tabConfig, isFromCache)
            }
            WidgetType.MULTI_LINE_GRAPH.asLowerCase() -> {
                mapToMultiTrendLineUiData(data, tabConfig, isFromCache)
            }
            else -> {
                null
            }
        }
    }

    private fun mapToPieChartUiModel(
        data: MultiComponent,
        tabConfig: WidgetModel,
        isFromCache: Boolean
    ): PieChartWidgetUiModel {
        return PieChartWidgetUiModel(
            id = tabConfig.id.toString(),
            widgetType = data.componentType,
            title = tabConfig.title ?: "",
            subtitle = tabConfig.subtitle ?: "",
            tooltip = tooltipMapper.mapRemoteModelToUiModel(tabConfig.tooltip),
            tag = tabConfig.tag ?: "",
            appLink = tabConfig.appLink ?: "",
            dataKey = data.dataKey,
            ctaText = tabConfig.ctaText ?: "",
            gridSize = tabConfig.gridSize ?: 1,
            isShowEmpty = tabConfig.isShowEmpty ?: false,
            data = null,
            isLoaded = false,
            isLoading = true,
            isFromCache = isFromCache,
            emptyState = tabConfig.emptyStateModel.mapToUiModel(),
            useRealtime = tabConfig.useRealtime
        )
    }

    private fun mapToMultiTrendLineUiData(
        data: MultiComponent,
        tabConfig: WidgetModel,
        isFromCache: Boolean
    ): MultiLineGraphWidgetUiModel {
        return MultiLineGraphWidgetUiModel(
            id = tabConfig.id.toString(),
            widgetType = data.componentType,
            title = tabConfig.title ?: "",
            subtitle = tabConfig.subtitle ?: "",
            tooltip = tooltipMapper.mapRemoteModelToUiModel(tabConfig.tooltip),
            tag = tabConfig.tag ?: "",
            appLink = tabConfig.appLink ?: "",
            dataKey = data.dataKey,
            ctaText = tabConfig.ctaText ?: "",
            gridSize = tabConfig.gridSize ?: 1,
            isShowEmpty = tabConfig.isShowEmpty ?: false,
            data = null,
            isLoaded = false,
            isLoading = true,
            isFromCache = isFromCache,
            emptyState = tabConfig.emptyStateModel.mapToUiModel(),
            useRealtime = tabConfig.useRealtime,
            isComparePeriodOnly = tabConfig.isComparePeriodOnly
        )
    }
}
