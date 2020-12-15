package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.common.const.MetricsType
import com.tokopedia.sellerhomecommon.domain.model.LineModel
import com.tokopedia.sellerhomecommon.domain.model.MultiTrendLineMetricModel
import com.tokopedia.sellerhomecommon.domain.model.MultiTrendlineWidgetDataModel
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 26/10/20
 */

class MultiLineGraphMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(items: List<MultiTrendlineWidgetDataModel>?, isFromCache: Boolean): List<MultiLineGraphDataUiModel> {
        return items.orEmpty()
                .filter { !it.dataKey.isNullOrBlank() }
                .map {
                    MultiLineGraphDataUiModel(
                            dataKey = it.dataKey.orEmpty(),
                            error = it.errorMsg.orEmpty(),
                            isFromCache = isFromCache,
                            metrics = getMetrics(it.multiTrendlineData?.metrics.orEmpty())
                    )
                }
    }

    private fun getMetrics(metrics: List<MultiTrendLineMetricModel>): List<MultiLineMetricUiModel> {
        return metrics.map {
            MultiLineMetricUiModel(
                    isError = it.isError ?: true,
                    errorMsg = it.errMsg.orEmpty(),
                    type = it.type ?: MetricsType.UNKNOWN,
                    summary = MetricsSummaryUiModel(
                            title = it.summary?.title.orEmpty(),
                            valueFmt = it.summary?.value.orEmpty(),
                            state = it.summary?.state.orEmpty(),
                            description = it.summary?.description.orEmpty(),
                            lineColor = it.summary?.color.orEmpty()
                    ),
                    yAxis = it.yAxis.orEmpty()
                            .map { yAxis ->
                                YAxisUiModel(
                                        yValue = yAxis.yVal ?: 0f,
                                        yLabel = yAxis.yLabel.orEmpty()
                                )
                            },
                    linePeriod = LinePeriodUiModel(
                            currentPeriod = it.line?.currentPeriode.orEmpty().map { period ->
                                XYAxisUiModel(
                                        yVal = period.yVal ?: 0f,
                                        yLabel = period.yLabel.orEmpty(),
                                        xLabel = period.xLabel.orEmpty()
                                )
                            },
                            lastPeriod = it.line?.lastPeriode.orEmpty().map { period ->
                                XYAxisUiModel(
                                        yVal = period.yVal ?: 0f,
                                        yLabel = period.yLabel.orEmpty(),
                                        xLabel = period.xLabel.orEmpty()
                                )
                            }
                    ),
                    isEmpty = isMetricEmpty(it.line)
            )
        }
    }

    private fun isMetricEmpty(line: LineModel?): Boolean {
        val isCurrentPeriodEmpty = line?.currentPeriode?.sumBy { (it.yVal ?: 0f).toInt() } == 0
        val isLastPeriodEmpty = line?.lastPeriode?.sumBy { (it.yVal ?: 0f).toInt() } == 0
        return isCurrentPeriodEmpty && isLastPeriodEmpty
    }
}