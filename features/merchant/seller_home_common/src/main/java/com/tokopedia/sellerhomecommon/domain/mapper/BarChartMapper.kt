package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.charts.common.ChartColor
import com.tokopedia.sellerhomecommon.domain.model.*
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 10/07/20
 */

class BarChartMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(barChartDataList: List<BarChartWidgetDataModel>): List<BarChartDataUiModel> {
        return barChartDataList.map {
            BarChartDataUiModel(
                    dataKey = it.dataKey,
                    error = it.errorMsg,
                    chartData = BarChartUiModel(
                            metrics = getBarChartMetrics(it.data.metrics),
                            xAxis = getAxis(it.data.axes.xLabel),
                            yAxis = getAxis(it.data.axes.yLabel).distinctBy { axis -> axis.value },
                            summary = mapBarChartSummary(it.data.summary)
                    )
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
                    barHexColor = ChartColor.getHexColorByIndex(i)
            )
        }
    }

    private fun getAxis(data: List<BarChartValueModel>): List<BarChartAxisUiModel> {
        return data.map {
            BarChartAxisUiModel(it.value, it.valueFmt)
        }
    }
}