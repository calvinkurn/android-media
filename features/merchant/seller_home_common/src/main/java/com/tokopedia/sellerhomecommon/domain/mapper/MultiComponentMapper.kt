package com.tokopedia.sellerhomecommon.domain.mapper

import android.graphics.Color
import com.tokopedia.charts.common.ChartColor
import com.tokopedia.charts.model.AxisLabel
import com.tokopedia.charts.model.BarChartMetricValue
import com.tokopedia.charts.model.StackedBarChartData
import com.tokopedia.charts.model.StackedBarChartMetric
import com.tokopedia.charts.model.StackedBarChartMetricValue
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.domain.model.GetMultiComponentDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentData
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiComponentTab
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.BarMultiComponentLegendModel
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.BarMultiComponentUiModel
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.MultiComponentItemUiModel
import javax.inject.Inject

class MultiComponentMapper @Inject constructor() :
    BaseResponseMapper<GetMultiComponentDataResponse, List<MultiComponentDataUiModel>> {

    override fun mapRemoteDataToUiData(
        response: GetMultiComponentDataResponse,
        isFromCache: Boolean
    ): List<MultiComponentDataUiModel> {
        return listOf(
            MultiComponentDataUiModel(
                dataKey = response.fetchMultiComponentWidget.dataKey,
                tabs = response.fetchMultiComponentWidget.tabs.map {
                    MultiComponentTab(
                        id = it.id,
                        title = it.title,
                        components = it.components.map { component ->
                            MultiComponentData(
                                componentType = component.componentType,
                                dataKey = component.dataKey,
                                configuration = component.configuration,
                                metricParam = component.metricsParam,
                                data = null
                            )
                        },
                        isSelected = false,
                        isLoaded = false,
                        isError = it.components.all { multiComponent -> !multiComponent.error }
                    )
                }
            )
        )
    }

    fun mapToMultiComponentData(data: BaseDataUiModel): MultiComponentItemUiModel? {
        return when (data) {
            is BarChartDataUiModel -> mapBarChartUiModel(data)
            else -> null
        }
    }

    fun getDummyData(): MultiComponentItemUiModel {
        return BarMultiComponentUiModel(
            title = "Detail potensi penjualan",
            description = "Bebas Ongkir bantu tingkatkan 5% potensi penjualan",
            stackedBarChartData = StackedBarChartData(
                yAxis = listOf(
                    AxisLabel(0f, ""),
                    AxisLabel(20f, "20jt"),
                    AxisLabel(40f, "40jt"),
                    AxisLabel(60f, "60jt"),
                    AxisLabel(80f, "80jt"),
                    AxisLabel(100f, "100jt")
                ),
                xAxisLabels = listOf(),
                metrics = listOf(
                    StackedBarChartMetric(
                        values = listOf(
                            StackedBarChartMetricValue(
                                values = listOf(
                                    BarChartMetricValue(
                                        value = 4.25f,
                                        yLabel = "",
                                        xLabel = "",
                                        barColor = "#C9FDE0"
                                    ),
                                    BarChartMetricValue(
                                        value = 1.25f,
                                        yLabel = "",
                                        xLabel = "",
                                        barColor = "#4FE397"
                                    ),
                                    BarChartMetricValue(
                                        value = 1f,
                                        yLabel = "",
                                        xLabel = "",
                                        barColor = "#00AA5B"
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            legends = listOf(
                BarMultiComponentLegendModel(
                    Color.parseColor("#00AA5B"),
                    "Bebas Ongkir",
                    "6.029"
                ),
                BarMultiComponentLegendModel(
                    Color.parseColor("#4FE397"),
                    "Bebas Ongkir Dilayani Tokopedia",
                    "10.053"
                ),
                BarMultiComponentLegendModel(
                    Color.parseColor("#C9FDE0"),
                    "Non Bebas Ongkir",
                    "4.250"
                )
            )
        )
    }

    private fun mapBarChartUiModel(data: BarChartDataUiModel): BarMultiComponentUiModel {
        return BarMultiComponentUiModel(
                    title = "Detail potensi penjualan",
                    description = "Bebas Ongkir bantu tingkatkan 5% potensi penjualan",
                    stackedBarChartData = StackedBarChartData(
                        yAxis = listOf(
                            AxisLabel(0f, ""),
                            AxisLabel(20f, "20jt"),
                            AxisLabel(40f, "40jt"),
                            AxisLabel(60f, "60jt"),
                            AxisLabel(80f, "80jt"),
                            AxisLabel(100f, "100jt")
                        ),
                        xAxisLabels = listOf(),
                        metrics = listOf(
                            StackedBarChartMetric(
                                values = listOf(
                                    StackedBarChartMetricValue(
                                        values = listOf(
                                            BarChartMetricValue(
                                                value = 4.25f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#C9FDE0"
                                            ),
                                            BarChartMetricValue(
                                                value = 1.25f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#4FE397"
                                            ),
                                            BarChartMetricValue(
                                                value = 1f,
                                                yLabel = "",
                                                xLabel = "",
                                                barColor = "#00AA5B"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    legends = listOf(
                        BarMultiComponentLegendModel(
                            Color.parseColor("#00AA5B"),
                            "Bebas Ongkir",
                            "6.029"
                        ),
                        BarMultiComponentLegendModel(
                            Color.parseColor("#4FE397"),
                            "Bebas Ongkir Dilayani Tokopedia",
                            "10.053"
                        ),
                        BarMultiComponentLegendModel(
                            Color.parseColor("#C9FDE0"),
                            "Non Bebas Ongkir",
                            "4.250"
                        )
                    )
                )
        return BarMultiComponentUiModel(
            title = "Test doang TODO",
            description = data.error,
            stackedBarChartData = StackedBarChartData(
                yAxis = data.chartData.yAxis.map {
                    AxisLabel(
                        it.value.toFloat(),
                        it.valueFmt
                    )
                },
                xAxisLabels = data.chartData.xAxis.map {
                    AxisLabel(
                        it.value.toFloat(),
                        it.valueFmt
                    )
                },
                metrics = data.chartData.metrics.map {
                    StackedBarChartMetric(
                        title = it.title,
                        values = listOf(
                            StackedBarChartMetricValue(
                                values = it.value.map { metricValue ->
                                    BarChartMetricValue(
                                        value = metricValue.value.toFloat(),
                                        yLabel = "",
                                        xLabel = ""
                                    )
                                }
                            )
                        )
                    )
                }
            ),
            legends = data.chartData.metrics.map {
                BarMultiComponentLegendModel(
                    color = Color.parseColor(ChartColor.DMS_DEFAULT_BAR_COLOR),
                    title = it.title,
                    value = it.value.firstOrNull()?.value.orZero().toString()
                )
            }
        )
    }
}
