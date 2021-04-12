package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.charts.common.ChartColor
import com.tokopedia.sellerhomecommon.domain.model.*
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 10/07/20
 */

class BarChartMapper @Inject constructor(): BaseResponseMapper<GetBarChartDataResponse, List<BarChartDataUiModel>> {

    override fun mapRemoteDataToUiData(response: GetBarChartDataResponse, isFromCache: Boolean): List<BarChartDataUiModel> {
        return response.fetchBarChartWidgetData.data.map {
            BarChartDataUiModel(
                    dataKey = it.dataKey,
                    error = it.errorMsg,
                    chartData = BarChartUiModel(
                            metrics = getBarChartMetrics(it.data.metrics),
                            xAxis = getAxis(it.data.axes.xLabel),
                            yAxis = getAxis(it.data.axes.yLabel).distinctBy { axis -> axis.value },
                            summary = mapBarChartSummary(it.data.summary)
                    ),
                    isFromCache = isFromCache,
                    showWidget = it.showWidget.orFalse()
            )
        }
    }

    private fun mapBarChartSummary(summary: ChartSummaryModel): ChartSummaryUiModel {
        return ChartSummaryUiModel(
                diffPercentage = summary.diffPercentage,
                diffPercentageFmt = summary.diffPercentageFmt,
                value = summary.value,
                valueFmt = summary.valueFmt
        )
    }

    private fun getBarChartMetrics(metrics: List<BarChartMetricModel>): List<BarChartMetricsUiModel> {
        return metrics.mapIndexed { i, metric ->
            BarChartMetricsUiModel(
                    value = getAxis(metric.value),
                    title = metric.name,
                    barHexColor = getBarHexColor(metric)
            )
        }
    }

    private fun getAxis(data: List<BarChartValueModel>): List<BarChartAxisUiModel> {
        return data.map {
            BarChartAxisUiModel(it.value, it.valueFmt)
        }
    }

    /**
     * Here is the sample response from BE.
     * So, to get bar chart color, we just take the color of the first item (index 0)
        "value": [
            {
                "value": 0,
                "valueFmt": "0",
                "color": "# 4FBA68"
            },
            {
                "value": 0,
                "valueFmt": "0",
                "color": "# 4FBA68"
            },
            {
                "value": 0,
                "valueFmt": "0",
                "color": "# 4FBA68"
            }
        ]
     */
    private fun getBarHexColor(metric: BarChartMetricModel): String {
        return metric.value.getOrNull(0)?.hexColor ?: ChartColor.DEFAULT_BAR_COLOR
    }
}