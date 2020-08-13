package com.tokopedia.charts.config

import com.tokopedia.charts.config.annotation.ChartConfigDsl
import com.tokopedia.charts.model.XAxisConfigModel

/**
 * Created By @ilhamsuaib on 15/07/20
 */

@ChartConfigDsl
class XAxisConfig : BaseAxisConfig() {

    companion object {
        fun getDefault(): XAxisConfigModel = create { }

        fun create(lambda: XAxisConfig.() -> Unit): XAxisConfigModel {
            return XAxisConfig().apply(lambda).build()
        }
    }

    override var labelPosition: Int = XAxisConfigModel.LABEL_BOTTOM

    /**
     * Position can be XAxisConfigModel.LABEL_BOTTOM, XAxisConfigModel.LABEL_TOP, XAxisConfigModel.LABEL_BOTH_SIDE,
     * XAxisConfigModel.LABEL_BOTTOM_INSIDE or XAxisConfigModel.LABEL_TOP_INSIDE
     * */
    override fun labelPosition(lambda: () -> Int) {
        labelPosition = lambda()
    }

    fun build(): XAxisConfigModel {
        return XAxisConfigModel(
                typeface = typeface,
                isEnabled = isEnabled,
                isLabelEnabled = isLabelEnabled,
                isGridEnabled = isGridEnabled,
                mLabelPosition = labelPosition,
                axisMinimum = axisMinimum
        )
    }
}