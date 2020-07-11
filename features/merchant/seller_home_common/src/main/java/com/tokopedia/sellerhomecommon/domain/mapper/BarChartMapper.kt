package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.BarChartMetricModel
import com.tokopedia.sellerhomecommon.domain.model.BarChartValueModel
import com.tokopedia.sellerhomecommon.domain.model.BarChartWidgetDataModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartAxisUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartMetricsUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartUiModel
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
                            yAxis = getAxis(it.data.axes.yLabel)
                    )
            )
        }
    }

    private fun getBarChartMetrics(metrics: List<BarChartMetricModel>): List<BarChartMetricsUiModel> {
        return metrics.map {
            BarChartMetricsUiModel(
                    value = getAxis(it.value),
                    title = it.name
            )
        }
    }

    private fun getAxis(data: List<BarChartValueModel>): List<BarChartAxisUiModel> {
        return data.map {
            BarChartAxisUiModel(it.value, it.valueFmt)
        }
    }
}