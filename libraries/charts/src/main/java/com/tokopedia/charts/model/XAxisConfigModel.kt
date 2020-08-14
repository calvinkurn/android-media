package com.tokopedia.charts.model

import android.graphics.Typeface
import com.github.mikephil.charting.components.XAxis
import com.tokopedia.charts.common.utils.LabelFormatter

/**
 * Created By @ilhamsuaib on 25/06/20
 */

data class XAxisConfigModel(
        override val typeface: Typeface?,
        override val isEnabled: Boolean,
        override val isLabelEnabled: Boolean,
        override val isGridEnabled: Boolean,
        override val mLabelPosition: Int,
        override val axisMinimum: Float,
        override val labelFormatter: LabelFormatter
): BaseAxisConfigModel {

    companion object {
        const val LABEL_BOTTOM = 0
        const val LABEL_TOP = 1
        const val LABEL_BOTH_SIDE = 2
        const val LABEL_BOTTOM_INSIDE = 3
        const val LABEL_TOP_INSIDE = 4
    }

    fun getLabelPosition(): XAxis.XAxisPosition {
        return when (mLabelPosition) {
            LABEL_TOP -> XAxis.XAxisPosition.TOP
            LABEL_BOTH_SIDE -> XAxis.XAxisPosition.BOTH_SIDED
            LABEL_BOTTOM_INSIDE -> XAxis.XAxisPosition.BOTTOM_INSIDE
            LABEL_TOP_INSIDE -> XAxis.XAxisPosition.TOP_INSIDE
            else -> XAxis.XAxisPosition.BOTTOM
        }
    }
}