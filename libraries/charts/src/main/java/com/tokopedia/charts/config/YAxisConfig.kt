package com.tokopedia.charts.config

import com.tokopedia.charts.config.annotation.ChartConfigDsl
import com.tokopedia.charts.model.YAxisConfigModel

/**
 * Created By @ilhamsuaib on 15/07/20
 */

@ChartConfigDsl
class YAxisConfig : BaseAxisConfig() {

    companion object {
        const val DEFAULT_LABEL_COUNT = 6

        fun getDefault(): YAxisConfigModel = create { }

        fun create(lambda: YAxisConfig.() -> Unit): YAxisConfigModel {
            return YAxisConfig().apply(lambda).build()
        }
    }

    override var labelPosition: Int = YAxisConfigModel.LABEL_OUTSIDE_CHART

    private var spaceTop: Float = 10f
    private var labelCount: Int = DEFAULT_LABEL_COUNT

    /**
     * Position can be YAxisConfigModel.LABEL_OUTSIDE_CHART or YAxisConfigModel.LABEL_INSIDE_CHART
     * */
    override fun labelPosition(lambda: () -> Int) {
        labelPosition = lambda()
    }

    fun spaceTop(lambda: () -> Float) {
        spaceTop = lambda()
    }

    fun labelCount(lambda: () -> Int) {
        labelCount = lambda()
    }

    internal fun build(): YAxisConfigModel {
        return YAxisConfigModel(
                typeface = typeface,
                isEnabled = isEnabled,
                isLabelEnabled = isLabelEnabled,
                isGridEnabled = isGridEnabled,
                mLabelPosition = labelPosition,
                axisMinimum = axisMinimum,
                spaceTop = spaceTop,
                labelCount = labelCount,
                labelFormatter = labelFormatter
        )
    }
}