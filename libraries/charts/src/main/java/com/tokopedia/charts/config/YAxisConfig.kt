package com.tokopedia.charts.config

import com.tokopedia.charts.config.annotation.ChartConfigDsl
import com.tokopedia.charts.model.YAxisConfigModel

/**
 * Created By @ilhamsuaib on 15/07/20
 */

@ChartConfigDsl
class YAxisConfig : BaseAxisConfig() {

    companion object {
        fun getDefault(): YAxisConfigModel = create { }

        fun create(lambda: YAxisConfig.() -> Unit): YAxisConfigModel {
            return YAxisConfig().apply(lambda).build()
        }
    }

    override var labelPosition: Int = YAxisConfigModel.LABEL_OUTSIDE_CHART

    private var spaceTop: Float = 10f

    /**
     * Position can be YAxisConfigModel.LABEL_OUTSIDE_CHART or YAxisConfigModel.LABEL_INSIDE_CHART
     * */
    override fun labelPosition(lambda: () -> Int) {
        labelPosition = lambda()
    }

    fun spaceTop(lambda: () -> Float) {
        spaceTop = lambda()
    }

    fun build(): YAxisConfigModel {
        return YAxisConfigModel(
                typeface = typeface,
                isEnabled = isEnabled,
                isLabelEnabled = isLabelEnabled,
                isGridEnabled = isGridEnabled,
                mLabelPosition = labelPosition,
                spaceTop = spaceTop
        )
    }
}