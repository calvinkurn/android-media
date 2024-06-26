package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.WidgetSource
import com.tokopedia.sellerhomecommon.domain.model.ParamCommonWidgetModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentData
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetMultiComponentDetailDataUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getMultiLineGraphUseCase: Lazy<GetMultiLineGraphUseCase>,
    private val getPieChartDataUseCase: Lazy<GetPieChartDataUseCase>
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + SupervisorJob()

    suspend fun executeOnBackground(
        tab: MultiComponentTab,
        dynamicParameter: ParamCommonWidgetModel
    ): List<MultiComponentData> {
        val dynamicParameterMultiComponent = dynamicParameter.copy(
            widgetSource =
            WidgetSource.MULTI_COMPONENT_WIDGET_SOURCE
        )
        return withContext(dispatcher.io) {
            val dataDeferred = tab.components.map {
                async {
                    val componentData = getComponentDataAsync(it, dynamicParameterMultiComponent)

                    when (it.data) {
                        is PieChartWidgetUiModel -> {
                            return@async it.copy(
                                data = it.data.copy(
                                    data = componentData as? PieChartDataUiModel,
                                    isShowEmpty = (componentData as? PieChartDataUiModel)
                                        ?.isWidgetEmpty()
                                        .orFalse()
                                )
                            )
                        }
                        is MultiLineGraphWidgetUiModel -> {
                            return@async it.copy(
                                data = it.data.copy(
                                    data = componentData as? MultiLineGraphDataUiModel,
                                    isShowEmpty = (componentData as? MultiLineGraphDataUiModel)
                                        ?.isWidgetEmpty()
                                        .orFalse()
                                )
                            )
                        }
                        else -> {
                            it
                        }
                    }
                }.await()
            }

            dataDeferred
        }
    }

    private suspend fun getComponentDataAsync(
        component: MultiComponentData,
        dynamicParameter: ParamCommonWidgetModel
    ): BaseDataUiModel? {
        return when (component.componentType) {
            WidgetType.MULTI_LINE_GRAPH -> getMultiLineGraphWidgetData(
                listOf(component.dataKey),
                dynamicParameter
            )
            WidgetType.PIE_CHART -> getPieChartWidgetData(
                listOf(component.dataKey),
                dynamicParameter
            )
            else -> null
        }
    }

    private suspend fun getPieChartWidgetData(
        dataKeys: List<String>,
        dynamicParameter: ParamCommonWidgetModel
    ): BaseDataUiModel? {
        return try {
            getPieChartDataUseCase.get().params =
                GetPieChartDataUseCase.getRequestParams(dataKeys, dynamicParameter)
            getPieChartDataUseCase.get().executeOnBackground().firstOrNull()
        } catch (e: Throwable) {
            null
        }
    }

    private suspend fun getMultiLineGraphWidgetData(
        dataKeys: List<String>,
        dynamicParameter: ParamCommonWidgetModel
    ): BaseDataUiModel? {
        return try {

            val useCase = getMultiLineGraphUseCase.get()
            useCase.params = GetMultiLineGraphUseCase.getRequestParams(
                dataKey = dataKeys,
                dynamicParam = dynamicParameter
            )
            useCase.executeOnBackground().firstOrNull()
        } catch (e: Throwable) {
            null
        }
    }
}