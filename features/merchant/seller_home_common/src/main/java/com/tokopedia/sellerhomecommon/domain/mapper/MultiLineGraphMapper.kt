package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.common.const.MetricsType
import com.tokopedia.sellerhomecommon.domain.model.MultiTrendLineMetricModel
import com.tokopedia.sellerhomecommon.domain.model.MultiTrendlineWidgetDataModel
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 26/10/20
 */

class MultiLineGraphMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(items: List<MultiTrendlineWidgetDataModel>): List<MultiLineGraphDataUiModel> {
        return items.filter { !it.dataKey.isNullOrBlank() }
                .map {
                    MultiLineGraphDataUiModel(
                            dataKey = it.dataKey.orEmpty(),
                            error = it.errorMsg.orEmpty(),
                            metrics = getMetrics(it.multiTrendlineData?.metrics.orEmpty())
                    )
                }
    }

    private fun getMetrics(metrics: List<MultiTrendLineMetricModel>): List<MultiLineMetricUiModel> {
        return metrics.map {
            MultiLineMetricUiModel(
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
                                MultiLineYAxisUiModel(
                                        yValue = yAxis.yVal ?: 0f,
                                        yLabel = yAxis.yLabel.orEmpty()
                                )
                            },
                    linePeriod = LinePeriodUiModel(
                            currentPeriod = it.line?.currentPeriode.orEmpty().map { period ->
                                PeriodAxisUiModel(
                                        yValue = period.yVal ?: 0f,
                                        yLabelFmt = period.yLabel.orEmpty(),
                                        xLabelFmt = period.xLabel.orEmpty()
                                )
                            },
                            lastPeriod = it.line?.lastPeriode.orEmpty().map { period ->
                                PeriodAxisUiModel(
                                        yValue = period.yVal ?: 0f,
                                        yLabelFmt = period.yLabel.orEmpty(),
                                        xLabelFmt = period.xLabel.orEmpty()
                                )
                            }
                    )
            )
        }
    }
}