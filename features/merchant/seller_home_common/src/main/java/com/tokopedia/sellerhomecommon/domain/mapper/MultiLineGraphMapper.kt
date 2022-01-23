package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerhomecommon.common.const.MetricsType
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetMultiLineGraphResponse
import com.tokopedia.sellerhomecommon.domain.model.LineModel
import com.tokopedia.sellerhomecommon.domain.model.MultiTrendLineMetricModel
import com.tokopedia.sellerhomecommon.presentation.model.LinePeriodUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MetricsSummaryUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineMetricUiModel
import com.tokopedia.sellerhomecommon.presentation.model.XYAxisUiModel
import com.tokopedia.sellerhomecommon.presentation.model.YAxisUiModel
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 26/10/20
 */

class MultiLineGraphMapper @Inject constructor(
    private val lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface
) : BaseResponseMapper<GetMultiLineGraphResponse, List<MultiLineGraphDataUiModel>> {

    override fun mapRemoteDataToUiData(
        response: GetMultiLineGraphResponse,
        isFromCache: Boolean
    ): List<MultiLineGraphDataUiModel> {
        return response.fetchMultiTrendlineWidgetData?.fetchMultiTrendlineData.orEmpty()
            .filter { !it.dataKey.isNullOrBlank() }
            .map {
                MultiLineGraphDataUiModel(
                    dataKey = it.dataKey.orEmpty(),
                    error = it.errorMsg.orEmpty(),
                    isFromCache = isFromCache,
                    metrics = getMetrics(it.multiTrendlineData?.metrics.orEmpty()),
                    showWidget = it.showWidget.orFalse(),
                    lastUpdated = getLastUpdatedMillis(it.dataKey.orEmpty(), isFromCache)
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
                linePeriod = getLinePeriods(it),
                isEmpty = isMetricEmpty(it.line)
            )
        }
    }

    private fun getLinePeriods(lineMetricModel: MultiTrendLineMetricModel): LinePeriodUiModel {
        val empty = 0
        val currentPeriodAxisSize = lineMetricModel.line?.currentPeriode?.size ?: empty
        val lastPeriodAxisSize = lineMetricModel.line?.lastPeriode?.size ?: empty
        val lowestSize = currentPeriodAxisSize.coerceAtMost(lastPeriodAxisSize)

        val currentPeriod = if (lowestSize != empty) {
            lineMetricModel.line?.currentPeriode.takeOrDefault(lowestSize)
        } else {
            lineMetricModel.line?.currentPeriode.orEmpty()
        }

        val lastPeriod = if (lowestSize != empty) {
            lineMetricModel.line?.lastPeriode.takeOrDefault(lowestSize)
        } else {
            lineMetricModel.line?.lastPeriode.orEmpty()
        }

        val minValueOfY = 0f
        return LinePeriodUiModel(
            currentPeriod = currentPeriod.map { period ->
                XYAxisUiModel(
                    yVal = period.yVal ?: minValueOfY,
                    yLabel = period.yLabel.orEmpty(),
                    xLabel = period.xLabel.orEmpty()
                )
            },
            lastPeriod = lastPeriod.map { period ->
                XYAxisUiModel(
                    yVal = period.yVal ?: minValueOfY,
                    yLabel = period.yLabel.orEmpty(),
                    xLabel = period.xLabel.orEmpty()
                )
            }
        )
    }

    private fun isMetricEmpty(line: LineModel?): Boolean {
        val isCurrentPeriodEmpty = line?.currentPeriode?.sumOf { (it.yVal ?: 0f).toInt() } == 0
        val isLastPeriodEmpty = line?.lastPeriode?.sumOf { (it.yVal ?: 0f).toInt() } == 0
        return isCurrentPeriodEmpty && isLastPeriodEmpty
    }

    private fun <T> Iterable<T>?.takeOrDefault(take: Int): List<T> {
        return try {
            this?.take(take).orEmpty()
        } catch (e: IllegalArgumentException) {
            this?.toList().orEmpty()
        }
    }

    private fun getLastUpdatedMillis(dataKey: String, isFromCache: Boolean): Long {
        val nowMillis = Date().time
        return if (isFromCache) {
            lastUpdatedSharedPref.getLastUpdateInfoInMillis(dataKey, nowMillis)
        } else {
            saveLastUpdated(dataKey, nowMillis)
            nowMillis
        }
    }

    private fun saveLastUpdated(dataKey: String, timeInMillis: Long) {
        lastUpdatedSharedPref.saveLastUpdateInfo(dataKey, timeInMillis)
    }
}