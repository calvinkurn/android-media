package com.tokopedia.charts.model

import android.graphics.Color
import com.tokopedia.charts.model.DonutStyleConfigModel

/**
 * Created By @ilhamsuaib on 07/07/20
 */

data class PieChartConfigModel(
        val entryLabelColor: Int = Color.BLACK,
        val entryLabelTextSize: Float,
        val xAnimationDuration: Int,
        val yAnimationDuration: Int,
        val sliceSpaceWidth: Float,
        val isHalfChart: Boolean,
        val rotationEnabled: Boolean,
        val highlightPerTapEnabled: Boolean,
        val animationEnabled: Boolean,
        val showXValueEnabled: Boolean,
        val showYValueEnabled: Boolean,
        val touchEnabled: Boolean,
        val legendEnabled: Boolean,
        val donutStyleConfig: DonutStyleConfigModel
)