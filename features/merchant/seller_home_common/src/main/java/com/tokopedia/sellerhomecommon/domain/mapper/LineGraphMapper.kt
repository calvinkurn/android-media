package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.domain.model.LineGraphDataModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.XYAxisUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class LineGraphMapper @Inject constructor() {

    fun mapRemoteDataModelToUiDataModel(items: List<LineGraphDataModel>): List<LineGraphDataUiModel> {
        return items.map {
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
                    }
            )
        }
    }
}