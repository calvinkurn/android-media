package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.ChartSummaryModel
import com.tokopedia.sellerhomecommon.domain.model.GetPieChartDataResponse
import com.tokopedia.sellerhomecommon.domain.model.PieChartItemModel
import com.tokopedia.sellerhomecommon.presentation.model.ChartSummaryUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 06/07/20
 */

class PieChartMapper @Inject constructor() : BaseResponseMapper<GetPieChartDataResponse, List<PieChartDataUiModel>> {

    override fun mapRemoteDataToUiData(response: GetPieChartDataResponse, isFromCache: Boolean): List<PieChartDataUiModel> {
        return response.fetchPieChartWidgetData.data.map {
            PieChartDataUiModel(
                    dataKey = it.dataKey,
                    error = it.errorMsg,
                    data = PieChartUiModel(
                            item = mapPieChartItem(it.data.item),
                            summary = mapPieChartSummary(it.data.summary)
                    ),
                    isFromCache = isFromCache,
                    showWidget = it.showWidget
            )
        }
    }

    private fun mapPieChartItem(item: List<PieChartItemModel>): List<PieChartItemUiModel> {
        return item.map {
            PieChartItemUiModel(
                    color = it.color,
                    legend = it.legend,
                    percentage = it.percentage,
                    percentageFmt = it.percentageFmt,
                    value = it.value,
                    valueFmt = it.valueFmt
            )
        }
    }

    private fun mapPieChartSummary(summary: ChartSummaryModel): ChartSummaryUiModel {
        return ChartSummaryUiModel(
                diffPercentage = summary.diffPercentage,
                diffPercentageFmt = summary.diffPercentageFmt,
                value = summary.value,
                valueFmt = summary.valueFmt
        )
    }
}