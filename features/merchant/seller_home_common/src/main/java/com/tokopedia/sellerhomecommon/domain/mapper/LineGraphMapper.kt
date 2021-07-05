package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.domain.model.GetLineGraphDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.XYAxisUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class LineGraphMapper @Inject constructor(): BaseResponseMapper<GetLineGraphDataResponse, List<LineGraphDataUiModel>> {

    override fun mapRemoteDataToUiData(response: GetLineGraphDataResponse, isFromCache: Boolean): List<LineGraphDataUiModel> {
        return response.getLineGraphData?.widgetData.orEmpty().map {
            LineGraphDataUiModel(
                    dataKey = it.dataKey.orEmpty(),
                    header = it.header.orEmpty(),
                    description = it.description.orEmpty(),
                    error = it.error.orEmpty(),
                    list = it.list.orEmpty().map { xyModel ->
                        XYAxisUiModel(
                                xLabel = xyModel.xLabel.orEmpty(),
                                yLabel = xyModel.yLabel.orEmpty(),
                                yVal = xyModel.yVal.orZero()
                        )
                    },
                    yLabels = it.yLabels.orEmpty().map { xyModel ->
                        XYAxisUiModel(
                                xLabel = xyModel.xLabel.orEmpty(),
                                yLabel = xyModel.yLabel.orEmpty(),
                                yVal = xyModel.yVal.orZero()
                        )
                    },
                    isFromCache = isFromCache,
                    showWidget = it.showWidget.orFalse()
            )
        }
    }
}