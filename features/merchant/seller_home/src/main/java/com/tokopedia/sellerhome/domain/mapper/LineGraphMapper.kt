package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhome.domain.model.LineGraphDataModel
import com.tokopedia.sellerhome.view.model.LineGraphDataUiModel
import com.tokopedia.sellerhome.view.model.XYAxisUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-27
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