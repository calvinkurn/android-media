package com.tokopedia.charts.model

/**
 * Created By @ilhamsuaib on 07/07/20
 */

data class PieChartConfigModel(
        val entryLabelColor: Int,
        val entryLabelTextSize: Float,
        val pieChartWidth: Int,
        val pieChartHeight: Int,
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